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

package org.vosao.service.back.impl;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.helper.FileHelper;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FileService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.FileVO;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.MimeType;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FileServiceImpl extends AbstractServiceImpl 
		implements FileService {

	@Override
	public List<FileEntity> getByFolder(Long folderId) {
		List<FileEntity> result = getDao().getFileDao().getByFolder(folderId);
		Collections.sort(result, new FileHelper.NameAsc());
		return result;
	}

	@Override
	public ServiceResponse deleteFiles(List<String> fileIds) {
		getBusiness().getFileBusiness().remove(StrUtil.toLong(fileIds));
		return new ServiceResponse("success", Messages.get(
				"files.success_delete"));
	}

	@Override
	public String getFilePath(Long fileId) {
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
	public ServiceResponse updateContent(Long fileId, String content) {
		FileEntity file = getDao().getFileDao().getById(fileId);
		if (file != null) {
			try {
				getDao().getFileDao().save(file, content.getBytes("UTF-8"));
				FolderEntity folder = getDao().getFolderDao().getById(
						file.getFolderId());
				String cacheUrl = getBusiness().getFolderBusiness()
						.getFolderPath(folder) + "/" + file.getFilename();
				getBusiness().getSystemService().getFileCache().remove(cacheUrl);
				return ServiceResponse.createSuccessResponse(
						Messages.get("file.success_update"));
			} catch (UnsupportedEncodingException e) {
				return ServiceResponse.createErrorResponse(
						Messages.get("unsupported_encoding"));
			}
		}
		else {
			return ServiceResponse.createErrorResponse(Messages.get(
					"file.not_found"));
		}
	}

	@Override
	public FileVO getFile(Long id) {
		return getFile(id, "UTF-8");
	}
	
	@Override
	public FileVO getFile(Long id, String encoding) {
		try {
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
			if (vo.isTextFile()) {
				vo.setContent(new String(getDao().getFileDao()
						.getFileContent(file), encoding));
			}
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
			file = getDao().getFileDao().getById(Long.valueOf(vo.get("id")));
		}
		if (file == null) {
			file = new FileEntity();
		}
		file.setFilename(vo.get("name"));
		file.setTitle(vo.get("title"));
		file.setFolderId(Long.valueOf(vo.get("folderId")));
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
			getBusiness().getSystemService().getFileCache().remove(cacheUrl);
			getDao().getFileDao().save(file);
			return ServiceResponse.createSuccessResponse(
					Messages.get("file.success_update"), file.getId());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

}
