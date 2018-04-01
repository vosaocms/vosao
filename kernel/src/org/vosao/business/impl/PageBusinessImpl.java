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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.AlternatorTool;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.IteratorTool;
import org.apache.velocity.tools.generic.ListTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.RenderTool;
import org.apache.velocity.tools.generic.SortTool;
import org.apache.velocity.tools.generic.ValueParser;
import org.apache.velocity.tools.view.tools.LinkTool;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.pagefilter.BodyBeginPageFilter;
import org.vosao.business.impl.pagefilter.HeadBeginPageFilter;
import org.vosao.business.impl.pagefilter.HeadEndPageFilter;
import org.vosao.business.impl.pagefilter.HtmlEndPageFilter;
import org.vosao.business.impl.pagefilter.MetaPageFilter;
import org.vosao.business.impl.pagefilter.PageFilter;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.business.page.PageRenderDecorator;
import org.vosao.business.page.impl.SimplePageRenderDecorator;
import org.vosao.business.page.impl.StructurePageRenderDecorator;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.enums.PageState;
import org.vosao.i18n.Messages;
import org.vosao.utils.UrlUtil;
import org.vosao.velocity.MyDateTool;
import org.vosao.velocity.MyEscTool;
import org.vosao.velocity.VelocityPluginService;
import org.vosao.velocity.VelocityService;
import org.vosao.velocity.impl.VelocityPluginServiceImpl;
import org.vosao.velocity.impl.VelocityServiceImpl;
import org.vosao.velocity.impl.VosaoTool;

/**
 * @author Alexander Oleynik
 */
