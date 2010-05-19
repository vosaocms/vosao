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

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.ContentPermissionBusiness;
import org.vosao.business.FolderPermissionBusiness;
import org.vosao.common.Messages;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.FolderPermissionType;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionBusinessImpl extends AbstractBusinessImpl 
	implements FolderPermissionBusiness {

	private static final String PAGE = "/page";
	
	private ContentPermissionBusiness contentPermissionBusiness;
	
	@Override
	public FolderPermissionEntity getGuestPermission(
			final FolderEntity folder) {
		GroupEntity guests = getDao().getGroupDao().getGuestsGroup();
		return getGroupPermission(folder, guests.getId());
	}

	@Override
	public FolderPermissionEntity getPermission(final FolderEntity folder, 
			final UserEntity user) {
		if (user == null) {
			return getGuestPermission(folder);
		}
		if (user.isAdmin()) {
			return new FolderPermissionEntity(folder.getId(), 
					FolderPermissionType.ADMIN);
		}
		List<UserGroupEntity> userGroups = getDao().getUserGroupDao()
				.selectByUser(user.getId());
		userGroups.add(new UserGroupEntity(getDao().getGroupDao()
				.getGuestsGroup().getId(), user.getId()));
		List<FolderPermissionEntity> permissions = 
				new ArrayList<FolderPermissionEntity>();
		for (UserGroupEntity userGroup : userGroups) {
			FolderPermissionEntity FolderPermission = getGroupPermission(folder, 
					userGroup.getGroupId());
			if (FolderPermission != null) {
				permissions.add(FolderPermission);
			}
		}
		permissions.add(getPagePermission(folder));
		FolderPermissionEntity result = consolidatePermissions(permissions);
		result.setFolderId(folder.getId());
		return result;
	}

	private FolderPermissionEntity consolidatePermissions(
			List<FolderPermissionEntity> permissions) {
		FolderPermissionEntity result = new FolderPermissionEntity();
		result.setPermission(FolderPermissionType.DENIED);
		for (FolderPermissionEntity perm : permissions) {
			if (perm.isMyPermissionHigher(result)) {
				result.setPermission(perm.getPermission());
			}
		}
		return result;
	}

	private FolderPermissionEntity getGroupPermission(FolderEntity folder,
			Long groupId) {
		FolderEntity myFolder = folder;
		FolderPermissionEntity result = new FolderPermissionEntity(folder.getId());
		result.setPermission(FolderPermissionType.DENIED);
		while (myFolder != null) {
			FolderPermissionEntity perm = getDao().getFolderPermissionDao()
				.getByFolderGroup(myFolder.getId(), groupId);
			if (perm != null) {
				return perm;
			}
			if (myFolder.isRoot()) {
				myFolder = null;
			}
			else {
				myFolder = getDao().getFolderDao().getById(myFolder.getParent());
			}
		};		
		return result;
	}

	@Override
	public void setPermission(FolderEntity folder, GroupEntity group,
			FolderPermissionType permission) {
		FolderPermissionEntity perm = getDao().getFolderPermissionDao()
				.getByFolderGroup(folder.getId(), group.getId());
		if (perm == null) {
			perm = new FolderPermissionEntity();
			perm.setFolderId(folder.getId());
			perm.setGroupId(group.getId());
		}
		perm.setPermission(permission);
		getDao().getFolderPermissionDao().save(perm);
	}

	@Override
	public List<String> validateBeforeUpdate(FolderPermissionEntity perm) {
		List<String> errors = new ArrayList<String>();
		if (perm.getGroupId() == null) {
			errors.add(Messages.get("group_is_empty"));
		}
		if (perm.getFolderId() == null) {
			errors.add(Messages.get("folder_is_empty"));
		}
		if (perm.getPermission() == null) {
			errors.add(Messages.get("permission_is_empty"));
		}
		return errors;
	}

	@Override
	public List<FolderPermissionEntity> getInheritedPermissions(
			final FolderEntity folder) {
		List<FolderPermissionEntity> result = 
				new ArrayList<FolderPermissionEntity>();
		FolderEntity myFolder = getDao().getFolderDao().getById(folder.getId());
		while (myFolder != null) {
			result.addAll(getDao().getFolderPermissionDao().selectByFolder(
					myFolder.getId()));			
			if (myFolder.isRoot()) {
				myFolder = null;
			}
			else {
				myFolder = getDao().getFolderDao().getById(myFolder.getParent());
			}
		}
		result.addAll(getPagePermissions(folder));
		return result;
	}

	private FolderPermissionEntity getPagePermission(FolderEntity folder) {
		String path = getDao().getFolderDao().getFolderPath(folder.getId());
		FolderPermissionEntity result = new FolderPermissionEntity();
		result.setPermission(FolderPermissionType.DENIED);
		if (path.startsWith(PAGE)) {
			String pageUrl = getFolderFromPageUrl(path);			
			ContentPermissionEntity pagePermission = 
				getContentPermissionBusiness().getPermission(
					pageUrl, VosaoContext.getInstance().getUser());
			result.setPermission(FolderPermissionType
					.fromContentPermissionType(pagePermission.getPermission()));
		}
		return result;
	}

	private String getFolderFromPageUrl(String url) {
		return url.equals(PAGE) ? "/" : url.substring(PAGE.length()); 
	}
	
	private List<FolderPermissionEntity> getPagePermissions(
			FolderEntity folder) {
		String path = getDao().getFolderDao().getFolderPath(folder.getId());
		//logger.info(folder.getName() + " " + path);
		List<FolderPermissionEntity> result = 
			new ArrayList<FolderPermissionEntity>();
		if (path.startsWith(PAGE)) {
			String pageUrl = getFolderFromPageUrl(path); 
			List<ContentPermissionEntity> pagePermissions = 
				getContentPermissionBusiness().selectByUrl(pageUrl);
			//logger.info(pagePermissions.size());
			for (ContentPermissionEntity perm : pagePermissions) {
				FolderPermissionEntity p = new FolderPermissionEntity();
				p.setId(-perm.getId());
				p.setPermission(FolderPermissionType
						.fromContentPermissionType(perm.getPermission()));
				p.setFolderId(folder.getId());
				p.setGroupId(perm.getGroupId());
				result.add(p);
			}
		}
		return result;
	}
	
	@Override
	public ContentPermissionBusiness getContentPermissionBusiness() {
		return contentPermissionBusiness;
	}

	@Override
	public void setContentPermissionBusiness(ContentPermissionBusiness bean) {
		contentPermissionBusiness = bean;		
	}
	
	
}
