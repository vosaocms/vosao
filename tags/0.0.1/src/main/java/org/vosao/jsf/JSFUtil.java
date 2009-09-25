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

package org.vosao.jsf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;

public class JSFUtil {

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	public static Object getSessionObject(final String name) {
		return getRequest().getSession(true).getAttribute(name);
	}

	public static void setSessionObject(final String name, final Object data) {
		getRequest().getSession(true).setAttribute(name, data);
	}
	
	public static void addErrorMessage(final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}
	
	public static void addInfoMessage(final String msg) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	
	public static void redirect(final String url) throws IOException {
		getResponse().sendRedirect(url);
	}
	
	public static String getTextResource(final String path) throws IOException {
		ClassPathResource res = new ClassPathResource(path);
		if (res == null) {
			throw new IOException("Empty resource.");
		}
		InputStream in = res.getInputStream();
		if (in == null) {
			throw new IOException("Empty input stream.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		while(reader.ready()) {
			buf.append(reader.readLine()).append("\n");
		}
		return buf.toString();
	}
	
	public static String getParameter(final String name) {
		return getRequest().getParameter(name);
	}

	public static void addErrorMessages(final List<String> msgList) {
		for (String msg : msgList) {
			addErrorMessage(msg);
		}
	}
	
}
