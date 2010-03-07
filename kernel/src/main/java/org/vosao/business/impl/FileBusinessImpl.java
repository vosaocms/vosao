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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.MimeType;
import org.vosao.utils.FolderUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FileBusinessImpl extends AbstractBusinessImpl 
	implements FileBusiness {

	private static final Log logger = LogFactory.getLog(FileBusinessImpl.class);
	
	private FolderBusiness folderBusiness;
	
	@Override
	public List<String> validateBeforeUpdate(final FileEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtil.isEmpty(entity.getFilename())) {
			errors.add("Filename is empty");
		}
		else {
			FileEntity file = getDao().getFileDao().getByName(
					entity.getFolderId(), entity.getFilename());
			if ((entity.isNew() && file != null)
				||
				(!entity.isNew()) && !file.getId().equals(entity.getId()) ) {
				errors.add("File with such name already exists in this folder");
			}
		}
		if (StringUtil.isEmpty(entity.getTitle())) {
			errors.add("Title is empty");
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
			return file;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	public void setFolderBusiness(FolderBusiness folderBusiness) {
		this.folderBusiness = folderBusiness;
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
	
}
