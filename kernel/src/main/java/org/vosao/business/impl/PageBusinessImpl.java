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
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.ListTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.RenderTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.view.tools.LinkTool;
import org.vosao.business.Business;
import org.vosao.business.CurrentUser;
import org.vosao.business.PageBusiness;
import org.vosao.business.PageRenderDecorator;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.pagefilter.BodyBeginPageFilter;
import org.vosao.business.impl.pagefilter.HeadBeginPageFilter;
import org.vosao.business.impl.pagefilter.HtmlEndPageFilter;
import org.vosao.business.impl.pagefilter.PageFilter;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
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

	private VelocityPluginService velocityPluginService;
	private Business business;
	private List<PageFilter> pageFilters;

	public void init() throws Exception {
		velocityPluginService = new VelocityPluginServiceImpl(getDao(), 
				getSystemService(), getBusiness());
	}
	
	private List<PageFilter> getPageFilters() {
		if (pageFilters == null) {
			pageFilters = new ArrayList<PageFilter>();
			pageFilters.add(new HeadBeginPageFilter(getBusiness()));
			pageFilters.add(new HtmlEndPageFilter(getBusiness()));
			pageFilters.add(new BodyBeginPageFilter(getBusiness()));
		}
		return pageFilters;
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
					.render(template.getContent(), context), page);
		}
		else {
			ContentEntity content = getPageContent(page, languageCode);
			return pagePostProcess(content.getContent(), page);
		}
	}
	
	@Override	
	public PageRenderDecorator createPageRenderDecorator(final PageEntity page,
			final String languageCode) {
		if (page.isSimple()) {
			return new SimplePageRenderDecorator(page, languageCode, 
					getDao(), this, getSystemService());
		}
		if (page.isStructured()) {
			return new StructurePageRenderDecorator(page, languageCode, 
					getDao(), this, getSystemService());
		}
		logger.error("Wrong page type " + page.getTitle());
		return null;
	}
	
	@Override
	public VelocityContext createContext(final String languageCode) {
		LanguageEntity language = getDao().getLanguageDao().getByCode(
				languageCode);
		VelocityContext context = new VelocityContext();
		ConfigEntity configEntity = business.getConfigBusiness().getConfig();
		addVelocityTools(context);
		context.put("language", language);
		context.put("config", configEntity);
		VelocityService velocityService = new VelocityServiceImpl(getDao(),
				this, languageCode);
		context.put("service", velocityService);
		context.put("plugin", getVelocityPluginService().getPlugins());
		context.put("messages", business.getMessageBusiness().getBundle(languageCode));
		return context;
	}
	
	private void addVelocityTools(final VelocityContext context) {
		context.put("date", new DateTool());
		context.put("esc", new EscapeTool());
		context.put("link", new LinkTool());
		context.put("lists", new ListTool());
		context.put("number", new NumberTool());
		context.put("render", new RenderTool());
		context.put("sorter", new SortTool());
	}
	
	private String pagePostProcess(final String content, 
			final PageEntity pageEntity) {
		String result = content;
		for (PageFilter filter : getPageFilters()) {
			result = filter.apply(result, pageEntity);
		}
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
			if (canReadPage(page)) {
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
			if (!canReadPage(page)) {
				return null;
			}
		}
		return page;
	}
	
	private boolean canReadPage(PageEntity page) {
		return canReadPage(page.getFriendlyURL());
	}

	private boolean canReadPage(String url) {
		return !business.getContentPermissionBusiness().getPermission(
				url, CurrentUser.getInstance()).isDenied();
	}

	private boolean canWritePage(String url) {
		return business.getContentPermissionBusiness().getPermission(
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
		TreeItemDecorator<FolderEntity> root = business.getFolderBusiness()
				.getTree();
		List<String> folderIds = new ArrayList<String>();
		for (String id : removeIds) {
			PageEntity page = getDao().getPageDao().getById(id);
			TreeItemDecorator<FolderEntity> folder = business.getFolderBusiness()
					.findFolderByPath(root, "/page" + page.getFriendlyURL()); 
			if (folder != null) {	
				folderIds.add(folder.getEntity().getId());
			}
		}
		business.getFolderBusiness().recursiveRemove(folderIds);
		getDao().getPageDao().remove(removeIds);
	}

	@Override
	public void removeVersion(String id) {
		PageEntity page = getDao().getPageDao().getById(id);
		if (page != null) {
			if (canWritePage(page.getFriendlyURL())) {
				getDao().getPageDao().removeVersion(id);
			}
		}
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
		PageEntity page = getById(pageId);
		if (page != null) {
			return securityFilter(page.getFriendlyURL(), 
					getDao().getPageDao().getContents(pageId));
		}
		return Collections.EMPTY_LIST;
	}

	private List<ContentEntity> securityFilter(String url, 
			List<ContentEntity> list) {
		ContentPermissionEntity perm = business.getContentPermissionBusiness()
				.getPermission(url, CurrentUser.getInstance());
		if (perm.isAllLanguages()) {
			return list;
		}
		List<ContentEntity> result = new ArrayList<ContentEntity>();
		List<String> languages = perm.getLanguagesList();
		for (ContentEntity content : list) {
			if (languages.contains(content.getLanguageCode())) {
				result.add(content);
			}
		}
		return result;
	}
	
	@Override
	public List<PageEntity> selectByUrl(String url) {
		if (canReadPage(url)) {
			return getDao().getPageDao().selectByUrl(url);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public boolean canChangeContent(String url, String languageCode) {
		ContentPermissionEntity perm = business.getContentPermissionBusiness()
				.getPermission(url, CurrentUser.getInstance());
		if (perm.isAllLanguages()) {
			return perm.isChangeGranted();
		}
		return perm.isChangeGranted() && perm.getLanguagesList().contains(
				languageCode);
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business bean) {
		this.business = bean;
	}

	@Override
	public void saveContent(PageEntity page, String language, String content,
			boolean oldSearchable, boolean searchable) {
		ContentEntity contentEntity = getDao().getPageDao().setContent(
				page.getId(), language, content);
		if (searchable) {
			if (oldSearchable) {
				getBusiness().getSearchEngine().updateIndex(contentEntity);
			}
			else {
				getBusiness().getSearchEngine().updateIndex(page);
			}
		}
		else {
			if (oldSearchable) {
				getBusiness().getSearchEngine().removeFromIndex(contentEntity);
			}
			else {
				getBusiness().getSearchEngine().removeFromIndex(page);
			}
		}
		getBusiness().getSearchEngine().saveIndex();
	}
}

