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

package org.vosao.service.back.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.helper.GroupHelper;
import org.vosao.enums.ContentPermissionType;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.ContentPermissionService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.ContentPermissionVO;
import org.vosao.utils.UrlUtil;

import com.google.appengine.repackaged.com.google.protobuf.ServiceException;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ContentPermissionServiceImpl extends AbstractServiceImpl 
		implements ContentPermissionService {

	@Override
	public ServiceResponse remove(List<String> ids) {
		List<Long> idList = new ArrayList<Long>();
		for (String id : ids) {
			idList.add(Long.valueOf(id));
		}
		getDao().getContentPermissionDao().remove(idList);
		return ServiceResponse.createSuccessResponse(
				Messages.get("content_permissions_success_delete"));
	}

	@Override
	public ContentPermissionEntity getById(Long id) {
		return getDao().getContentPermissionDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		try {
			GroupEntity group = getDao().getGroupDao().getById(Long.valueOf((
				vo.get("groupId"))));
			if (group == null) {
				throw new ServiceException(Messages.get("group_not_found"));
			}
			String url = vo.get("url");
			ContentPermissionType perm = ContentPermissionType.valueOf(
				vo.get("permission"));
			String languages = StringUtils.isEmpty(vo.get("languages")) ? null : 
				vo.get("languages"); 
			getBusiness().getContentPermissionBusiness().setPermission(
					url, group, perm, languages);
			return ServiceResponse.createSuccessResponse(
					Messages.get("content_permissions_success_save"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.toString() + " " 
					+ e.getMessage());
		}
	}

	@Override
	public List<ContentPermissionVO> selectByUrl(String pageUrl) {
		List<ContentPermissionEntity> direct = getDao()
				.getContentPermissionDao().selectByUrl(pageUrl);
		List<ContentPermissionEntity> inherited = getBusiness()
				.getContentPermissionBusiness().getInheritedPermissions(
						UrlUtil.getParentFriendlyURL(pageUrl));
		Map<Long, GroupEntity> groups = GroupHelper.createIdMap(getDao()
				.getGroupDao().select());
		List<ContentPermissionVO> result = new ArrayList<ContentPermissionVO>();
		for (ContentPermissionEntity perm : inherited) {
			if (!containsPermission(direct, perm)
				&& !containsPermissionVO(result, perm)) {
				ContentPermissionVO vo = new ContentPermissionVO(perm);
				vo.setInherited(true);
				vo.setGroup(groups.get(perm.getGroupId()));
				result.add(vo);
			}
		}
		for (ContentPermissionEntity perm : direct) {
			ContentPermissionVO vo = new ContentPermissionVO(perm);
			vo.setInherited(false);
			vo.setGroup(groups.get(perm.getGroupId()));
			result.add(vo);
		}
		return result;
	}

	private boolean containsPermission(List<ContentPermissionEntity> list,
			ContentPermissionEntity permission) {
		for (ContentPermissionEntity perm : list) {
			if (perm.getGroupId().equals(permission.getGroupId())) {
				return true;
			}
		}
		return false;
	}

	private boolean containsPermissionVO(List<ContentPermissionVO> list,
			ContentPermissionEntity permission) {
		for (ContentPermissionVO perm : list) {
			if (perm.getGroup().getId().equals(permission.getGroupId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ContentPermissionEntity getPermission(String url) {
		return getBusiness().getContentPermissionBusiness().getPermission(
				url, VosaoContext.getInstance().getUser());
	}
	
}
