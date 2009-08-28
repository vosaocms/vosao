package org.vosao.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	}
	
}