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

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.SetupBean;
import org.vosao.common.BCrypt;
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
import org.vosao.servlet.SessionCleanTaskServlet;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.labs.taskqueue.Queue;

public class SetupBeanImpl implements SetupBean {

	private static Log log = LogFactory.getLog(SetupBeanImpl.class);

	public static final String VERSION = "0.4";
	public static final String FULLVERSION = "0.4";

	private Dao dao;
	private Business business;
	
	private GroupEntity guests;
	
	public void setup() {
		log.info("setup...");
		clearSessions();
		clearCache();
		initGroups();
		initUsers();
		initTemplates();
		initFolders();
		initPages();
		initConfigs();
		initForms();
		initLanguages();
		getBusiness().getSearchEngine().reindexInRequest();
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
		Queue queue = getBusiness().getSystemService().getQueue("session-clean");
		queue.add(url(SessionCleanTaskServlet.SESSION_CLEAN_TASK_URL));
	} 
	
	private void clearCache() {
		business.getSystemService().getCache().clear();
	} 

	private void initUsers() {
		List<UserEntity> admins = getDao().getUserDao().getByRole(UserRole.ADMIN);
		if (admins.size() == 0) {
			UserEntity admin = new UserEntity("admin", 
					BCrypt.hashpw("admin", BCrypt.gensalt()), 
					"admin@test.com", 
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

	public static final String HOME_PAGE_FILE = 
		"org/vosao/resources/html/root.html";

	public static final String LOGIN_PAGE_FILE = 
		"org/vosao/resources/html/login.html";
	
	public static final String SEARCH_PAGE_FILE = 
		"org/vosao/resources/html/search.html";
	
	private void initPages() {
		List<PageEntity> roots = getDao().getPageDao().getByParent("");
		if (roots.size() == 0) {
			TemplateEntity template = getDao().getTemplateDao().getByUrl("simple");
			addPage("Home page", "/", HOME_PAGE_FILE, template.getId(), 0);
			getBusiness().getContentPermissionBusiness().setPermission(
					"/", guests, ContentPermissionType.READ);
	        addPage("Site user Login", "/login", LOGIN_PAGE_FILE, 
	        		template.getId(), 0);
	        addPage("Search", "/search", SEARCH_PAGE_FILE, template.getId(), 1);
		}
	}

	private void addPage(String title, String url, String resource, 
				Long templateId, Integer sortIndex) {
        String content = loadResource(resource);
		PageEntity login = new PageEntity(title, url,	templateId);
		login.setCreateUserEmail("admin@test.com");
		login.setModUserEmail("admin@test.com");
		login.setState(PageState.APPROVED);
		login.setSortIndex(sortIndex);
		getDao().getPageDao().save(login);
		getDao().getPageDao().setContent(login.getId(), 
				LanguageEntity.ENGLISH_CODE, content);
        log.info("Added " + title);
	}
	
	public static final String SIMPLE_TEMPLATE_FILE = 
			"org/vosao/resources/html/simple.html";
	
	private void initTemplates() {
		List<TemplateEntity> list = getDao().getTemplateDao().select();
		if (list.size() == 0) {
			String content = loadResource(SIMPLE_TEMPLATE_FILE);
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
			FolderEntity root = new FolderEntity("file", "/", null);
			getDao().getFolderDao().save(root);
			FolderEntity theme = new FolderEntity("Themes resources", "theme", root.getId());
			getDao().getFolderDao().save(theme);
			FolderEntity simple = new FolderEntity("Simple", "simple", theme.getId());
			getDao().getFolderDao().save(simple);
			getBusiness().getFolderPermissionBusiness().setPermission(
					root, guests, FolderPermissionType.READ);
			FolderEntity tmp = new FolderEntity("tmp", "tmp", root.getId());
			getDao().getFolderDao().save(tmp);
			getBusiness().getFolderPermissionBusiness().setPermission(
					tmp, guests, FolderPermissionType.WRITE);
			FolderEntity page = new FolderEntity("page", "page", root.getId());
			getDao().getFolderDao().save(page);
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
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		if (config.getId() == null) {
			config.setFormTemplate(loadResource(
					FORM_TEMPLATE_FILE));
			config.setLetterTemplate(loadResource(
					FORM_LETTER_FILE));
			getDao().getFormConfigDao().save(config);			
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
		getDao().getPluginDao().removeAll();
		getDao().getPluginResourceDao().removeAll();
		clearCache();
	}
	
}
