package org.vosao.business.impl;

import org.vosao.business.Business;
import org.vosao.business.UserPreferences;

public class BusinessImpl implements Business {

	private UserPreferences userPreferences;
	
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(UserPreferences aUserPreferences) {
		userPreferences = aUserPreferences;
	}

}
