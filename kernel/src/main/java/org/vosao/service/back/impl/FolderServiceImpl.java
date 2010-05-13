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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.Messages;
import org.vosao.entity.FolderEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FileService;
import org.vosao.service.back.FolderPermissionService;
import org.vosao.service.back.FolderService;
import org.vosao.service.back.GroupService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.FolderRequestVO;
import org.vosao.utils.ParamUtil;
import org.vosao.utils.StrUtil;

/**
 * @author Alexander Oleynik
 */
public class FolderServiceImpl extends AbstractServiceImpl 
		implements FolderService {

	private FileService fileService;
	private FolderPermissionService folderPermissionService;
	private GroupService groupService;
	
	@Override
	public TreeItemDecorator<FolderEntity> getTree() {
		return getBusiness().getFolderBusiness().getTree();
	}

	@Override
	public String getFolderPath(final Long folderId) {
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder != null) {
			return getBusiness().getFolderBusiness().getFolderPath(folder);
		}
		return null;
	}

	@Override
	public FolderEntity getFolder(Long id) {
		return getBusiness().getFolderBusiness().getById(id);
	}

	@Override
	public List<FolderEntity> getByParent(Long id) {
		return getBusiness().getFolderBusiness().getByParent(id);
	}

	@Override
	public ServiceResponse saveFolder(Map<String, String> vo) {
		FolderEntity folder = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			folder = getDao().getFolderDao().getById(Long.valueOf(vo.get("id")));
		}
		if (folder == null) {
			folder = new FolderEntity();
		}
		folder.setName(vo.get("name"));
		folder.setTitle(vo.get("title"));
		folder.setParent(ParamUtil.getLong(vo.get("parent"), null));
		List<String> errors = getBusiness().getFolderBusiness()
			.validateBeforeUpdate(folder);
		if (errors.isEmpty()) {
			getDao().getFolderDao().save(folder);
			return ServiceResponse.createSuccessResponse(folder.getId().toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public ServiceResponse deleteFolder(List<String> ids) {
		getBusiness().getFolderBusiness().recursiveRemove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				Messages.get("folder.successs_delete"));
	}

	@Override
	public FolderRequestVO getFolderRequest(Long folderId, Long folderParentId) {
		FolderRequestVO result = new FolderRequestVO();
		result.setFolder(getFolder(folderId));
		Long permFolderId = folderParentId;
		if (result.getFolder() != null) {
			result.setChildren(getByParent(folderId));
			result.setFiles(getFileService().getByFolder(folderId));
			result.setPermissions(getFolderPermissionService().selectByFolder(
					folderId));
			permFolderId = folderId;
		}
		result.setGroups(getGroupService().select());
		result.setFolderPermission(getFolderPermissionService().getPermission(
				permFolderId));
		return result;
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFolderPermissionService(FolderPermissionService bean) {
		folderPermissionService = bean;
	}

	public FolderPermissionService getFolderPermissionService() {
		return folderPermissionService;
	}

	public void setGroupService(GroupService bean) {
		groupService = bean;
	}
	
	public GroupService getGroupService() {
		return groupService;
	}

	public void setFileService(FileService bean) {
		fileService = bean;
	}

	@Override
	public TreeItemDecorator<FolderEntity> getFolderByPath(
			String path) {
		return getBusiness().getFolderBusiness().findFolderByPath(
				getBusiness().getFolderBusiness().getTree(), path);
	}

	@Override
	public FolderEntity createFolderByPath(String path) 
			throws UnsupportedEncodingException {
		return getBusiness().getFolderBusiness().createFolder(path);
	}


}
