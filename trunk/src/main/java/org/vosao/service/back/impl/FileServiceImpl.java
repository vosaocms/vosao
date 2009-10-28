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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FileService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.FileVO;
import org.vosao.servlet.FolderUtil;
import org.vosao.servlet.MimeType;

public class FileServiceImpl extends AbstractServiceImpl 
		implements FileService {

	private static Log logger = LogFactory.getLog(FileServiceImpl.class);

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

	@Override
	public ServiceResponse updateContent(String fileId, String content) {
		FileEntity file = getDao().getFileDao().getById(fileId);
		if (file != null) {
			try {
				getDao().getFileDao().save(file, content.getBytes("UTF-8"));
				FolderEntity folder = getDao().getFolderDao().getById(
						file.getFolderId());
				String cacheUrl = getBusiness().getFolderBusiness()
						.getFolderPath(folder) + "/" + file.getFilename();
				getBusiness().getSystemService().getCache().remove(cacheUrl);
				return ServiceResponse.createSuccessResponse(
						"File was successfully updated");
			} catch (UnsupportedEncodingException e) {
				return ServiceResponse.createErrorResponse(
						"Error! Unsupported encoding.");
			}
		}
		else {
			return ServiceResponse.createErrorResponse("File not found " 
					+ fileId);
		}
	}

	@Override
	public FileVO getFile(String id) {
		try {
			if (StringUtils.isEmpty(id)) {
				return null;
			}
			FileEntity file = getDao().getFileDao().getById(id);
			FileVO vo = new FileVO(file);
			FolderEntity folder = getDao().getFolderDao().getById(file
				.getFolderId());
			vo.setLink("/file" + getBusiness().getFolderBusiness()
					.getFolderPath(folder) + "/" + file.getFilename());
			String ext = FolderUtil.getFileExt(file.getFilename());
			vo.setTextFile(getBusiness().getConfigBusiness().isTextFileExt(ext));
			vo.setImageFile(getBusiness().getConfigBusiness()
					.isImageFileExt(ext));
			vo.setContent(new String(getDao().getFileDao().getFileContent(file), 
					"UTF-8"));
			return vo;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public ServiceResponse saveFile(Map<String, String> vo) {
		FileEntity file = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			file = getDao().getFileDao().getById(vo.get("id"));
		}
		if (file == null) {
			file = new FileEntity();
		}
		file.setFilename(vo.get("name"));
		file.setTitle(vo.get("title"));
		file.setFolderId(vo.get("folderId"));
		file.setLastModifiedTime(new Date());
		List<String> errors = getBusiness().getFileBusiness()
			.validateBeforeUpdate(file);
		if (errors.isEmpty()) {
			FolderEntity folder = getDao().getFolderDao().getById(
					file.getFolderId());
			file.setMimeType(MimeType.getContentTypeByExt(
					FolderUtil.getFileExt(file.getFilename())));
			String cacheUrl = getBusiness().getFolderBusiness()
					.getFolderPath(folder) + "/" + file.getFilename();
			getBusiness().getSystemService().getCache().remove(cacheUrl);
			getDao().getFileDao().save(file);
			return ServiceResponse.createSuccessResponse(
					"File was successfully saved.");
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Errors occured during file save", errors);
		}
	}

}
