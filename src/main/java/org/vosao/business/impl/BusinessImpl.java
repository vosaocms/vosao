package org.vosao.business.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vosao.business.Business;
import org.vosao.business.UserPreferences;
import org.vosao.jsf.JSFUtil;

public class BusinessImpl implements Business {

	/*private UserPreferences userPreferences;
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences aUserPreferences) {
		userPreferences = aUserPreferences;
	}*/
	
	
	public UserPreferences getUserPreferences() {
		String name = UserPreferences.class.getName();
		if (JSFUtil.getSessionObject(name) == null) {
			JSFUtil.setSessionObject(name, new UserPreferences());
		}
		return (UserPreferences)JSFUtil.getSessionObject(name);
	}

	public void setUserPreferences(UserPreferences aUserPreferences) {
		JSFUtil.setSessionObject(UserPreferences.class.getName(), 
				aUserPreferences);
	}

	public UserPreferences getUserPreferences(final HttpServletRequest request) {
		String name = UserPreferences.class.getName();
		HttpSession session = request.getSession(true);
		if (session.getAttribute(name) == null) {
			session.setAttribute(name, new UserPreferences());
		}
		return (UserPreferences)session.getAttribute(name);
	}
	
}
