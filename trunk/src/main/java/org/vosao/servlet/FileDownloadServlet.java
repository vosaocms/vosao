package org.vosao.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
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
		
		String[] folderChain = FolderUtil.getFolderChain(chain);
		String filename = chain[chain.length-1];
		
		TreeItemDecorator<FolderEntity> tree = getBusiness().getFolderBusiness()
				.getTree();
		FileEntity file = FolderUtil.getFile(tree, folderChain, filename);
		if (file != null) {
			getBusiness().getCache().put(request.getPathInfo(), file);
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
		response.setHeader("Content-type", file.getFile().getMimeType());
		response.setHeader("Content-Length", String.valueOf(
				file.getFile().getContent().length));
		BufferedOutputStream output = new BufferedOutputStream(
				response.getOutputStream());
		output.write(file.getFile().getContent());
		output.flush();
		output.close();
	}
	
}