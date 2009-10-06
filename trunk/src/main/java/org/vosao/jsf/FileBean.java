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

package org.vosao.jsf;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.servlet.FolderUtil;
import org.vosao.servlet.MimeType;


public class FileBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 3L;
	private static Log log = LogFactory.getLog(FileBean.class);
	
	private FileEntity current;
	private FolderEntity folder;
	private String id;
	private String folderId;
	private String content;

	public void init() {
		initCurrent();
	}
	
	private void initCurrent() {
		content = "";
		current = new FileEntity();
		current.setFolderId(folderId);
		if (getCurrentId() != null) {
			current = getDao().getFileDao().getById(getCurrentId());
			if (current != null) {
				if (isEditContent()) {
					try {
						content = new String(getDao().getFileDao()
								.getFileContent(current), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						JSFUtil.addErrorMessage("Encoding error while getting file content");
					}
				}
			}
		}
		if (current.getFolderId() != null) {
			folder = getDao().getFolderDao().getById(current.getFolderId());
		}
	}
	
	public String cancelEdit() {
		return "pretty:folders";
	}
	
	public String update() throws UnsupportedEncodingException {
		List<String> errors = getBusiness().getFileBusiness()
			.validateBeforeUpdate(current);
		if (errors.isEmpty()) {
			current.setMimeType(MimeType.getContentTypeByExt(
					FolderUtil.getFileExt(current.getFilename())));
			getDao().getFileDao().save(current, content.getBytes("UTF-8"));
			String cacheUrl = getBusiness().getFolderBusiness()
				.getFolderPath(folder) + "/" + current.getFilename();
			getBusiness().getSystemService().getCache().remove(cacheUrl);
			setCurrentId(current.getId());
			initCurrent();
			JSFUtil.addInfoMessage("File was successfully saved.");
			return "pretty:fileUpdate";
		}
		else {
			JSFUtil.addErrorMessages(errors);
			return null;
		}
	}
	
	public void create() {
		setCurrentId(null);
		initCurrent();
	}
	
	public void edit() {
		if (id != null) {
			setCurrentId(id);
			initCurrent();
		}
	}
	
	public FileEntity getCurrent() {
		return current;
	}

	public void setCurrent(FileEntity current) {
		this.current = current;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentId() {
		String name = this.getClass().getName() + "currentId";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setCurrentId(String data) {
		String name = this.getClass().getName() + "currentId";
		JSFUtil.setSessionObject(name, data);
	}
	
	public boolean isEditContent() {
		if (current.getId() == null) {
			return false;
		}
		String editExt = getBusiness().getConfigBusiness().getConfig()
				.getEditExt();
		String[] exts = editExt.split(",");
		for (String ext : exts) {
			if (FolderUtil.getFileExt(current.getFilename()).equals(ext)) {
				return true;
			}
		}
		return false;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isImageContent() {
		if (current.getId() == null) {
			return false;
		}
		return current.isImage();
	}
	
	public String getFileLink() {
		if (folder != null) {
			return "/file" + getBusiness().getFolderBusiness()
				.getFolderPath(folder) + "/" + current.getFilename();
		}
		return "";
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}
	
}
