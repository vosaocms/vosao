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

package org.vosao.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.vosao.business.mq.message.ImportMessage;
import org.vosao.common.UploadException;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.i18n.Messages;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.MimeType;
import org.vosao.utils.StreamUtil;

/**
 * Servlet for uploading images into database.
 * 
 * @author Aleksandr Oleynik
 */
public class FileUploadServlet extends AbstractServlet {
	
	private static class UploadItem {
		public FileItemStream item;
		public byte[] data;
	}
	
	private static final long serialVersionUID = 6098745782027999297L;

	private static final long MAX_SIZE = 128000000;

	private static final String FOLDER_PARAM = "folderId";
	
	private static final String FILE_TYPE_PARAM = "fileType";
	private static final String FILE_TYPE_RESOURCE = "resource";
	private static final String FILE_TYPE_IMPORT = "import";
	private static final String FILE_TYPE_PLUGIN = "plugin";
	private static final String FILE_TYPE_PICASA = "picasa";

	private static final String TEXT_MESSAGE = "::%s::%s::";

	public static final String IMAGE_UPLOAD_PAGE_ID = "imageUploadPageId";
	
	private String ckeditorFuncNum = "";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//logger.info("File upload...");
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(MAX_SIZE);
		upload.setHeaderEncoding("UTF-8");
		String message = null;
		Map<String, String> parameters = new HashMap<String, String>();
		List<UploadItem> uploadItems = new ArrayList<UploadItem>();
		try {
			FileItemIterator iter;
			try {
				iter = upload.getItemIterator(request);
				FileItemStream imageFileItem = null;
				String folder = null;
				InputStream stream = null;
				InputStream filestream = null;
				byte[] fileData = null;
				parameters.put(IMAGE_UPLOAD_PAGE_ID, 
						VosaoContext.getInstance().getSession().getString(
								IMAGE_UPLOAD_PAGE_ID));
				
				if (request.getParameter("CKEditorFuncNum") != null) {
					ckeditorFuncNum = request.getParameter("CKEditorFuncNum");
				}
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (item.isFormField()) {
						parameters.put(item.getFieldName(), 
								Streams.asString(stream, "UTF-8"));
					} else {
						UploadItem uploadItem = new UploadItem();
						uploadItem.item = item;
						uploadItem.data = StreamUtil.readFileStream(stream);
						uploadItems.add(uploadItem);
					}
				}
				//logger.info(parameters.toString());
				for (UploadItem item : uploadItems) {
					message = processFile(item.item, item.data, parameters);
				}
			} catch (FileUploadException e) {
				logger.error(Messages.get("request_parsing_error"));
				throw new UploadException(Messages.get("request_parsing_error"));
			}
		} catch (UploadException e) {
			message = createMessage("error", e.getMessage()); 
			logger.error(message);
		}
		if (isCKeditorUpload(parameters)) {
			response.setContentType("text/html");
		}
		else {
			response.setContentType("text/plain");
		}
		response.setStatus(200);
		response.getWriter().write(message);
	}

	private String createMessage(final String result, final String message) {
		return String.format(TEXT_MESSAGE, result, message);
	}
	
	private String processFile(FileItemStream fileItem, byte[] data, 
			Map<String, String> parameters) throws UploadException {
		
		if (!parameters.containsKey(FILE_TYPE_PARAM)) {
			if (!parameters.containsKey(IMAGE_UPLOAD_PAGE_ID)) {
				throw new UploadException(Messages.get(
						"file.type_not_specified"));
			}
			else {
				return processResourceFileCKeditor(fileItem, data, 
						Long.valueOf(parameters.get(IMAGE_UPLOAD_PAGE_ID)));
			}
		}
		String fileType = parameters.get(FILE_TYPE_PARAM);
		if (fileType.equals(FILE_TYPE_RESOURCE)) {
			if (!parameters.containsKey(FOLDER_PARAM)) {
				throw new UploadException(Messages.get(
						"folder.parameter_not_specified"));
			}
			return processResourceFileJSON(fileItem, data, 
					getFolder(Long.valueOf(parameters.get(FOLDER_PARAM))));
		}
		if (fileType.equals(FILE_TYPE_IMPORT)) {
			return processImportFile(fileItem, data);
		}
		if (fileType.equals(FILE_TYPE_PLUGIN)) {
			return processPluginFile(fileItem, data);
		}
		if (fileType.equals(FILE_TYPE_PICASA)) {
			return processPicasaFile(fileItem, data, parameters);
		}
		return null;
	}
	
	private String processResourceFileJSON(FileItemStream imageItem, byte[] data, 
			FolderEntity folder) throws UploadException {
		FileEntity file = processResourceFile(imageItem, data, folder);
		String message = createMessage("success", file.getId().toString());
		return message;
	}
	
	/**
	 * 
	 * Process uploaded file.
	 * 
	 * @param request
	 *            - Http request.
	 * @param item
	 *            - multipart item.
	 * @return Status string.
	 */
	private FileEntity processResourceFile(FileItemStream imageItem, byte[] data, 
			FolderEntity folder) throws UploadException {

		String path = imageItem.getName();
		String filename = FilenameUtils.getName(path);
		String cacheUrl = getBusiness().getFolderBusiness()
				.getFolderPath(folder) + "/" + filename;
		getBusiness().getSystemService().getFileCache().remove(cacheUrl);
		String ext = FilenameUtils.getExtension(path);
		logger.debug("path " + path + " filename " + filename + " ext " + ext);
		FileEntity file = getDao().getFileDao().getByName(folder.getId(), 
				filename);
		if (file == null) {
			file = new FileEntity(filename, filename, folder.getId(),
				MimeType.getContentTypeByExt(ext), new Date(), data.length);
			logger.debug("created file " + file.getFilename());
		}
		getDao().getFileDao().save(file, data);
		return file;
	}

	private FolderEntity getFolder(final Long folderId)
			throws UploadException {
		
		logger.debug("getFolder " + folderId);

		if (folderId == null) {
			throw new UploadException(Messages.get("folder_is_empty"));
		}
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder == null) {
			throw new UploadException(Messages.get("folder.not_found", 
					folderId));
		}
		return folder;
	}

	private String processImportFile(FileItemStream fileItem, byte[] data) 
		throws UploadException {

		String ext = FolderUtil.getFileExt(fileItem.getName());
		if (!ext.toLowerCase().equals("zip") 
			&& !ext.toLowerCase().equals("vz")) {
			throw new UploadException(Messages.get("wrong_file_extension."));
		}
		getSystemService().getCache().putBlob(fileItem.getName(), data);
		getMessageQueue().publish(new ImportMessage.Builder()
				.setStart(1).setFilename(fileItem.getName()).create());
		return createMessage("success", Messages.get("saved_for_import"));
	}

	/**
	 * Process uploaded file from CKeditor upload tab.
	 * 
	 * @return Status string.
	 */
	private String processResourceFileCKeditor(FileItemStream imageItem, 
			byte[] data, Long pageId) {
		try {
			PageEntity page = getDao().getPageDao().getById(pageId);
			if (page == null) {
				throw new UploadException(Messages.get(
						"page.not_found", pageId));
			}
			FolderEntity folder;
			String folderPath = "/page" + page.getFriendlyURL();
			folder = getBusiness().getFolderBusiness().createFolder(folderPath);
			FileEntity file = processResourceFile(imageItem, data, folder);
			return "<script type=\"text/javascript\">"
				+ " window.parent.CKEDITOR.tools.callFunction("
				+ ckeditorFuncNum + ",'/file" 
				+ folderPath + "/" + file.getFilename() + "');"
				+ "</script>";
		}
		catch (UploadException e) {
			return "<script type=\"text/javascript\">"
				+ " window.parent.CKEDITOR.tools.callFunction("
				+ ckeditorFuncNum +",'','" + e.getMessage() + "');"
				+ "</script>";
		}
	}
	
	private boolean isCKeditorUpload(Map<String, String> parameters) {
		return !parameters.containsKey(FILE_TYPE_PARAM) && 
			parameters.containsKey(IMAGE_UPLOAD_PAGE_ID);
	}
	
	private String processPluginFile(FileItemStream fileItem, byte[] data)
			throws UploadException {
		String ext = FolderUtil.getFileExt(fileItem.getName());
		if (!ext.toLowerCase().equals("war")) {
			throw new UploadException(Messages.get("wrong_file_extension"));
		}
		try {
			getBusiness().getPluginBusiness().install(fileItem.getName(), data);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new UploadException(e.getMessage());
		}
		return createMessage("success", Messages.get("saved_for_import"));
	}

	private String processPicasaFile(FileItemStream fileItem, byte[] data,
			Map<String, String> parameters) throws UploadException {
		try {
			String albumId = parameters.get("albumId");
			if (StringUtils.isEmpty(albumId)) {
				throw new UploadException(Messages.get("album_not_found", 
						albumId));
			}
			getBusiness().getPicasaBusiness().upload(albumId, data, 
					fileItem.getName());
			return createMessage("success", Messages.get("photo_uploaded"));
		}
		catch (Exception e) {
			throw new UploadException(e.getMessage());
		}
	}

	
}