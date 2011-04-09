/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.SetupBean;
import org.vosao.business.mq.MessageQueue;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.ImportMessage;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.BCrypt;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FileEntity;
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

public class SetupBeanImpl implements SetupBean {

	private static Log log = LogFactory.getLog(SetupBeanImpl.class);

	private static final String DEFAULT_SITE = "default.vz";
	
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

	@Override
	public void clearSessions() {
		getMessageQueue().publish(new SimpleMessage(
				Topic.SESSION_CLEAN.name(), "start"));
	} 

	private void clearCache() {
		getBusiness().getSystemService().getCache().clear();
	} 

	private void initUsers() {
		List<UserEntity> admins = getDao().getUserDao().getByRole(UserRole.ADMIN);
		if (admins.size() == 0) {
			UserEntity admin = new UserEntity("${vosao.admin.username}", 
					BCrypt.hashpw("${vosao.admin.password}", BCrypt.gensalt()), 
					"${vosao.admin.email}", 
					UserRole.ADMIN);
			getDao().getUserDao().save(admin);
	        log.info("Adding admin user ${vosao.admin.email}");
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
			addPage("Home page", "/", HOME_PAGE_FILE, template.getId(), 0, true, 
					true);
			getBusiness().getContentPermissionBusiness().setPermission(
					"/", guests, ContentPermissionType.READ);
	        addPage("Site user Login", "/login", LOGIN_PAGE_FILE, 
	        		template.getId(), 0, true, false);
	        addPage("Search", "/search", SEARCH_PAGE_FILE, template.getId(), 1,
	        		false, false);
		}
	}

	private void addPage(String title, String url, String resource, 
				Long templateId, Integer sortIndex, boolean cached, 
				boolean searchable) {
        String content = loadResource(resource);
		PageEntity page = new PageEntity(title, url,	templateId);
		page.setCreateUserEmail("admin@test.com");
		page.setModUserEmail("admin@test.com");
		page.setState(PageState.APPROVED);
		page.setSortIndex(sortIndex);
		page.setCached(cached);
		page.setSearchable(searchable);
		getDao().getPageDao().save(page);
		getDao().getPageDao().setContent(page.getId(), 
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
	
	private Dao getDao() {
		return getBusiness().getDao();
	}

	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	private MessageQueue getMessageQueue() {
		return VosaoContext.getInstance().getMessageQueue();
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
		getDao().getTagDao().removeAll();
		getDao().getPageTagDao().removeAll();
		clearCache();
	}

	@Override
	public void clearFileCache() {
		clearFileCache(getDao().getFolderDao().getByPath("/"));
	}
	
	private void clearFileCache(FolderEntity folder) {
		if (folder == null) {
			return;
		}
		String folderPath = getDao().getFolderDao().getFolderPath(
				folder.getId());
		for (FileEntity file : getDao().getFileDao().getByFolder(
				folder.getId())) {
			getDao().getSystemService().getFileCache().remove(folderPath + "/" 
					+ file.getFilename());
		}
		for (FolderEntity child : getDao().getFolderDao().getByParent(
				folder.getId())) {
			clearFileCache(child);
		}
	}

	@Override
	public void loadDefaultSite() {
		try {
			byte[] file = StreamUtil.getBytesResource(DEFAULT_SITE);
			getBusiness().getSystemService().getCache().putBlob(
					DEFAULT_SITE, file);
			getMessageQueue().publish(new ImportMessage.Builder()
					.setStart(1)
					.setFilename(DEFAULT_SITE)
					.create());
		}
		catch (IOException e) {
			log.error("Can't load default site: " + e.getMessage());
		}
	}
	
}
