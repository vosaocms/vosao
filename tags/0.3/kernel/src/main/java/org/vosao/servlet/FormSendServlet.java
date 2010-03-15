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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.UploadException;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.utils.FileItem;
import org.vosao.utils.RecaptchaUtil;
import org.vosao.utils.StreamUtil;

/**
 * Servlet for form submition in forms plugin.
 * 
 * @author Aleksandr Oleynik 
 */
public class FormSendServlet extends BaseSpringServlet {
	
	private static final long serialVersionUID = 1L;

	private static final long MAX_SIZE = 10000000;
	private static final String TEXT_MESSAGE = "{\"result\":\"%s\", \"message\":\"%s\"}";
	private static final String PARSE_REQUEST_ERROR = "Parse request error";
	private static final String FORM_NAME_PARAM = "form-name";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String message = null;
		Map<String, String> parameters = new HashMap<String, String>();
		List<FileItem> files = new ArrayList<FileItem>();
		try {
			if (request.getContentType().startsWith("multipart/form-data")) {
				ServletFileUpload upload = new ServletFileUpload();
				upload.setFileSizeMax(MAX_SIZE);
				upload.setHeaderEncoding("UTF-8");
				FileItemIterator iter;
				try {
					iter = upload.getItemIterator(request);
					InputStream stream = null;
					while (iter.hasNext()) {
						FileItemStream item = iter.next();
						stream = item.openStream();
						if (item.isFormField()) {
							parameters.put(item.getFieldName(), 
								Streams.asString(stream, "UTF-8"));
						} else {
							files.add(new FileItem(item, 
								StreamUtil.readFileStream(stream)));
						}
					}
				} catch (FileUploadException e) {
					logger.error(e.getMessage());
					throw new UploadException(PARSE_REQUEST_ERROR);
				}
			}
			else {
				for (Object key : request.getParameterMap().keySet()) {
					String paramName = (String)key;
					parameters.put(paramName, request.getParameter(paramName));
				}
			}
			message = processForm(parameters, files, request);
		} catch (UploadException e) {
			message = createMessage("error", e.getMessage()); 
			logger.error(message);
		}
		catch (Exception e) {
			message = createMessage("error", e.getMessage()); 
			logger.error(message);
		}
		response.setContentType("text/html");
		response.setStatus(200);
		response.getWriter().write(message);
	}

	private String createMessage(final String result, final String message) {
		return String.format(TEXT_MESSAGE, result, message);
	}
	
	private String processForm(Map<String, String> parameters,
			List<FileItem> files, HttpServletRequest request) 
			throws UploadException {
		logger.info("parameters " + parameters.toString());
		String formName = parameters.get(FORM_NAME_PARAM);
		if (formName == null) {
			throw new UploadException("Form name parameter was not found.");
		}
		FormEntity form = getDao().getFormDao().getByName(formName);
		if (form == null) {
			throw new UploadException("Form with name " + formName 
					+ " was not found.");
		}
		ConfigEntity config = getDao().getConfigDao().getConfig();
		String challenge = parameters.get("recaptcha_challenge_field");
		String response = parameters.get("recaptcha_response_field");
		if (form.isEnableCaptcha() && config.isEnableRecaptcha()) {
			ReCaptchaResponse recaptchaResponse = RecaptchaUtil.check(
					config.getRecaptchaPublicKey(), 
					config.getRecaptchaPrivateKey(), 
					challenge, response, request);
			if (!recaptchaResponse.isValid()) {
				return createMessage("error", 
						"Incorrect captcha solution.");
			}
		}
		getBusiness().getFormBusiness().submit(form, parameters, files);
		return createMessage("success", "Form was successfully submited.");
	}
	
}