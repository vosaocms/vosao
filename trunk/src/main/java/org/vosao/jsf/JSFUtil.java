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
			buf.append(reader.readLine());
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
