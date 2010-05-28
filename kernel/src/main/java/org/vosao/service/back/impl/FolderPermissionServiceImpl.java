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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.vosao.common.VosaoContext;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.helper.GroupHelper;
import org.vosao.enums.FolderPermissionType;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FolderPermissionService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.FolderPermissionVO;
import org.vosao.utils.StrUtil;

import com.google.appengine.repackaged.com.google.protobuf.ServiceException;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionServiceImpl extends AbstractServiceImpl 
		implements FolderPermissionService {

	@Override
	public ServiceResponse remove(List<String> ids) {
		getDao().getFolderPermissionDao().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				Messages.get("folder.permissions_success_delete"));
	}

	@Override
	public FolderPermissionEntity getById(Long id) {
		return getDao().getFolderPermissionDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		try {
			GroupEntity group = getDao().getGroupDao().getById(Long.valueOf((
				vo.get("groupId"))));
			if (group == null) {
				throw new ServiceException(Messages.get("group_not_found"));
			}
			FolderEntity folder = getDao().getFolderDao().getById(
					Long.valueOf(vo.get("folderId")));
			if (folder == null) {
				throw new ServiceException(Messages.get("folder.not_found",
						vo.get("folderId")));
			}
			FolderPermissionType perm = FolderPermissionType.valueOf(
				vo.get("permission"));
			getBusiness().getFolderPermissionBusiness().setPermission(
					folder, group, perm);
			return ServiceResponse.createSuccessResponse(
				Messages.get("folder.permission_success_save"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.toString() + " " 
					+ e.getMessage());
		}
	}

	@Override
	public List<FolderPermissionVO> selectByFolder(Long folderId) {
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder == null) {
			return Collections.EMPTY_LIST;
		}
		List<FolderPermissionEntity> inherited = getBusiness()
				.getFolderPermissionBusiness().getInheritedPermissions(folder);
		//logger.info("inherited " + inherited.size());
		List<FolderPermissionEntity> direct = getDao()
				.getFolderPermissionDao().selectByFolder(folderId);
		//logger.info("direct " + direct.size());
		Map<Long, GroupEntity> groups = GroupHelper.createIdMap(getDao()
				.getGroupDao().select());
		List<FolderPermissionVO> result = new ArrayList<FolderPermissionVO>();
		for (FolderPermissionEntity perm : inherited) {
			if (!containsPermission(direct, perm)
				&& !containsPermissionVO(result, perm)) {
				FolderPermissionVO vo = new FolderPermissionVO(perm);
				vo.setInherited(true);
				vo.setGroup(groups.get(perm.getGroupId()));
				result.add(vo);
			}
		}
		for (FolderPermissionEntity perm : direct) {
			FolderPermissionVO vo = new FolderPermissionVO(perm);
			vo.setInherited(false);
			vo.setGroup(groups.get(perm.getGroupId()));
			result.add(vo);
		}
		return result;
	}

	private boolean containsPermission(List<FolderPermissionEntity> list,
			FolderPermissionEntity permission) {
		for (FolderPermissionEntity perm : list) {
			if (perm.getGroupId().equals(permission.getGroupId())) {
				return true;
			}
		}
		return false;
	}

	private boolean containsPermissionVO(List<FolderPermissionVO> list,
			FolderPermissionEntity permission) {
		for (FolderPermissionVO perm : list) {
			if (perm.getGroup().getId().equals(permission.getGroupId())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public FolderPermissionEntity getPermission(Long folderId) {
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder != null) {
			return getBusiness().getFolderPermissionBusiness().getPermission(
				folder, VosaoContext.getInstance().getUser());
		}
		return null;
	}
	
}
