package org.vosao.business;

import javax.servlet.http.HttpServletRequest;


public interface Business {
	
	UserPreferences getUserPreferences();
	UserPreferences getUserPreferences(final HttpServletRequest request);
	void setUserPreferences(final UserPreferences userPreferences);

	PageBusiness getPageBusiness();
	void setPageBusiness(final PageBusiness bean);
	
}
