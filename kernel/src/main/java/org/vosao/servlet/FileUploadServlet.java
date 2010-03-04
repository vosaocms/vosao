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

package org.vosao.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.vosao.business.ImportExportBusiness;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.MimeType;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.labs.taskqueue.Queue;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

/**
 * Servlet for uploading images into database.
 * 
 * @author Aleksandr Oleynik
 */
public class FileUploadServlet extends BaseSpringServlet {
	private static final long serialVersionUID = 6098745782027999297L;

	private static final long MAX_SIZE = 10000000;

	private static final String FOLDER_PARAM = "folderId";
	
	private static final String FILE_TYPE_PARAM = "fileType";
	private static final String FILE_TYPE_RESOURCE = "resource";
	private static final String FILE_TYPE_IMPORT = "import";
	private static final String FILE_TYPE_PLUGIN = "plugin";

	private static final String TEXT_MESSAGE = "::%s::%s::";

	private static final String FOLDER_NOT_FOUND = "Folder not found";
	private static final String FOLDER_ID_IS_NULL = "Folder id is null";
	private static final String PARSE_REQUEST_ERROR = "Parse request error";

	public static final String IMAGE_UPLOAD_PAGE_ID = "imageUploadPageId";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(true);
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(MAX_SIZE);
		upload.setHeaderEncoding("UTF-8");
		String message = null;
		Map<String, String> parameters = new HashMap<String, String>();
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
						(String)session.getAttribute(
								IMAGE_UPLOAD_PAGE_ID));
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (item.isFormField()) {
						parameters.put(item.getFieldName(), 
								Streams.asString(stream, "UTF-8"));
					} else {
						imageFileItem = item;
						fileData = StreamUtil.readFileStream(stream);
					}
				}
				message = processFile(imageFileItem, fileData, parameters);
			} catch (FileUploadException e) {
				logger.error(PARSE_REQUEST_ERROR);
				throw new UploadException(PARSE_REQUEST_ERROR);
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
				throw new UploadException("File type was not specified");
			}
			else {
				return processResourceFileCKeditor(fileItem, data, 
						Long.valueOf(parameters.get(IMAGE_UPLOAD_PAGE_ID)));
			}
		}
		String fileType = parameters.get(FILE_TYPE_PARAM);
		if (fileType.equals(FILE_TYPE_RESOURCE)) {
			if (!parameters.containsKey(FOLDER_PARAM)) {
				throw new UploadException("Folder parameter was not specified");
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
		getBusiness().getSystemService().getCache().remove(cacheUrl);
		logger.debug("Clear cache " + cacheUrl);
		String ext = FilenameUtils.getExtension(path);
		logger.debug("path " + path + " filename " + filename + " ext " + ext);
		String message = null;
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
			throw new UploadException(FOLDER_ID_IS_NULL);
		}
		FolderEntity folder = getDao().getFolderDao().getById(folderId);
		if (folder == null) {
			throw new UploadException(FOLDER_NOT_FOUND);
		}
		return folder;
	}

	private String processImportFile(FileItemStream fileItem, byte[] data) 
		throws UploadException {

		String ext = FolderUtil.getFileExt(fileItem.getName());
		if (!ext.toLowerCase().equals("zip")) {
			throw new UploadException("Wrong file extension.");
		}
		FolderEntity folder;
		try {
			folder = getBusiness().getFolderBusiness().createFolder(
					"/tmp");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new UploadException(e.getMessage());
		}
		FileEntity file = new FileEntity(fileItem.getName(), fileItem.getName(),
				folder.getId(), fileItem.getContentType(), new Date(),
				data.length);
		getDao().getFileDao().save(file, data);
		Queue queue = getSystemService().getQueue("import");
		queue.add(url(ImportTaskServlet.IMPORT_TASK_URL).param("start", "1")
				.param("filename", file.getFilename()));
		return createMessage("success", "Saved for import.");
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
				throw new UploadException("Page not found id = " + pageId);
			}
			FolderEntity folder;
			String folderPath = "/page" + page.getFriendlyURL();
			try {
				folder = getBusiness().getFolderBusiness().createFolder(folderPath);
			} catch (UnsupportedEncodingException e) {
				throw new UploadException("Can't create folder for path " 
					+ page.getFriendlyURL() + " " + e.getMessage());
			}
			FileEntity file = processResourceFile(imageItem, data, folder);
			return "<script type=\"text/javascript\">"
				+ " window.parent.CKEDITOR.tools.callFunction(1,"
				+ "'/file" + folderPath + "/" + file.getFilename() + "');"
				+ "</script>";
		}
		catch (UploadException e) {
			return "<script type=\"text/javascript\">"
				+ " window.parent.CKEDITOR.tools.callFunction(1,'',"
				+ "'" + e.getMessage() + "');"
				+ "</script>";
		}
	}
	
	private boolean isCKeditorUpload(Map<String, String> parameters) {
		return !parameters.containsKey(FILE_TYPE_PARAM) && 
			parameters.containsKey(IMAGE_UPLOAD_PAGE_ID);
	}
	
	private ImportExportBusiness getImportExportBusiness() {
		return (ImportExportBusiness) getSpringBean("importExportBusiness");
	}
	
	private String processPluginFile(FileItemStream fileItem, byte[] data)
			throws UploadException {
		String ext = FolderUtil.getFileExt(fileItem.getName());
		if (!ext.toLowerCase().equals("war")) {
			throw new UploadException("Wrong file extension.");
		}
		try {
			getBusiness().getPluginBusiness().install(fileItem.getName(), data);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new UploadException(e.getMessage());
		}
		return createMessage("success", "Saved for import.");
	}

}