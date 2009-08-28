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
		String[] folderChain = FolderUtil.getFolderChain(chain);
		String filename = chain[chain.length-1];
		
		TreeItemDecorator<FolderEntity> tree = getBusiness().getFolderBusiness()
				.getTree();
		FileEntity file = FolderUtil.getFile(tree, folderChain, filename);
		if (file != null) {
			response.setHeader("Content-type", file.getFile().getMimeType());
			response.setHeader("Content-Length", String.valueOf(
					file.getFile().getContent().length));
			BufferedOutputStream output = new BufferedOutputStream(
					response.getOutputStream());
			output.write(file.getFile().getContent());
		}
		else {
			response.getWriter().append("file " + request.getPathInfo() 
					+ " was not found");
		}
	}
	
	
}