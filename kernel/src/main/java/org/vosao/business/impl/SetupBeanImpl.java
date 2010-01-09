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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.SetupBean;
import org.vosao.dao.Dao;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.UserEntity;
import org.vosao.enums.ContentPermissionType;
import org.vosao.enums.FolderPermissionType;
import org.vosao.enums.PageState;
import org.vosao.enums.UserRole;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class SetupBeanImpl implements SetupBean {

	private static Log log = LogFactory.getLog(SetupBeanImpl.class);

	public static final String VERSION = "0.2";

	private Dao dao;
	private Business business;
	
	private GroupEntity guests;
	
	public void setup() {
		log.info("setup...");
		clearSessions();
		try {
			clearCache();
		} catch (CacheException e) {
			log.error(e);
		}
		initGroups();
		initUsers();
		initTemplates();
		initFolders();
		initPages();
		initConfigs();
		initForms();
		initLanguages();
	}
	
	private void initLanguages() {
		LanguageEntity lang = getDao().getLanguageDao().getByCode(
				LanguageEntity.ENGLISH_CODE); 
		if (lang == null) {
			lang = new LanguageEntity(
				LanguageEntity.ENGLISH_CODE, LanguageEntity.ENGLISH_TITLE);
			getDao().getLanguageDao().save(lang);
		}
	}

	private void clearSessions() {
        DatastoreService datastore = DatastoreServiceFactory
        		.getDatastoreService();
        Query query = new Query("_ah_SESSION");
        PreparedQuery results = datastore.prepare(query);
        log.info("Deleting " + results.countEntities() + " sessions from data store");
        for (Entity session : results.asIterable()) {
            datastore.delete(session.getKey());
        }
	} 
	
	private void clearCache() throws CacheException {
        CacheFactory cacheFactory = CacheManager.getInstance()
        		.getCacheFactory();
        Cache cache = cacheFactory.createCache(Collections.emptyMap());
        CacheStatistics stats = cache.getCacheStatistics();
        log.info("Clearing " + stats.getObjectCount() + " objects in cache");
        cache.clear();
	} 

	private void initUsers() {
		List<UserEntity> admins = getDao().getUserDao().getByRole(UserRole.ADMIN);
		if (admins.size() == 0) {
			UserEntity admin = new UserEntity("admin", "admin", "admin@test.com", 
					UserRole.ADMIN);
			getDao().getUserDao().save(admin);
	        log.info("Adding admin user admin@test.com.");
		}
	}

	private void initGroups() {
		guests = getDao().getGroupDao().getByName("guests");
		if (guests == null) {
			guests = new GroupEntity("guests");
			getDao().getGroupDao().save(guests);
		}
	}

	private void initPages() {
		List<PageEntity> roots = getDao().getPageDao().getByParent("");
		if (roots.size() == 0) {
			String content = loadResource("org/vosao/resources/html/root.html");
			TemplateEntity template = getDao().getTemplateDao().getByUrl("simple");
			PageEntity root = new PageEntity("root", "/", template.getId());
			root.setCreateUserEmail("admin@test.com");
			root.setModUserEmail("admin@test.com");
			root.setState(PageState.APPROVED);
			getDao().getPageDao().save(root);
			getDao().getPageDao().setContent(root.getId(), 
					LanguageEntity.ENGLISH_CODE, content);
			getBusiness().getContentPermissionBusiness().setPermission(
					"/", guests, ContentPermissionType.READ);
	        log.info("Adding root page.");
			content = loadResource("org/vosao/resources/html/login.html");
			PageEntity login = new PageEntity("Site user Login", "/login", 
					template.getId());
			login.setCreateUserEmail("admin@test.com");
			login.setModUserEmail("admin@test.com");
			login.setState(PageState.APPROVED);
			getDao().getPageDao().save(login);
			getDao().getPageDao().setContent(login.getId(), 
					LanguageEntity.ENGLISH_CODE, content);
	        log.info("Adding login page.");
		}
	}

	private void initTemplates() {
		List<TemplateEntity> list = getDao().getTemplateDao().select();
		if (list.size() == 0) {
			String content = loadResource(
					"org/vosao/resources/html/simple.html");
			TemplateEntity template = new TemplateEntity("Simple", content, 
					"simple");
			getDao().getTemplateDao().save(template);
	        log.info("Adding default template.");
		}
	}

	private void initFolders() {
		List<FolderEntity> roots = getDao().getFolderDao().getByParent(null);
		if (roots.size() == 0) {
	        log.info("Adding default folders.");
			FolderEntity root = new FolderEntity("/", null);
			getDao().getFolderDao().save(root);
			FolderEntity theme = new FolderEntity("Themes resources", "theme", root.getId());
			getDao().getFolderDao().save(theme);
			FolderEntity simple = new FolderEntity("Simple", "simple", theme.getId());
			getDao().getFolderDao().save(simple);
			getBusiness().getFolderPermissionBusiness().setPermission(
					root, guests, FolderPermissionType.READ);
		}
	}
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public static final String COMMENTS_TEMPLATE_FILE = 
			"org/vosao/resources/html/comments.html";
	
	private void initConfigs() {
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		if (config.getId() == null || config.getId() == 0) {
	        config.setVersion(VERSION);
			config.setGoogleAnalyticsId("");
	        config.setSiteEmail("");
	        config.setSiteDomain("");
	        config.setEditExt("css,js,xml");
	        config.setSiteUserLoginUrl("/login");
	        config.setCommentsTemplate(loadResource(
	        		COMMENTS_TEMPLATE_FILE));
	        getDao().getConfigDao().save(config);
		}
	}
	
	private String loadResource(final String url) {
		try {
			return StreamUtil.getTextResource(url);
		}
		catch(IOException e) {
			log.error("Can't read comments template." + e);
			return "Error during load resources " + url;
		}
	}
	
	public static final String FORM_TEMPLATE_FILE =
		"org/vosao/resources/html/form-template.html";
	public static final String FORM_LETTER_FILE =
		"org/vosao/resources/html/form-letter.html";
	
	private void initForms() {
		FormConfigEntity config = getDao().getFormDao().getConfig();
		if (config.getId() == null) {
			config.setFormTemplate(loadResource(
					FORM_TEMPLATE_FILE));
			config.setLetterTemplate(loadResource(
					FORM_LETTER_FILE));
			getDao().getFormDao().save(config);			
		}
	}

	@Override
	public void clear() {
		getDao().getCommentDao().removeAll();
		getDao().getConfigDao().removeAll();
		getDao().getContentDao().removeAll();
		getDao().getContentPermissionDao().removeAll();
		getDao().getFieldDao().removeAll();
		getDao().getFileDao().removeAll();
		getDao().getFolderDao().removeAll();
		getDao().getFolderPermissionDao().removeAll();
		getDao().getFormDao().removeAll();
		getDao().getGroupDao().removeAll();
		getDao().getLanguageDao().removeAll();
		getDao().getMessageDao().removeAll();
		getDao().getPageDao().removeAll();
		getDao().getSeoUrlDao().removeAll();
		getDao().getStructureDao().removeAll();
		getDao().getStructureTemplateDao().removeAll();
		getDao().getTemplateDao().removeAll();
		getDao().getUserDao().removeAll();
		getDao().getUserGroupDao().removeAll();
	}
	
}
