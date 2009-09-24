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

package org.vosao.service.impl;

import java.util.List;

import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.service.FileService;
import org.vosao.service.ServiceResponse;

public class FileServiceImpl extends AbstractServiceImpl 
		implements FileService {

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		return getDao().getFileDao().getByFolder(folderId);
	}

	@Override
	public ServiceResponse deleteFiles(List<String> fileIds) {
		getDao().getFileDao().remove(fileIds);
		return new ServiceResponse("success", "Files was successfully deleted.");
	}

	@Override
	public String getFilePath(String fileId) {
		FileEntity file = getDao().getFileDao().getById(fileId);
		if (file != null) {
			FolderEntity folder = getDao().getFolderDao().getById(
					file.getFolderId());
			return "/file" + getBusiness().getFolderBusiness().getFolderPath(
					folder) + "/" + file.getFilename();
		}
		return "";
	}

}
