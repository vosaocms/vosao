/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.ContentPermissionBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.ContentPermissionType;
import org.vosao.utils.UrlUtil;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionBusinessImpl extends AbstractBusinessImpl
		implements ContentPermissionBusiness {

	private static final Log logger = LogFactory
			.getLog(ContentPermissionBusinessImpl.class);

	private FolderBusiness folderBusiness;
	
	@Override
	public ContentPermissionEntity getGuestPermission(final String url) {
		GroupEntity guests = getDao().getGroupDao().getGuestsGroup();
		ContentPermissionEntity result = getGroupPermission(url, guests.getId());
		result.setUrl(url);
		return result;
	}

	@Override
	public ContentPermissionEntity getPermission(final String url,
			final UserEntity user) {
		if (user == null) {
			return getGuestPermission(url);
		}
		if (user.isAdmin()) {
			return new ContentPermissionEntity(url, ContentPermissionType.ADMIN);
		}
		List<UserGroupEntity> userGroups = getDao().getUserGroupDao()
				.selectByUser(user.getId());
		userGroups.add(new UserGroupEntity(getDao().getGroupDao()
				.getGuestsGroup().getId(), user.getId()));
		List<ContentPermissionEntity> permissions = new ArrayList<ContentPermissionEntity>();
		for (UserGroupEntity userGroup : userGroups) {
			ContentPermissionEntity contentPermission = getGroupPermission(url,
					userGroup.getGroupId());
			if (contentPermission != null) {
				permissions.add(contentPermission);
			}
		}
		ContentPermissionEntity result = consolidatePermissions(permissions);
		result.setUrl(url);
		return result;
	}

	private ContentPermissionEntity consolidatePermissions(
			List<ContentPermissionEntity> permissions) {
		ContentPermissionEntity result = new ContentPermissionEntity();
		result.setPermission(ContentPermissionType.DENIED);
		result.setAllLanguages(false);
		Set<String> languages = new HashSet<String>();
		for (ContentPermissionEntity perm : permissions) {
			if (perm.isMyPermissionHigher(result)) {
				result.setPermission(perm.getPermission());
				if (perm.isAllLanguages()) {
					result.setAllLanguages(true);
				}
				languages.addAll(perm.getLanguagesList());
			}
		}
		String langs = "";
		for (String lang : languages) {
			langs += (langs.equals("") ? "" : ",") + lang;
		}
		result.setLanguages(langs);
		return result;
	}

	private ContentPermissionEntity getGroupPermission(String url, Long groupId) {
		String myUrl = url;
		while (myUrl != null) {
			ContentPermissionEntity perm = getDao().getContentPermissionDao()
					.getByUrlGroup(myUrl, groupId);
			if (perm != null) {
				return perm;
			}
			if (myUrl.equals("/")) {
				myUrl = null;
			} else {
				myUrl = UrlUtil.getParentFriendlyURL(myUrl);
			}
		}
		;
		return null;
	}

	@Override
	public void setPermission(String url, GroupEntity group,
			ContentPermissionType permission) {
		setPermission(url, group, permission, null);
	}

	@Override
	public void setPermission(String url, GroupEntity group,
			ContentPermissionType permission, String languages) {
		ContentPermissionEntity perm = getDao().getContentPermissionDao()
				.getByUrlGroup(url, group.getId());
		if (perm == null) {
			perm = new ContentPermissionEntity();
			perm.setUrl(url);
			perm.setGroupId(group.getId());
		}
		if (languages == null) {
			perm.setAllLanguages(true);
		} else {
			perm.setAllLanguages(false);
			perm.setLanguages(languages);
		}
		perm.setPermission(permission);
		getDao().getContentPermissionDao().save(perm);
		getFolderBusiness().createFolder("/page" + url);
	}

	@Override
	public List<String> validateBeforeUpdate(ContentPermissionEntity perm) {
		List<String> errors = new ArrayList<String>();
		if (perm.getGroupId() == null) {
			errors.add("Group is empty");
		}
		if (perm.getUrl() == null) {
			errors.add("Url is empty");
		}
		if (perm.getPermission() == null) {
			errors.add("Permission is empty");
		}
		return errors;
	}

	@Override
	public List<ContentPermissionEntity> getInheritedPermissions(
			final String url) {
		List<ContentPermissionEntity> result = new ArrayList<ContentPermissionEntity>();
		String myUrl = url;
		while (myUrl != null) {
			result
					.addAll(getDao().getContentPermissionDao().selectByUrl(
							myUrl));
			if (myUrl.equals("/")) {
				myUrl = null;
			} else {
				myUrl = UrlUtil.getParentFriendlyURL(myUrl);
			}
		}
		return result;
	}

	@Override
	public List<ContentPermissionEntity> selectByUrl(String pageUrl) {
		List<ContentPermissionEntity> direct = getDao()
				.getContentPermissionDao().selectByUrl(pageUrl);
		List<ContentPermissionEntity> inherited = 
				getInheritedPermissions(UrlUtil.getParentFriendlyURL(pageUrl));
		List<ContentPermissionEntity> result = 
				new ArrayList<ContentPermissionEntity>();
		for (ContentPermissionEntity perm : inherited) {
			if (!direct.contains(perm) && !result.contains(perm)) {
				result.add(perm);
			}
		}
		for (ContentPermissionEntity perm : direct) {
			result.add(perm);
		}
		return result;
	}

	@Override
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	@Override
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}

}
