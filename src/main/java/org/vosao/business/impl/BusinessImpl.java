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
import org.vosao.business.CommentBusiness;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.FieldBusiness;
import org.vosao.business.FileBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FormBusiness;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.TemplateBusiness;
import org.vosao.business.UserPreferences;
import org.vosao.jsf.JSFUtil;

public class BusinessImpl implements Business {

	private static final Log log = LogFactory.getLog(BusinessImpl.class);

	private Cache cache;
	
	private PageBusiness pageBusiness;
	private FolderBusiness folderBusiness;
	private TemplateBusiness templateBusiness;
	private ImportExportBusiness importExportBusiness;
	private ConfigBusiness configBusiness;
	private FormBusiness formBusiness;
	private FileBusiness fileBusiness;
	private CommentBusiness commentBusiness;
	private FieldBusiness fieldBusiness;

	public void init() {
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(
            		Collections.emptyMap());
        } catch (CacheException e) {
            log.error("Can't init cache manager.");
        }
	}
	
	@Override
	public UserPreferences getUserPreferences() {
		String name = UserPreferences.class.getName();
		if (JSFUtil.getSessionObject(name) == null) {
			JSFUtil.setSessionObject(name, new UserPreferences());
		}
		return (UserPreferences)JSFUtil.getSessionObject(name);
	}

	@Override
	public void setUserPreferences(UserPreferences aUserPreferences) {
		JSFUtil.setSessionObject(UserPreferences.class.getName(), 
				aUserPreferences);
	}

	@Override
	public UserPreferences getUserPreferences(final HttpServletRequest request) {
		String name = UserPreferences.class.getName();
		HttpSession session = request.getSession(true);
		if (session.getAttribute(name) == null) {
			session.setAttribute(name, new UserPreferences());
		}
		return (UserPreferences)session.getAttribute(name);
	}

	
	@Override
	public PageBusiness getPageBusiness() {
		return pageBusiness;
	}

	@Override
	public void setPageBusiness(PageBusiness bean) {
		pageBusiness = bean;
	}
	
	@Override
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}

	@Override
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}

	@Override
	public TemplateBusiness getTemplateBusiness() {
		return templateBusiness;
	}

	@Override
	public void setTemplateBusiness(TemplateBusiness bean) {
		templateBusiness = bean;
	}

	@Override
	public ImportExportBusiness getImportExportBusiness() {
		return importExportBusiness;
	}

	@Override
	public void setImportExportBusiness(ImportExportBusiness bean) {
		importExportBusiness = bean;
	}

	@Override
	public Cache getCache() {
		return cache;
	}

	@Override
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	@Override
	public ConfigBusiness getConfigBusiness() {
		return configBusiness;
	}
	@Override
	public void setConfigBusiness(ConfigBusiness bean) {
		configBusiness = bean;
	}

	@Override
	public FormBusiness getFormBusiness() {
		return formBusiness;
	}

	@Override
	public void setFormBusiness(FormBusiness formBusiness) {
		this.formBusiness = formBusiness;
	}

	@Override
	public FileBusiness getFileBusiness() {
		return fileBusiness;
	}

	@Override
	public void setFileBusiness(FileBusiness bean) {
		fileBusiness = bean;		
	}

	@Override
	public CommentBusiness getCommentBusiness() {
		return commentBusiness;
	}

	@Override
	public void setCommentBusiness(CommentBusiness bean) {
		commentBusiness = bean;
	}

	@Override
	public FieldBusiness getFieldBusiness() {
		return fieldBusiness;
	}

	@Override
	public void setFieldBusiness(FieldBusiness bean) {
		fieldBusiness = bean;
	}
	
	
}
