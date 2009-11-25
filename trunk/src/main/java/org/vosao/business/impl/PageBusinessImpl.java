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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.ContentPermissionBusiness;
import org.vosao.business.CurrentUser;
import org.vosao.business.MessageBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.pagefilter.GoogleAnalyticsPageFilter;
import org.vosao.business.impl.pagefilter.JavaScriptPageFilter;
import org.vosao.business.impl.pagefilter.PageFilter;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.UserEntity;
import org.vosao.enums.PageState;
import org.vosao.filter.SiteFilter;
import org.vosao.velocity.VelocityPluginService;
import org.vosao.velocity.VelocityService;
import org.vosao.velocity.impl.VelocityPluginServiceImpl;
import org.vosao.velocity.impl.VelocityServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

    private static final Log logger = LogFactory.getLog(PageBusinessImpl.class);

    private ConfigBusiness configBusiness;
    private MessageBusiness messageBusiness;
    private ContentPermissionBusiness contentPermissionBusiness;
	private VelocityPluginService velocityPluginService;

	public void init() throws Exception {
		velocityPluginService = new VelocityPluginServiceImpl(getDao(), 
				getSystemService());
	}
	
	@Override
	public TreeItemDecorator<PageEntity> getTree(final List<PageEntity> pages) {
		Map<String, TreeItemDecorator<PageEntity>> buf = 
				new HashMap<String, TreeItemDecorator<PageEntity>>();
		for (PageEntity page : pages) {
			buf.put(page.getFriendlyURL(), 
					new TreeItemDecorator<PageEntity>(page, null));
		}
		TreeItemDecorator<PageEntity> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageEntity> page = buf.get(id);
			if (StringUtils.isEmpty(page.getEntity().getParentUrl())) {
				root = page;
			}
			else {
				TreeItemDecorator<PageEntity> parent = buf.get(page.getEntity()
						.getParentUrl());
				if (parent != null) {
					parent.getChildren().add(page);
					page.setParent(parent);
				}
			}
		}
		return root;
	}	
	
	@Override
	public String render(PageEntity page, String languageCode) {
		if (page.getTemplate() != null) {
			TemplateEntity template = getDao().getTemplateDao().getById(
					page.getTemplate());
			VelocityContext context = createContext(languageCode);
			context.put("page", createPageRenderDecorator(page, languageCode));
			return pagePostProcess(getSystemService()
					.render(template.getContent(), context));
		}
		else {
			ContentEntity content = getPageContent(page, languageCode);
			return pagePostProcess(content.getContent());
		}
	}
	
	@Override	
	public PageRenderDecorator createPageRenderDecorator(final PageEntity page,
			final String languageCode) {
		return new PageRenderDecorator(page, languageCode, getDao(), this, 
				getSystemService());
	}
	
	@Override
	public VelocityContext createContext(final String languageCode) {
		LanguageEntity language = getDao().getLanguageDao().getByCode(
				languageCode);
		VelocityContext context = new VelocityContext();
		ConfigEntity configEntity = getConfigBusiness().getConfig();
		context.put("language", language);
		context.put("config", configEntity);
		VelocityService velocityService = new VelocityServiceImpl(getDao(),
				this, languageCode);
		context.put("service", velocityService);
		context.put("plugin", getVelocityPluginService());
		context.put("messages", getMessageBusiness().getBundle(languageCode));
		return context;
	}
	
	private String pagePostProcess(final String page) {
		List<PageFilter> filters = createFilters();
		String result = page;
		for (PageFilter filter : filters) {
			result = filter.apply(result);
		}
		return result;
	}
	
	private List<PageFilter> createFilters() {
		List<PageFilter> result = new ArrayList<PageFilter>();
		result.add(new GoogleAnalyticsPageFilter(configBusiness, this));
		result.add(new JavaScriptPageFilter(configBusiness, this));
		return result;
	}

	@Override
	public List<String> validateBeforeUpdate(final PageEntity page) {
		List<String> errors = new ArrayList<String>();
		if (page.getId() == null) {
			PageEntity myPage = getDao().getPageDao().getByUrl(
					page.getFriendlyURL());
			if (myPage != null) {
				errors.add("Page with such friendly URL already exists");
			}
		}
		if (StringUtils.isEmpty(page.getFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		else {
			if (SiteFilter.isSkipUrl(page.getFriendlyURL())) {
				errors.add("Entered url is reserved for internal use.");
			}
		}
		if (!page.isRoot() 
			&& StringUtils.isEmpty(page.getPageFriendlyURL())) {
			errors.add("Friendly URL is empty");
		}
		if (StringUtils.isEmpty(page.getTitle())) {
			errors.add("Title is empty");
		}
		return errors;
	}

	@Override
	public TreeItemDecorator<PageEntity> getTree() {
		return getTree(getDao().getPageDao().select());
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
	public MessageBusiness getMessageBusiness() {
		return messageBusiness;
	}

	@Override
	public void setMessageBusiness(MessageBusiness bean) {
		messageBusiness = bean;		
	}

	@Override
	public ContentPermissionBusiness getContentPermissionBusiness() {
		return contentPermissionBusiness;
	}

	@Override
	public void setContentPermissionBusiness(ContentPermissionBusiness bean) {
		contentPermissionBusiness = bean;		
	}

	public VelocityPluginService getVelocityPluginService() {
		return velocityPluginService;
	}

	@Override
	public ContentEntity getPageContent(PageEntity page, String languageCode) {
		ContentEntity content = getDao().getContentDao().getByLanguage(
				PageEntity.class.getName(), page.getId(), languageCode);
		if (content == null) {
			content = getDao().getContentDao().getByLanguage(
					PageEntity.class.getName(), page.getId(), 
					LanguageEntity.ENGLISH_CODE);
		}
		if (content == null) {
			logger.error("No content found for page " + page.getTitle());
			content = new ContentEntity();
			content.setParentClass(PageEntity.class.getName());
			content.setParentKey(page.getId());
			content.setLanguageCode(languageCode);
		}
		return content;
	}

	@Override
	public PageEntity addVersion(PageEntity oldPage, final Integer version, 
			final String versionTitle, final UserEntity user) {
		PageEntity page = new PageEntity();
		page.copy(oldPage);
		page.setState(PageState.EDIT);
		page.setVersion(version);
		page.setVersionTitle(versionTitle);
		Date dt = new Date();
		page.setCreateDate(dt);
		page.setCreateUserEmail(user.getEmail());
		page.setModDate(dt);
		page.setModUserEmail(user.getEmail());
		getDao().getPageDao().save(page);
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				oldPage.getId());
		for (ContentEntity content : contents) {
			getDao().getPageDao().setContent(page.getId(), 
					content.getLanguageCode(), content.getContent());
		}
		return page;
	}

	@Override
	public List<PageEntity> select() {
		return securityFilter(getDao().getPageDao().select());
	}
	
	private List<PageEntity> securityFilter(List<PageEntity> list) {
		List<PageEntity> result = new ArrayList<PageEntity>();
		for (PageEntity page : list) {
			if (canReadPage(page.getFriendlyURL())) {
				result.add(page);
			}
		}
		return result;
	}

	@Override
	public PageEntity getById(String id) {
		return securityFilter(getDao().getPageDao().getById(id));
	}

	private PageEntity securityFilter(PageEntity page) {
		if (page != null) {
			if (!canReadPage(page.getFriendlyURL())) {
				return null;
			}
		}
		return page;
	}
	
	private boolean canReadPage(String url) {
		return !getContentPermissionBusiness().getPermission(
				url, CurrentUser.getInstance()).isDenied();
	}

	private boolean canWritePage(String url) {
		return getContentPermissionBusiness().getPermission(
				url, CurrentUser.getInstance()).isChangeGranted();
	}

	@Override
	public PageEntity getByUrl(String url) {
		return securityFilter(getDao().getPageDao().getByUrl(url));
	}

	@Override
	public List<PageEntity> getByParent(String url) {
		return securityFilter(getDao().getPageDao().getByParent(url));
	}

	@Override
	public void remove(List<String> ids) {
		List<String> removeIds = new ArrayList<String>();
		for (String id : ids) {
			PageEntity page = getDao().getPageDao().getById(id);
			if (page != null) {
				if (canWritePage(page.getFriendlyURL())) {
					removeIds.add(id);
				}
			}
		}
		getDao().getPageDao().remove(removeIds);
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
		PageEntity page = getById(pageId);
		if (page != null) {
			return getDao().getPageDao().getContents(pageId);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<PageEntity> selectByUrl(String url) {
		if (canReadPage(url)) {
			return getDao().getPageDao().selectByUrl(url);
		}
		return Collections.EMPTY_LIST;
	}
}

