package org.vosao.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

/**
 * Servlet for uploading images into database.
 * 
 * @author Aleksandr Oleynik
 */
public class FileUploadServlet extends BaseSpringServlet {
	private static final long serialVersionUID = 6098745782027999297L;
	private static final Log log = LogFactory
			.getLog(FileUploadServlet.class);

	private static final long MAX_SIZE = 5000000;

	private static final String FOLDER_PARAM = "folderId";
	
	private static final String FILE_TYPE_PARAM = "fileType";
	private static final String FILE_TYPE_RESOURCE = "resource";
	private static final String FILE_TYPE_IMPORT = "import";

	private static final String SUCCESS_MESSAGE = "{success:'%s'}";
	private static final String FOLDER_NOT_FOUND = "Folder not found";
	private static final String FOLDER_ID_IS_NULL = "Folder id is null";
	private static final String PARSE_REQUEST_ERROR = "Parse request error";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(MAX_SIZE);
		upload.setHeaderEncoding("UTF-8");
		String json = null;
		try {
			FileItemIterator iter;
			try {
				iter = upload.getItemIterator(request);
				FileItemStream imageFileItem = null;
				String folder = null;
				InputStream stream = null;
				InputStream filestream = null;
				byte[] fileData = null;
				Map<String, String> parameters = new HashMap<String, String>();
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (item.isFormField()) {
						parameters.put(item.getFieldName(), 
								Streams.asString(stream, "UTF-8"));
					} else {
						imageFileItem = item;
						fileData = readFileStream(stream);
					}
				}
				json = processFile(imageFileItem, fileData, parameters);
			} catch (FileUploadException e) {
				log.error(PARSE_REQUEST_ERROR);
				throw new UploadException(PARSE_REQUEST_ERROR);
			}
		} catch (UploadException e) {
			json = "{error:'" + e.getMessage() + "'}";
			log.error(json);
		}
		response.setContentType("text/plain");
		response.setStatus(200);
		response.getWriter().write(json);
		// log.info(json);
	}

	private String processFile(FileItemStream fileItem, byte[] data, 
			Map<String, String> parameters) throws UploadException {
		
		if (!parameters.containsKey(FILE_TYPE_PARAM)) {
			throw new UploadException("File type was not specified");
		}
		String fileType = parameters.get(FILE_TYPE_PARAM);
		if (fileType.equals(FILE_TYPE_RESOURCE)) {
			if (!parameters.containsKey(FOLDER_PARAM)) {
				throw new UploadException("Folder parameter was not specified");
			}
			return processResourceFile(fileItem, data, 
					getFolder(parameters.get(FOLDER_PARAM)));
		}
		if (fileType.equals(FILE_TYPE_IMPORT)) {
			return processImportFile(fileItem, data);
		}
		return null;
	}
	
	
	/**
	 * Process uploaded file.
	 * 
	 * @param request
	 *            - Http reauest.
	 * @param item
	 *            - mulitpart item.
	 * @return Status string.
	 */
	private String processResourceFile(FileItemStream imageItem, byte[] data, 
			FolderEntity folder) throws UploadException {

		String path = imageItem.getName();
		String filename = FilenameUtils.getName(path);
		String ext = FilenameUtils.getExtension(path);
		log.info("path " + path + " filename " + filename + " ext " + ext);
		String message = null;
		FileEntity file = new FileEntity(filename, filename, 
				MimeType.getContentTypeByExt(ext), data, folder);
		getDao().getFileDao().save(file);
		log.info("created fileEntity id=" + file.getId());
		message = String.format(SUCCESS_MESSAGE, file.getId());
		return message;
	}

	private byte[] readFileStream(final InputStream stream) throws IOException {
		byte[] buffer = new byte[4096];
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		long contentLength = 0;
		int n = 0;
		while (-1 != (n = stream.read(buffer))) {
			contentLength += n;
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
	
	private FolderEntity getFolder(final String folderId)
			throws UploadException {
		
		log.info("getFolder " + folderId);

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

		log.info("Process import file filename " + fileItem.getName());
		String message = null;
		ByteArrayInputStream inputData = new ByteArrayInputStream(data);
		try {
			ZipInputStream in = new ZipInputStream(inputData);
			getBusiness().getImportExportBusiness().importThemes(in);
			message = String.format(SUCCESS_MESSAGE, "Imported.");
			in.close();
		}
		catch (IOException e) {
			throw new UploadException(e.getMessage());
		}
		catch (DocumentException e) {
			throw new UploadException(e.getMessage());
		}
		return message;
	}

	
}