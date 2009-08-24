package org.vosao.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JSFUtil {

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
	
	public static Object getSessionObject(final String name) {
		return getRequest().getSession(true).getAttribute(name);
	}

	public static void setSessionObject(final String name, final Object data) {
		getRequest().getSession(true).setAttribute(name, data);
	}
	
}
