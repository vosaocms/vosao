package org.vosao.business.impl;

import java.util.Collections;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.FolderBusiness;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.TemplateBusiness;
import org.vosao.business.UserPreferences;
import org.vosao.jsf.JSFUtil;
import org.vosao.servlet.FileDownloadServlet;

public class BusinessImpl implements Business {

	private static final Log log = LogFactory.getLog(BusinessImpl.class);

	private boolean initialized;
	private Cache cache;
	
	private PageBusiness pageBusiness;
	private FolderBusiness folderBusiness;
	private TemplateBusiness templateBusiness;
	private ImportExportBusiness importExportBusiness;

	public void init() {
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(
            		Collections.emptyMap());
        } catch (CacheException e) {
            log.error("Can't init cache manager.");
        }
	}
	
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

	
	public PageBusiness getPageBusiness() {
		return pageBusiness;
	}

	public void setPageBusiness(PageBusiness bean) {
		pageBusiness = bean;
	}
	
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}

	public TemplateBusiness getTemplateBusiness() {
		return templateBusiness;
	}

	public void setTemplateBusiness(TemplateBusiness bean) {
		templateBusiness = bean;
	}

	public ImportExportBusiness getImportExportBusiness() {
		return importExportBusiness;
	}

	public void setImportExportBusiness(ImportExportBusiness bean) {
		importExportBusiness = bean;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}


	public Cache getCache() {
		return cache;
	}


	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
}
