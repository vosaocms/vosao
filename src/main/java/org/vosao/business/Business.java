package org.vosao.business;

import javax.cache.Cache;
import javax.servlet.http.HttpServletRequest;


public interface Business {
	
	boolean isInitialized();
	void setInitialized(boolean value);
	
	UserPreferences getUserPreferences();
	UserPreferences getUserPreferences(final HttpServletRequest request);
	void setUserPreferences(final UserPreferences userPreferences);

	PageBusiness getPageBusiness();
	void setPageBusiness(final PageBusiness bean);

	FolderBusiness getFolderBusiness();
	void setFolderBusiness(final FolderBusiness bean);
	
	TemplateBusiness getTemplateBusiness();
	void setTemplateBusiness(final TemplateBusiness bean);

	ImportExportBusiness getImportExportBusiness();
	void setImportExportBusiness(final ImportExportBusiness bean);
	
	Cache getCache();
	void setCache(Cache cache);

	ConfigBusiness getConfigBusiness();
	void setConfigBusiness(final ConfigBusiness bean);

	FormBusiness getFormBusiness();
	void setFormBusiness(final FormBusiness bean);
	
}
