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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.i18n.Messages;
import org.vosao.utils.DateUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class JSBundleServlet extends AbstractServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		byte[] content = Messages.getJSMessages().getBytes("UTF-8");
		response.setHeader("Content-type", "text/javascript; charset=\"utf-8\""); 
		response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
    	response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Length", String.valueOf(content.length));
		response.setHeader("Last-Modified", 
				DateUtil.toHeaderString(new Date()));
		BufferedOutputStream output = new BufferedOutputStream(
				response.getOutputStream());
		output.write(content);
		output.flush();
		output.close();
	}
	
}