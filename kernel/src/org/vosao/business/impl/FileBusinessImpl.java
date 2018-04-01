/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FolderPermissionBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.i18n.Messages;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.MimeType;

public class FileBusinessImpl extends AbstractBusinessImpl 
	implements FileBusiness {

	@Override
	public List<String> validateBeforeUpdate(final FileEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(entity.getFilename())) {
			errors.add(Messages.get("filename_is_empty"));
		}
		else {
			FileEntity file = getDao().getFileDao().getByName(
					entity.getFolderId(), entity.getFilename());
			if (file != null && 
				(entity.isNew() 
				||
				(!entity.isNew() && !file.getId().equals(entity.getId())))) {
				errors.add(Messages.get("file_already_exists"));
			}
		}
		if (StringUtils.isEmpty(entity.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		return errors;
	}

	@Override
	public byte[] readFile(String filename) {
		try {
			String path = FolderUtil.getFilePath(filename);
			String name = FolderUtil.getFileName(filename);
			FolderEntity folder = getFolderBusiness().findFolderByPath(
					getFolderBusiness().getTree(), path).getEntity();
			if (folder == null) {
				return null;
			}
			FileEntity file = getDao().getFileDao().getByName(folder.getId(), name);
			if (file != null) {
				return getDao().getFileDao().getFileContent(file);
			}
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FileEntity saveFile(String filename, byte[] data) {
		try {
			String path = FolderUtil.getFilePath(filename);
			String name = FolderUtil.getFileName(filename);
			FolderEntity folder = getFolderBusiness().createFolder(path);
			FileEntity file = getDao().getFileDao().getByName(folder.getId(), name);
			if (file == null) {
				file = new FileEntity(name, name, folder.getId(), 
						MimeType.getContentTypeByExt(FolderUtil.getFileExt(filename)), 
						new Date(), data.length);
			}
			getDao().getFileDao().save(file, data);
			getSystemService().getFileCache().remove(filename);
			return file;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private FolderBusiness getFolderBusiness() {
		return getBusiness().getFolderBusiness();
	}

	private FolderPermissionBusiness getFolderPermissionBusiness() {
		return getBusiness().getFolderPermissionBusiness();
	}

	@Override
	public FileEntity findFile(String filename) {
		try {
			String path = FolderUtil.getFilePath(filename);
			String name = FolderUtil.getFileName(filename);
			TreeItemDecorator<FolderEntity> folder = getFolderBusiness()
					.findFolderByPath(getFolderBusiness().getTree(), path);
			if (folder == null) {
				return null;
			}
			return getDao().getFileDao().getByName(folder.getEntity().getId(), 
					name);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void remove(String filename) {
		String path = FolderUtil.getFilePath(filename);
		String name = FolderUtil.getFileName(filename);
		TreeItemDecorator<FolderEntity> folder = getFolderBusiness()
				.findFolderByPath(getFolderBusiness().getTree(), path);
		if (folder == null) {
			logger.error("Folder not found. " + path);
			return;
		}
		FileEntity file = getDao().getFileDao().getByName(folder.getEntity()
				.getId(), name);
		if (file == null) {
			logger.error("File not found. " + filename);
			return;
		}
		FolderPermissionEntity perm = getFolderPermissionBusiness()
				.getPermission(folder.getEntity(), 
						VosaoContext.getInstance().getUser());
		if (perm.isChangeGranted()) {
			getDao().getFileDao().remove(file.getId());
			getSystemService().getFileCache().remove(filename);
		}
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			FileEntity file = getDao().getFileDao().getById(id);
			if (file != null) {
				getSystemService().getFileCache().remove(getFilePath(file));
			}
		}
		getDao().getFileDao().remove(ids);
	}

	@Override
	public String getFilePath(FileEntity file) {
		return getDao().getFolderDao().getFolderPath(file.getFolderId())
				+ "/" +file.getFilename();
	}
	
}
