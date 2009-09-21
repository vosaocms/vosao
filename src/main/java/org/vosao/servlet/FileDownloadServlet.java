package org.vosao.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

/**
 * Servlet for download files from database.
 * 
 * @author Aleksandr Oleynik
 */
public class FileDownloadServlet extends BaseSpringServlet {
	
	private static final long CACHE_LIMIT = 1048576;
	
	private static final long serialVersionUID = 6098745782027999297L;
	private static final Log log = LogFactory.getLog(FileDownloadServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        log.info("get file " + request.getPathInfo());
		String[] chain = FolderUtil.getPathChain(request.getPathInfo());
		if (chain.length == 0) {
			response.getWriter().append("file was not specified");
			return;
		}
		
		if (isInCache(request.getPathInfo())) {
			sendFromCache(request, response);
			return;
		}
		
		String filename = chain[chain.length-1];
		
		TreeItemDecorator<FolderEntity> tree = getBusiness().getFolderBusiness()
				.getTree();
		TreeItemDecorator<FolderEntity> folder = getBusiness().getFolderBusiness()
				.findFolderByPath(tree, FolderUtil.getFilePath(
						request.getPathInfo()));
		if (folder == null) {
	        log.info("folder " + request.getPathInfo() + " was not found");
			response.getWriter().append("folder " + request.getPathInfo() 
					+ " was not found");
			return;
		}
		FileEntity file = getDao().getFileDao().getByName(
				folder.getEntity().getId(), filename); 
		if (file != null) {
			if (file.getSize() < CACHE_LIMIT) {
				getBusiness().getCache().put(request.getPathInfo(), file);
			}
			sendFile(file, response);
		}
		else {
	        log.info("file " + request.getPathInfo() + " was not found");
			response.getWriter().append("file " + request.getPathInfo() 
					+ " was not found");
		}
	}
	
	private boolean isInCache(final String path) {
		return getBusiness().getCache().containsKey(path);
	}
	
	private void sendFromCache(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
        log.info("taking from memcache " + request.getPathInfo());
		FileEntity file = (FileEntity) getBusiness().getCache().get(
				request.getPathInfo());
		sendFile(file, response);
	}
	
	private void sendFile(final FileEntity file, HttpServletResponse response) 
			throws IOException {
		response.setHeader("Content-type", file.getMimeType());
		response.setHeader("Content-Length", String.valueOf(file.getSize()));
		BufferedOutputStream output = new BufferedOutputStream(
				response.getOutputStream());
		output.write(getDao().getFileDao().getFileContent(file));
		output.flush();
		output.close();
	}
	
}