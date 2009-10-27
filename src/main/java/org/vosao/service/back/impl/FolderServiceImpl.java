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

import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FolderService;
import org.vosao.service.impl.AbstractServiceImpl;

public class FolderServiceImpl extends AbstractServiceImpl 
		implements FolderService {

	@Override
	public TreeItemDecorator<FolderEntity> getTree() {
		return getBusiness().getFolderBusiness().getTree();
	}

	@Override
	public String getFolderPath(final String folderId) {
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder != null) {
			return getBusiness().getFolderBusiness().getFolderPath(folder);
		}
		return null;
	}

	@Override
	public FolderEntity getFolder(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return getDao().getFolderDao().getById(id);
	}

	@Override
	public List<FolderEntity> getByParent(String id) {
		return getDao().getFolderDao().getByParent(id);
	}

	@Override
	public ServiceResponse saveFolder(Map<String, String> vo) {
		FolderEntity folder = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			folder = getDao().getFolderDao().getById(vo.get("id"));
		}
		if (folder == null) {
			folder = new FolderEntity();
		}
		folder.setName(vo.get("name"));
		folder.setTitle(vo.get("title"));
		folder.setParent(vo.get("parent"));
		List<String> errors = getBusiness().getFolderBusiness()
			.validateBeforeUpdate(folder);
		if (errors.isEmpty()) {
			getDao().getFolderDao().save(folder);
			return ServiceResponse.createSuccessResponse(
					"Folder was successfully saved.");
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Errors occured during folder save", errors);
		}
	}

	@Override
	public ServiceResponse deleteFolder(List<String> ids) {
		getDao().getFolderDao().remove(ids);
		return ServiceResponse.createSuccessResponse(
				"Folders were successsfully deleted.");
	}


}