public class PageBusinessImpl extends AbstractBusinessImpl 
	implements PageBusiness {

	private VelocityPluginService velocityPluginService;
	private List<PageFilter> pageFilters;
	private VelocityService velocityService;

	private List<PageFilter> getPageFilters() {
		if (pageFilters == null) {
			pageFilters = new ArrayList<PageFilter>();
			pageFilters.add(new HeadBeginPageFilter(getBusiness()));
			pageFilters.add(new HeadEndPageFilter(getBusiness()));
			pageFilters.add(new HtmlEndPageFilter(getBusiness()));
			pageFilters.add(new BodyBeginPageFilter(getBusiness()));
			pageFilters.add(new MetaPageFilter());
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
		sortTree(root);
		return root;
	}	
	
	private void sortTree(TreeItemDecorator<PageEntity> page) {
		if (page.isHasChildren()) {
			Collections.sort(page.getChildren(), 
				new Comparator<TreeItemDecorator<PageEntity>>() {
					@Override
					public int compare(TreeItemDecorator<PageEntity> o1,
							TreeItemDecorator<PageEntity> o2) {
						return PageHelper.SORT_INDEX_ASC.compare(
								o1.getEntity(), o2.getEntity());
					}
			});
			for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
				sortTree(child);
			}
		}
	}
	
	@Override 
	public String render(PageEntity page, String template, 
			String languageCode) {
		VosaoContext.getInstance().getPageRenderingContext().setPage(page);
		VelocityContext context = createContext(languageCode, page);
		context.put("page", createPageRenderDecorator(page, languageCode));
		return pagePostProcess(getSystemService().render(template, context), 
				page);
	}
	
	@Override
	public String render(PageEntity page, String languageCode) {
		VosaoContext.getInstance().getPageRenderingContext().setPage(page);
		if (page.getTemplate() != null) {
			TemplateEntity template = getDao().getTemplateDao().getById(
					page.getTemplate());
			return render(page, template.getContent(), languageCode);
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
			return new StructurePageRenderDecorator(page, languageCode, null, 
					getDao(), this, getSystemService());
		}
		logger.error("Wrong page type " + page.getTitle());
		return null;
	}
	
	@Override	
	public PageRenderDecorator createStructuredPageRenderDecorator(
			final PageEntity page, final String languageCode,
			StructureTemplateEntity template) {
		if (page.isStructured()) {
			return new StructurePageRenderDecorator(page, languageCode, 
					template, getDao(), this, getSystemService());
		}
		else {
			logger.error("Wrong page type " + page.getTitle());
			return null;
		}
	}

	@Override
	public VelocityContext createContext(final String languageCode,
			PageEntity page) {
		LanguageEntity language = getDao().getLanguageDao().getByCode(
				languageCode);
		VelocityContext context = new VelocityContext();
		ConfigEntity configEntity = getBusiness().getConfigBusiness().getConfig();
		addVelocityTools(context);
		context.put("language", language);
		context.put("locale", new Locale(language.getCode()));
		context.put("config", configEntity);
		context.put("service", getVelocityService());
		context.put("plugin", getVelocityPluginService().getPlugins());
		context.put("messages", getBusiness().getMessageBusiness().getBundle(
				languageCode));
		context.put("user", getBusiness().getUser());
		context.put("request", VosaoContext.getInstance().getRequest());
		context.put("response", VosaoContext.getInstance().getResponse());
		context.put("timezone", getBusiness().getTimeZone());
		VosaoTool vosaoTool = new VosaoTool();
		vosaoTool.setPage(page);
		context.put("vosao", vosaoTool);
		return context;
	}

	@Override
	public void addVelocityTools(VelocityContext context) {
		context.put("date", new MyDateTool(getBusiness().getTimeZone()));
		context.put("esc", new MyEscTool());
		context.put("link", new LinkTool());
		context.put("list", new ListTool());
		context.put("number", new NumberTool());
		context.put("render", new RenderTool());
		context.put("sorter", new SortTool());
		context.put("math", new MathTool());
		context.put("alternator", new AlternatorTool());
		context.put("comparisonDate", new ComparisonDateTool());
		context.put("iterator", new IteratorTool());
		context.put("parser", new ValueParser());
	}
	
	private String pagePostProcess(final String content, 
			final PageEntity pageEntity) {
		String result = content;
		if (!pageEntity.isSkipPostProcessing()) {
			for (PageFilter filter : getPageFilters()) {
				result = filter.apply(result, pageEntity);
			}
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
				errors.add(Messages.get("page.already_exists"));
			}
		}
		if (StringUtils.isEmpty(page.getFriendlyURL())) {
			errors.add(Messages.get("url_is_empty"));
		}
		else {
			if (VosaoContext.getInstance().isSkipUrl(page.getFriendlyURL())) {
				errors.add(Messages.get("url_reserved"));
			}
		}
		if (StringUtils.isEmpty(page.getPageFriendlyURL())
			&& !page.isRoot()) {
			errors.add(Messages.get("url_is_empty"));
		}
		if (StringUtils.isEmpty(page.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		return errors;
	}

	@Override
	public TreeItemDecorator<PageEntity> getTree() {
		return getTree(getDao().getPageDao().select());
	}

	@Override
	public VelocityPluginService getVelocityPluginService() {
		if (velocityPluginService == null) {
			velocityPluginService = new VelocityPluginServiceImpl(getBusiness());
		}
		return velocityPluginService;
	}

	@Override
	public ContentEntity getPageContent(PageEntity page, String languageCode) {
		ContentEntity content = getDao().getContentDao().getByLanguage(
				PageEntity.class.getName(), page.getId(), languageCode);
		if (content == null || content.getContent().isEmpty()) {
			content = getDao().getContentDao().getByLanguage(
					PageEntity.class.getName(), page.getId(), 
					getBusiness().getDefaultLanguage());
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
		page.setKey(null);
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
	public PageEntity getById(Long id) {
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
		return !getBusiness().getContentPermissionBusiness().getPermission(
				url, VosaoContext.getInstance().getUser()).isDenied();
	}

	private boolean canWritePage(String url) {
		return getBusiness().getContentPermissionBusiness().getPermission(
				url, VosaoContext.getInstance().getUser()).isChangeGranted();
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
	public List<PageEntity> getByParentApproved(String url) {
		return securityFilter(getDao().getPageDao().getByParentApproved(url));
	}

	@Override
	public List<PageEntity> getByParentApproved(
			final String url, Date startDate, Date endDate) {
		return securityFilter(getDao().getPageDao().getByParentApproved(
				url, startDate, endDate));
	}

	@Override
	public void remove(List<Long> ids) {
		List<Long> removeIds = new ArrayList<Long>();
		PageMessage message = new PageMessage(Topic.PAGES_DELETED);
		for (Long id : ids) {
			PageEntity page = getDao().getPageDao().getById(id);
			if (page != null) {
				if (canWritePage(page.getFriendlyURL())) {
					if (isLastRootPageVersion(page)) {
						continue;
					}
					removeIds.add(id);
					message.addPage(page.getFriendlyURL(), id);
				}
			}
		}
		TreeItemDecorator<FolderEntity> root = getBusiness().getFolderBusiness()
				.getTree();
		List<Long> folderIds = new ArrayList<Long>();
		for (Long id : removeIds) {
			PageEntity page = getDao().getPageDao().getById(id);
			TreeItemDecorator<FolderEntity> folder = getBusiness()
					.getFolderBusiness()
					.findFolderByPath(root, "/page" + page.getFriendlyURL()); 
			if (folder != null) {	
				folderIds.add(folder.getEntity().getId());
			}
			getBusiness().getSystemService().getPageCache().remove(
					page.getFriendlyURL());
		}
		getBusiness().getFolderBusiness().recursiveRemove(folderIds);
		getDao().getPageDao().remove(removeIds);
		getBusiness().getMessageQueue().publish(message);
	}

	private boolean isLastRootPageVersion(PageEntity page) {
		if (page.isRoot()) {
			List<PageEntity> versions = getDao().getPageDao().selectByUrl("/");
			return versions.size() == 1;
		}
		return false;
	}

	@Override
	public void remove(String pageURL) {
		List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
		if (pages.size() > 0) {
			List<Long> ids = new ArrayList<Long>();
			ids.add(pages.get(0).getId());
			remove(ids);
			for (PageEntity page : pages) {
				getBusiness().getSystemService().getPageCache().remove(
						page.getFriendlyURL());
			}
		}
	}
	
	@Override
	public void removeVersion(Long id) {
		PageEntity page = getDao().getPageDao().getById(id);
		if (page != null) {
			if (canWritePage(page.getFriendlyURL())) {
				getDao().getPageDao().removeVersion(id);
				getBusiness().getSystemService().getPageCache().remove(
						page.getFriendlyURL());
				PageMessage message = new PageMessage(Topic.PAGES_DELETED,
						page.getFriendlyURL(), id);
				getBusiness().getMessageQueue().publish(message);
			}
		}
	}

	@Override
	public List<ContentEntity> getContents(Long pageId) {
		PageEntity page = getById(pageId);
		if (page != null) {
			return securityFilter(page.getFriendlyURL(), 
					getDao().getPageDao().getContents(pageId));
		}
		return Collections.EMPTY_LIST;
	}

	private List<ContentEntity> securityFilter(String url, 
			List<ContentEntity> list) {
		ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness()
				.getPermission(url, VosaoContext.getInstance().getUser());
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
		ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness()
				.getPermission(url, VosaoContext.getInstance().getUser());
		if (perm.isAllLanguages()) {
			return perm.isChangeGranted();
		}
		return perm.isChangeGranted() && perm.getLanguagesList().contains(
				languageCode);
	}

	@Override
	public void saveContent(PageEntity page, String language, String content) {
		ContentEntity contentEntity = getDao().getPageDao().setContent(
				page.getId(), language, content);
		getSystemService().getPageCache().remove(page.getFriendlyURL());
		PageMessage message = new PageMessage(Topic.PAGES_CHANGED,
				page.getFriendlyURL(), page.getId());
		getBusiness().getMessageQueue().publish(message);
	}

	@Override
	public Integer getNextSortIndex(String friendlyURL) {
		String parentURL = UrlUtil.getParentFriendlyURL(friendlyURL);
		if (StringUtils.isEmpty(parentURL)) {
			return 0;
		}
		return reorderPages(parentURL);
	}
	
	private Integer reorderPages(String url) {
		List<PageEntity> pages = getDao().getPageDao().selectAllChildren(url);
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		Map<String, Integer> indexes = new HashMap<String, Integer>();
		int currentIndex = 0;
		for (PageEntity page : pages) {
			String key = page.getFriendlyURL();
			if (indexes.containsKey(key)){
				if (page.getSortIndex() != indexes.get(key)) {
					page.setSortIndex(indexes.get(key));
					getDao().getPageDao().save(page);
				}
			}
			else {
				if (page.getSortIndex() != currentIndex) {
					page.setSortIndex(currentIndex);
					getDao().getPageDao().save(page);
				}
				indexes.put(key, page.getSortIndex());
				currentIndex++;
			}
		}
		return currentIndex;
	}

	@Override
	public void moveDown(PageEntity page) {
		String parentURL = UrlUtil.getParentFriendlyURL(page.getFriendlyURL());
		if (StringUtils.isEmpty(parentURL)) {
			return;
		}
		reorderPages(parentURL);
		List<PageEntity> pages = getByParent(parentURL);
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		PageEntity currentPage = findByFriendlyURL(pages, page.getFriendlyURL());
		if (currentPage == null) {
			logger.error("page not found in moveDown");
		}
		int currentPos = pages.indexOf(currentPage);
		if (currentPos < pages.size() - 1 ) {
			swapSortIndexes(currentPage.getFriendlyURL(), 
					pages.get(currentPos + 1).getFriendlyURL());
		}
	}

	private PageEntity findByFriendlyURL(List<PageEntity> pages, 
			String friendlyURL) {
		for (PageEntity page : pages) {
			if (page.getFriendlyURL().equals(friendlyURL)) {
				return page;
			}
		}
		return null;
	}
	
	private void swapSortIndexes(String url1, String url2) {
		List<PageEntity> pages1 = getDao().getPageDao().selectByUrl(url1);
		int index1 = pages1.get(0).getSortIndex();
		List<PageEntity> pages2 = getDao().getPageDao().selectByUrl(url2);
		int index2 = pages2.get(0).getSortIndex();
		for (PageEntity page : pages1) {
			page.setSortIndex(index2);
			getDao().getPageDao().save(page);			
		}
		for (PageEntity page : pages2) {
			page.setSortIndex(index1);
			getDao().getPageDao().save(page);			
		}
	}
	
	@Override
	public void moveUp(PageEntity page) {
		String parentURL = UrlUtil.getParentFriendlyURL(page.getFriendlyURL());
		if (StringUtils.isEmpty(parentURL)) {
			return;
		}
		reorderPages(parentURL);
		List<PageEntity> pages = getByParent(parentURL);
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		PageEntity currentPage = findByFriendlyURL(pages, page.getFriendlyURL());
		if (currentPage == null) {
			logger.error("page not found in moveUp");
		}
		int currentPos = pages.indexOf(currentPage);
		if (currentPos > 0) {
			swapSortIndexes(currentPage.getFriendlyURL(), 
					pages.get(currentPos - 1).getFriendlyURL());
		}
	}

	@Override
	public VelocityService getVelocityService() {
		if (velocityService == null) {
			velocityService = new VelocityServiceImpl(getBusiness());
		}
		return velocityService;
	}

	@Override
	public void move(PageEntity page, String friendlyURL) {
		if (getDao().getPageDao().selectByUrl(friendlyURL).size() > 0) {
			return;
		}
		if (canWritePage(page.getFriendlyURL()) 
			&& canWritePage(friendlyURL)) {
			List<PageEntity> children = getDao().getPageDao().getByParent(
					page.getFriendlyURL()); 
			for (PageEntity child : children) {
				move(child, friendlyURL + "/" + child.getPageFriendlyURL());
			}
			List<PageEntity> versions = getDao().getPageDao().selectByUrl(
					page.getFriendlyURL());
			for (PageEntity version : versions) {
				version.setFriendlyURL(friendlyURL);
				getDao().getPageDao().save(version);
			}
			getBusiness().getSystemService().getPageCache().remove(
					page.getFriendlyURL());
			getBusiness().getSystemService().getPageCache().remove(
					friendlyURL);
		}
	}

	@Override
	public void moveAfter(PageEntity page, PageEntity refPage) {
		List<PageEntity> pages = getByParent(refPage.getParentFriendlyURL());
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		int i = 0;
		for (PageEntity item : pages) {
			if (item.equals(refPage)) {
				item.setSortIndex(i++);
				getDao().getPageDao().save(item);
				page.setSortIndex(i);
				getDao().getPageDao().save(page);
			}
			else if (!item.equals(page)) {
				item.setSortIndex(i);
				getDao().getPageDao().save(item);
			}
			if (!item.equals(page)) {
				i++;
			}
		}
	}

	@Override
	public void moveBefore(PageEntity page, PageEntity refPage) {
		List<PageEntity> pages = getByParent(refPage.getParentFriendlyURL());
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		int i = 0;
		for (PageEntity item : pages) {
			if (item.equals(refPage)) {
				page.setSortIndex(i++);
				getDao().getPageDao().save(page);
			}
			if (!item.equals(page)) {
				item.setSortIndex(i++);
				getDao().getPageDao().save(item);
			}
		}
	}

	private boolean pageExists(String url) {
		return getDao().getPageDao().selectByUrl(url).size() > 0;
	}

	@Override
	public String makeUniquePageURL(String url) {
		int suffix = 1;
		String pageURL = url;
		while (pageExists(pageURL)) {
			pageURL = url + suffix;
			suffix++;
		}
		return pageURL;
	}

	private void copyContent(PageEntity src, PageEntity dst) {
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				src.getId());
		for (ContentEntity content : contents) {
			ContentEntity newContent = new ContentEntity();
			newContent.copy(content);
			newContent.setId(null);
			newContent.setParentKey(dst.getId());
			getDao().getContentDao().save(newContent);
			getBusiness().getSystemService().getPageCache().remove(
					dst.getFriendlyURL());
		}
	}
	
	@Override
	public void copy(PageEntity page, String parentURL) {
		String newURL = parentURL + "/" + page.getPageFriendlyURL();
		List<PageEntity> versions = getDao().getPageDao().selectByUrl(
				page.getFriendlyURL());
		for (PageEntity version : versions) {
			PageEntity newVersion = new PageEntity();
			newVersion.copy(version);
			newVersion.setId(null);
			newVersion.setFriendlyURL(newURL);
			save(newVersion);
			copyContent(version, newVersion);
		}
		for (PageEntity child : getDao().getPageDao().getByParent(
				page.getFriendlyURL())) {
			copy(child, newURL);
		}
	}

	@Override
	public void save(PageEntity page) {
		getDao().getPageDao().save(page);
		getPageFolder(page.getFriendlyURL());
		getBusiness().getSystemService().getPageCache().remove(
				page.getFriendlyURL());
		
		PageMessage message = new PageMessage(Topic.PAGES_CHANGED,
				page.getFriendlyURL(), page.getId());
		
		getBusiness().getMessageQueue().publish(message);
	}

	@Override
	public PageEntity getPageDefaultSettings(String url) {
		String defaultUrl = url + (url.equals("/") ? "_default" : "/_default");
		List<PageEntity> pages = getDao().getPageDao().selectByUrl(defaultUrl);
		if (pages.size() == 0) {
			PageEntity page = new PageEntity();
			page.setFriendlyURL(defaultUrl);
			page.setTitle("Children default settings");
			getDao().getPageDao().save(page);
			return page;
		}
		return pages.get(0);		
	}

	@Override
	public void setDefaultValues(PageEntity page) {
		PageEntity defaultPage = getPageDefaultSettings(
				page.getParentFriendlyURL());
		
		page.setCached(defaultPage.isCached());
		page.setCommentsEnabled(defaultPage.isCommentsEnabled());
		page.setContentType(defaultPage.getContentType());
		page.setDescription(defaultPage.getDescription());
		page.setHeadHtml(defaultPage.getHeadHtml());
		page.setKeywords(defaultPage.getKeywords());
		page.setPageType(defaultPage.getPageType());
		page.setSearchable(defaultPage.isSearchable());
		page.setSkipPostProcessing(defaultPage.isSkipPostProcessing());
		page.setStructureId(defaultPage.getStructureId());
		page.setStructureTemplateId(defaultPage.getStructureTemplateId());
		page.setTemplate(defaultPage.getTemplate());
		page.setVelocityProcessing(defaultPage.isVelocityProcessing());
		page.setWikiProcessing(defaultPage.isWikiProcessing());
		page.setTitleValue(defaultPage.getTitleValue());
	}

	@Override
	public void updateDefaultContent(PageEntity page) {
		PageEntity defaultPage = getPageDefaultSettings(
				page.getParentFriendlyURL());
		
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				defaultPage.getId());
		
		if (contents.size() > 0) {
			for (ContentEntity content : contents) {
				getDao().getPageDao().setContent(page.getId(), 
						content.getLanguageCode(), content.getContent());
			}
		}
	}

	@Override
	public FolderEntity getPageFolder(String pageURL) {
		return getBusiness().getFolderBusiness().createFolder(
				"/page"	+ pageURL);
	}

	@Override
	public PageEntity getRestPage(String url) {
		PageEntity result;
		String base = url;
		while (base.length() > 0) {
			int slash = base.lastIndexOf("/");
			if (slash == 0) {
				break;
			}
			base = base.substring(0, slash);
			result = getDao().getPageDao().getByUrl(base);
			if (result != null) {
				if (result.isRestful()) {
					return result;
				}
				else {
					return null;
				}
			}
		}
		result = getDao().getPageDao().getByUrl("/");
		if (result != null && result.isRestful()) {
			return result;
		}
		return null;
	}

}

