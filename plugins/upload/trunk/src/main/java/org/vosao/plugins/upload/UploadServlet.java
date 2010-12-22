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

package org.vosao.plugins.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.httpclient.util.ExceptionUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.helper.UserHelper;
import org.vosao.utils.StreamUtil;

public class UploadServlet extends HttpServlet {

	private static final String CHALLENGE = "challenge";
	private static final String VERIFY = "verify";
	private static final String FILENAME = "filename";
	private static final String DATA = "data";
	
	private static class Item {
		public String challenge;
		public String verify;
		public String filename;
		public byte[] byteData;
		
		public void setData(String value) throws UnsupportedEncodingException {
			if (value != null) {
				byteData = value.getBytes("UTF-8");
			}
		}
		
	}
	
	private static Log logger = LogFactory.getLog(UploadServlet.class);
	
	private static Dao getDao() {
		return getBusiness().getDao();
	}
	
	private static Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}

	private static String createMessage(String code, String msg) {
		return "{status:'" + code + "',message:'" + msg + "'}";
	}
	
	
	private UploadConfig uploadConfig;
	
	public UploadServlet(UploadConfig config) {
		super();
		uploadConfig = config;
	}
		
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		VosaoContext.getInstance().setUser(UserHelper.ADMIN);
		String code = "success";
		String message = "";
		try {
			message = processRequest(request);
		}
		catch (Throwable e) {
			code = "error";
			message = StringUtils.isEmpty(e.getMessage()) ? 
					ExceptionUtils.getStackTrace(e) : e.getMessage();
		}
		logger.info(message);
		response.getWriter().write(createMessage(code, message));
		response.setStatus(200);
	}
	
	private boolean isValidSignnature(Item item) {
		if (!uploadConfig.isUseKey()) {
			return true;
		}
		return DigestUtils.shaHex(item.challenge + uploadConfig.getSecretKey()
				+ DigestUtils.shaHex(item.byteData)).equals(item.verify);		
	}
	
	private void saveFile(Item item) throws IOException {
		logRequest(item);
		if (!isValidSignnature(item)) {
			throw new IOException("Invalid signature verification.");
		}
		getBusiness().getFileBusiness().saveFile(uploadConfig.getFolder() + "/" 
				+ item.filename, item.byteData);
	}
	
	private void logRequest(Item item) {
		logger.info("Upload request. filename: " + item.filename 
				+ ",\n challenge: " + item.challenge
				+ ",\n verify: " + item.verify);		
	}

	private String processRequest(HttpServletRequest request) 
			throws FileUploadException, IOException {
		Item item = null;
		if (ServletFileUpload.isMultipartContent(request)) {
			item = processMultipartPost(request);
		}
		else {
			item = processPost(request);
		}
		saveFile(item);
		return "Success";
	}
		
	private Item processPost(HttpServletRequest request) 
			throws IOException {
		Item result = new Item();
		result.filename = request.getParameter(FILENAME);
		result.challenge = request.getParameter(CHALLENGE);
		result.verify = request.getParameter(VERIFY);
		if (request.getParameter(DATA) == null) {
			throw new IOException("data field is empty");
		}
		result.setData(request.getParameter(DATA));
		return result;
	}	
	
	private Item processMultipartPost(HttpServletRequest request) 
			throws FileUploadException, IOException {
		Item result = new Item();
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(uploadConfig.getMaxSize());
		upload.setHeaderEncoding("UTF-8");
		FileItemIterator iter;
		iter = upload.getItemIterator(request);
		InputStream stream = null;
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			stream = item.openStream();
			if (item.isFormField()) {
				String value = Streams.asString(stream, "UTF-8");
				if (item.getFieldName().equals(FILENAME)) {
					result.filename = value;
				}
				if (item.getFieldName().equals(CHALLENGE)) {
					result.challenge = value;
				}
				if (item.getFieldName().equals(VERIFY)) {
					result.verify = value;
				}
			} else {
				result.byteData = StreamUtil.readFileStream(stream);
			}
		}
		return result;
	}
	
}
