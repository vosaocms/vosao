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

package org.vosao.service.back.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageDependencyEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.TagEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.CommentService;
import org.vosao.service.back.ContentPermissionService;
import org.vosao.service.back.GroupService;
import org.vosao.service.back.LanguageService;
import org.vosao.service.back.PageService;
import org.vosao.service.back.TemplateService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.PageRequestVO;
import org.vosao.service.vo.PageVO;
import org.vosao.servlet.FileUploadServlet;
import org.vosao.utils.DateUtil;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;
import org.vosao.utils.UrlUtil;

/**
 * @author Alexander Oleynik
 */
public class PageServiceImpl extends AbstractServiceImpl 
		implements PageService {

	@Override
	public TreeItemDecorator<PageVO> getTree() {
		List<PageVO> pages = selectLastVersionPages(getPageBusiness().select());
		Map<String, TreeItemDecorator<PageVO>> buf = new HashMap<String, TreeItemDecorator<PageVO>>();
		for (PageVO page : pages) {
			buf.put(page.getFriendlyURL(), new TreeItemDecorator<PageVO>(page,
					null));
		}
		TreeItemDecorator<PageVO> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageVO> page = buf.get(id);
			if (StringUtils.isEmpty(page.getEntity().getParentUrl())) {
				root = page;
			} else {
				TreeItemDecorator<PageVO> parent = buf.get(page.getEntity()
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

	private void sortTree(TreeItemDecorator<PageVO> page) {
		if (page.isHasChildren()) {
			Collections.sort(page.getChildren(),
					new Comparator<TreeItemDecorator<PageVO>>() {
						@Override
						public int compare(TreeItemDecorator<PageVO> o1,
								TreeItemDecorator<PageVO> o2) {
							if (o1.getEntity().getSortIndex() > o2.getEntity()
									.getSortIndex()) {
								return 1;
							}
							if (o1.getEntity().getSortIndex().equals(
									o2.getEntity().getSortIndex())) {
								return 0;
							}
							return -1;
						}
					});
			for (TreeItemDecorator<PageVO> child : page.getChildren()) {
				sortTree(child);
			}
		}
	}

	private List<PageVO> selectLastVersionPages(List<PageEntity> pages) {
		Map<String, PageEntity> pageMap = new HashMap<String, PageEntity>();
		Map<String, Boolean> published = new HashMap<String, Boolean>();
		for (PageEntity page : pages) {
			if (page.isForInternalUse()) {
				continue;
			}
			if (pageMap.containsKey(page.getFriendlyURL())) {
				if (pageMap.get(page.getFriendlyURL()).getVersion() < page
						.getVersion()) {
					pageMap.put(page.getFriendlyURL(), page);
				}
				if (page.isApproved()) {
					published.put(page.getFriendlyURL(), true);
				}
			} else {
				pageMap.put(page.getFriendlyURL(), page);
				published.put(page.getFriendlyURL(), page.isApproved());
			}
		}
		List<PageVO> result = PageVO.create(pageMap.values());
		for (PageVO page : result) {
			boolean isPublished = false;
			if (published.containsKey(page.getFriendlyURL())) {
				isPublished = published.get(page.getFriendlyURL());
			}
			page.setHasPublishedVersion(isPublished);
		}
		return result;
	}

	@Override
	public PageEntity getPage(Long id) {
		return getPageBusiness().getById(id);
	}

	@Override
	public PageEntity getPageByUrl(String url) {
		List<PageEntity> result = getPageBusiness().selectByUrl(url);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public ServiceResponse savePage(Map<String, String> vo) {
		PageEntity page = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			page = getPage(Long.valueOf(vo.get("id")));
		}
		if (page == null) {
			page = new PageEntity();
			page.setFriendlyURL(vo.get("friendlyUrl"));
			page.setSortIndex(getPageBusiness().getNextSortIndex(
					vo.get("friendlyUrl")));
			getPageBusiness().setDefaultValues(page);
		}
		if (vo.get("friendlyUrl") != null) {
			page.setFriendlyURL(vo.get("friendlyUrl"));
		}
		if (vo.get("commentsEnabled") != null) {
			page.setCommentsEnabled(Boolean.valueOf(vo.get("commentsEnabled")));
		}
		if (vo.get("searchable") != null) {
			page.setSearchable(Boolean.valueOf(vo.get("searchable")));
		}
		if (vo.get("velocityProcessing") != null) {
			page.setVelocityProcessing(Boolean.valueOf(vo
					.get("velocityProcessing")));
		}
		if (vo.get("skipPostProcessing") != null) {
			page.setSkipPostProcessing(Boolean.valueOf(vo
					.get("skipPostProcessing")));
		}
		if (vo.get("cached") != null) {
			page.setCached(Boolean.valueOf(vo.get("cached")));
		}
		String languageCode = vo.get("languageCode");
		ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness().getPermission(
						page.getFriendlyURL(),
						VosaoContext.getInstance().getUser());
		boolean approve = Boolean.valueOf(vo.get("approve"));
		if (approve && perm.isPublishGranted()) {
			page.setState(PageState.APPROVED);
		} else {
			page.setState(PageState.EDIT);
		}
		if (!perm.isChangeGranted(languageCode)) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"access_denied"));
		}
		if (vo.get("publishDate") != null) {
			try {
				page.setPublishDate(DateUtil.dateTimeToDate(
						vo.get("publishDate")));
			} catch (ParseException e) {
				return ServiceResponse
						.createErrorResponse(Messages.get(
								"date_wrong_format"));
			}
		}
		if (vo.get("endPublishDate") != null) {
			try {
				if (!StringUtils.isEmpty(vo.get("endPublishDate"))) {
					page.setEndPublishDate(DateUtil.dateTimeToDate(
						vo.get("endPublishDate")));
				}
				else if (page.getEndPublishDate() != null) {
					page.setEndPublishDate(null);
				}
			} catch (ParseException e) {
				return ServiceResponse
						.createErrorResponse(Messages.get(
								"date_wrong_format"));
			}
		}
		if (vo.get("template") != null) {
			page.setTemplate(Long.valueOf(vo.get("template")));
		}
		if (vo.get("titles") != null) {
			page.setTitleValue(vo.get("titles"));
		}
		if (vo.get("title") != null) {
			page.setTitle(vo.get("title"));
		}
		if (vo.get("pageType") != null) {
			page.setPageType(PageType.valueOf(vo.get("pageType")));
		}
		if (vo.get("structureId") != null) {
			page.setStructureId(Long.valueOf(vo.get("structureId")));
		}
		if (vo.get("structureTemplateId") != null) {
			page.setStructureTemplateId(Long.valueOf(vo
					.get("structureTemplateId")));
		}
		if (vo.get("keywords") != null) {
			page.setKeywords(vo.get("keywords"));
		}
		if (vo.get("description") != null) {
			page.setDescription(vo.get("description"));
		}
		if (vo.get("headHtml") != null) {
			page.setHeadHtml(vo.get("headHtml"));
		}
		if (vo.get("dependencies") != null) {
			savePageDependencies(page.getFriendlyURL(), vo.get("dependencies"));
		}
		if (vo.get("contentType") != null) {
			page.setContentType(vo.get("contentType"));
		}
		if (vo.get("wikiProcessing") != null) {
			page.setWikiProcessing(Boolean.valueOf(vo.get("wikiProcessing")));
		}
		if (vo.get("enableCkeditor") != null) {
			page.setEnableCkeditor(Boolean.valueOf(vo.get("enableCkeditor")));
		}
		if (vo.get("restful") != null) {
			page.setRestful(Boolean.valueOf(vo.get("restful")));
		}
		if (vo.get("attributes") != null) {
			page.setAttributes(vo.get("attributes"));
		}
		List<String> errors = getPageBusiness().validateBeforeUpdate(page);
		if (errors.isEmpty()) {
			boolean isNew = page.isNew();
			getPageBusiness().save(page);
			if (isNew && !vo.containsKey("content") 
					&& !page.isForInternalUse()) {
				getPageBusiness().updateDefaultContent(page);
			}
			if (vo.containsKey("content")) {
				getPageBusiness().saveContent(page, languageCode,
						vo.get("content"));
			}
			return ServiceResponse.createSuccessResponse(page.getId()
					.toString());
		} else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}

	}

	@Override
	public List<PageVO> getChildren(String url) {
		List<PageEntity> pages = new ArrayList<PageEntity>();
		for (PageEntity page : getPageBusiness().getByParent(url)) {
			if (!page.isForInternalUse()) {
				pages.add(page);
			}
		}
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		return PageVO.create(pages);
	}

	@Override
	public ServiceResponse deletePages(List<String> ids) {
		getPageBusiness().remove(StrUtil.toLong(ids));
		return ServiceResponse
				.createSuccessResponse(Messages.get(
						"pages.success_delete"));
	}

	@Override
	public ServiceResponse deletePageVersion(Long id) {
		getPageBusiness().removeVersion(id);
		return ServiceResponse
				.createSuccessResponse(Messages.get("page.success_delete"));
	}

	private List<ContentEntity> getContents(Long pageId) {
		return getDao().getPageDao().getContents(pageId);
	}

	@Override
	public List<PageVO> getPageVersions(String url) {
		return PageVO.create(getPageBusiness().selectByUrl(url));
	}

	@Override
	public ServiceResponse addVersion(String url, String versionTitle) {
		if (!getBusiness().getContentPermissionBusiness().getPermission(url,
				VosaoContext.getInstance().getUser()).isChangeGranted()) {
			return ServiceResponse.createErrorResponse(
					Messages.get("access_denied"));
		}
		List<PageEntity> list = getPageBusiness().selectByUrl(url);
		if (list.size() > 0) {
			PageEntity lastPage = list.get(list.size() - 1);
			return ServiceResponse.createSuccessResponse(
					getPageBusiness().addVersion(lastPage,
							lastPage.getVersion() + 1, versionTitle,
							VosaoContext.getInstance().getUser()).getId()
					.toString());
		}
		return ServiceResponse.createErrorResponse(Messages.get(
				"page_not_found", url));
	}

	@Override
	public ServiceResponse approve(Long pageId) {
		if (pageId == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"page_not_found", pageId));
		}
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"page_not_found", pageId));
		}
		if (!getBusiness().getContentPermissionBusiness().getPermission(
				page.getFriendlyURL(), VosaoContext.getInstance().getUser())
				.isPublishGranted()) {
			return ServiceResponse.createErrorResponse(
					Messages.get("access_denied"));
		}
		page.setState(PageState.APPROVED);
		getPageBusiness().save(page);
		return ServiceResponse
				.createSuccessResponse(Messages.get("page.success_approve"));
	}

	@Override
	public PageRequestVO getPageRequest(final Long id, final String parentUrl) {
		try {
			PageRequestVO result = new PageRequestVO();
			PageEntity page = getPage(id);
			result.setPage(page);
			result.setConfig(VosaoContext.getInstance().getConfig());
			String permUrl = parentUrl;
			if (page != null) {
				String url = page.getFriendlyURL();
				result.setVersions(getPageVersions(url));
				result.setChildren(getChildren(url));
				result.setComments(getCommentService().getByPage(url));
				result.setContents(getContents(id));
				result.setPermissions(getContentPermissionService()
						.selectByUrl(url));
				
				result.setTags(getPageTags(url));
				permUrl = page.getFriendlyURL();
				if (page.isStructured()) {
					StructureEntity structure = getDao().getStructureDao()
							.getById(page.getStructureId());
					
					if (structure != null) {
						result.setStructureFields(structure.getFields());
					}
				}
				result.setDependencies(getDependencies(page.getFriendlyURL()));
				FolderEntity folder = getBusiness().getPageBusiness()
						.getPageFolder(page.getFriendlyURL());
				result.setFolderId(folder != null ? folder.getId() : null);

				VosaoContext.getInstance().getSession().set(
						FileUploadServlet.IMAGE_UPLOAD_PAGE_ID, id.toString());
			}
			else {
				result.setPage(getPageBusiness().getPageDefaultSettings(
						parentUrl));
				result.getPage().setId(null);
				result.getPage().setFriendlyURL("");
				result.getPage().setParentFriendlyURL(parentUrl);
				result.setChildren(Collections.EMPTY_LIST);
				result.setVersions(Collections.EMPTY_LIST);
				result.setComments(Collections.EMPTY_LIST);
				result.setPermissions(Collections.EMPTY_LIST);				
				result.setTags(Collections.EMPTY_LIST);				
			}
			if (result.getContents() == null 
					|| result.getContents().size() == 0) {
				PageEntity defaultPage = getPageBusiness()
						.getPageDefaultSettings(parentUrl);

				result.setContents(getDao().getPageDao().getContents(
						defaultPage.getId()));
			}
			result.setTemplates(getTemplateService().getTemplates());
			result.setLanguages(getLanguageService().select());
			result.setGroups(getGroupService().select());
			result.setPagePermission(getContentPermissionService()
					.getPermission(permUrl));
			result.setStructures(getDao().getStructureDao().select());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<TagEntity> getPageTags(String pageURL) {
		PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
		if (pageTag != null) {
			return getDao().getTagDao().getById(pageTag.getTags());
		}
		return Collections.EMPTY_LIST;
	}

	private CommentService getCommentService() {
		return getBackService().getCommentService();
	}

	private LanguageService getLanguageService() {
		return getBackService().getLanguageService();
	}

	private TemplateService getTemplateService() {
		return getBackService().getTemplateService();
	}

	private ContentPermissionService getContentPermissionService() {
		return getBackService().getContentPermissionService();
	}

	private GroupService getGroupService() {
		return getBackService().getGroupService();
	}

	private String loadResource(final String url) {
		try {
			return StreamUtil.getTextResource(url);
		} catch (IOException e) {
			logger.error("Can't read resource " + e);
			return Messages.get("resource_error", url);
		}
	}

	@Override
	public ServiceResponse restore(Long pageId, String pageType, String language) {
		PageEntity page = getPage(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"page_not_found", pageId));
		}
		String content = getPredefinedContent(pageType);
		if (content == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"page.wrong_type"));
		}
		if (page.isStructured()) {
			return ServiceResponse
					.createErrorResponse(Messages.get("page.change_type"));
		}
		page.setModDate(new Date());
		page.setModUserEmail(VosaoContext.getInstance().getUser().getEmail());
		ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness().getPermission(
						page.getFriendlyURL(),
						VosaoContext.getInstance().getUser());
		page.setState(PageState.EDIT);
		if (!perm.isChangeGranted()) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"access_denied"));
		}
		getPageBusiness().save(page);
		getPageBusiness().saveContent(page, language, content);
		return ServiceResponse.createSuccessResponse(
				Messages.get("page.success_restore"));
	}

	private String getPredefinedContent(String pageType) {
		String result = null;
		if (pageType.equals("home")) {
			result = loadResource(SetupBeanImpl.HOME_PAGE_FILE);
		} else if (pageType.equals("login")) {
			result = loadResource(SetupBeanImpl.LOGIN_PAGE_FILE);
		} else if (pageType.equals("search")) {
			result = loadResource(SetupBeanImpl.SEARCH_PAGE_FILE);
		}
		return result;
	}

	@Override
	public ServiceResponse moveDown(Long pageId) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", pageId));
		}
		getPageBusiness().moveDown(page);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	@Override
	public ServiceResponse moveUp(Long pageId) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", pageId));
		}
		getPageBusiness().moveUp(page);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	@Override
	public ServiceResponse remove(String pageURL) {
		if (!"/".equals(pageURL)) getPageBusiness().remove(pageURL);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	private PageBusiness getPageBusiness() {
		return getBusiness().getPageBusiness();
	}
		
	private String getBaseURL(String url) {
		if (url.equals("/")) {
			return "";
		}
		return url;
	}
	
	@Override
	public ServiceResponse rename(Long id, Long parentId, String title) {
		if (id == null) {
			PageEntity parent = getPageBusiness().getById(parentId);
			if (parent == null) {
				return ServiceResponse.createErrorResponse(
						Messages.get("page.not_found", parentId));
			}
			String url = getBaseURL(parent.getFriendlyURL()) + "/" 
					+ UrlUtil.titleToURL(title);
			PageEntity page = new PageEntity(title, 
					getPageBusiness().makeUniquePageURL(url));
			page.setTemplate(parent.getTemplate());
			getPageBusiness().save(page);
			return ServiceResponse.createSuccessResponse(
					page.getId().toString());
		}
		else {
			PageEntity page = getPageBusiness().getById(id);
			if (page == null) {
				return ServiceResponse.createErrorResponse(
						Messages.get("page.not_found", id));
			}
			if (!getPageBusiness().canChangeContent(
					page.getFriendlyURL(), null)) {
				return ServiceResponse.createErrorResponse(
						Messages.get("access_denied"));
			}
			List<PageEntity> versions = getDao().getPageDao().selectByUrl(
					page.getFriendlyURL());
			for (PageEntity version : versions) {
				version.setTitle(title);
				getPageBusiness().save(version);
			}
			if (!page.isRoot()) {
				String url = getBaseURL(page.getParentFriendlyURL()) + "/" 
					+ UrlUtil.titleToURL(title);
				getPageBusiness().move(page, 
						getPageBusiness().makeUniquePageURL(url));
			}
			return ServiceResponse.createSuccessResponse(Messages.get("success"));
		}
	}

	@Override
	public ServiceResponse removePage(Long pageId) {
		PageEntity page = getPageBusiness().getById(pageId);
		if (page != null && !page.isRoot()) {
			getPageBusiness().remove(page.getFriendlyURL());
		}
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	@Override
	public ServiceResponse movePage(Long pageId, Long refPageId, String type) {
		PageEntity page = getPageBusiness().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", pageId));
		}
		if (page.isRoot()) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.move_root"));
		}
		PageEntity refPage = getPageBusiness().getById(refPageId);
		if (refPage == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", refPageId));
		}
		if (type.equals("inside")) {
			String url = getBaseURL(refPage.getFriendlyURL()) + "/" +
				page.getPageFriendlyURL();
			getPageBusiness().move(page, 
					getPageBusiness().makeUniquePageURL(url));
		}
		if (type.equals("after")) {
			String url = getBaseURL(refPage.getParentFriendlyURL()) + "/" +
				page.getPageFriendlyURL();
			getPageBusiness().move(page, 
					getPageBusiness().makeUniquePageURL(url));
			page = getPageBusiness().getById(pageId);
			getPageBusiness().moveAfter(page, refPage);
		}
		if (type.equals("before")) {
			String url = getBaseURL(refPage.getParentFriendlyURL()) + "/" +
				page.getPageFriendlyURL();
			getPageBusiness().move(page, 
					getPageBusiness().makeUniquePageURL(url));
			page = getPageBusiness().getById(pageId);
			getPageBusiness().moveBefore(page, refPage);
		}
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	@Override
	public ServiceResponse copyPage(Long pageId, Long refPageId, String type) {
		PageEntity page = getPageBusiness().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", pageId));
		}
		PageEntity refPage = getPageBusiness().getById(refPageId);
		if (refPage == null) {
			return ServiceResponse.createErrorResponse(
					Messages.get("page.not_found", refPageId));
		}
		if (type.equals("inside")) {
			getPageBusiness().copy(page, refPage.getFriendlyURL());
		}
		if (type.equals("after")) {
			getPageBusiness().copy(page, refPage.getParentFriendlyURL());
			page = getPageBusiness().getById(pageId);
			getPageBusiness().moveAfter(page, refPage);
		}
		if (type.equals("before")) {
			getPageBusiness().copy(page, refPage.getParentFriendlyURL());
			page = getPageBusiness().getById(pageId);
			getPageBusiness().moveBefore(page, refPage);
		}
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}
	
	@Override
	public ServiceResponse resetCache(String url) {
		getBusiness().getSystemService().getPageCache().remove(url);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

	private String getDependencies(String url) {
		StringBuilder s = new StringBuilder();
		int count = 0;
		for (PageDependencyEntity dep : getDao().getPageDependencyDao()
				.selectByPage(url)) {
			if (count++ > 0) {
				s.append(",");
			}
			s.append(dep.getDependency());
		}
		return s.toString();
	}
	
	private void savePageDependencies(String url, String dependencies) {
		List<String> deps = Arrays.asList(dependencies.replace(" ", "")
				.split(","));
		List<String> alreadyExist = new ArrayList<String>();
		for (PageDependencyEntity entity : getDao().getPageDependencyDao()
				.selectByPage(url)) {
			if (!deps.contains(entity.getDependency())) {
				getDao().getPageDependencyDao().remove(entity.getId());
			}
			else {
				alreadyExist.add(entity.getDependency());
			}
		}
		for (String dependency : deps) {
			if (!StringUtils.isEmpty(dependency)
					&& !alreadyExist.contains(dependency)) {
				PageDependencyEntity entity = new PageDependencyEntity(
						dependency, url);
				
				getDao().getPageDependencyDao().save(entity);
			}
		}
	}

	@Override
	public PageEntity getPageDefaultSettings(String url) {
		return getPageBusiness().getPageDefaultSettings(url);
	}

	@Override
	public ServiceResponse saveAttribute(Long id, String name, String value, 
			String language, boolean applyToChildren) {
		PageEntity page = getDao().getPageDao().getById(id);
		if (page == null) {
			return ServiceResponse.createErrorResponse(Messages.get("page.not_found"));
		}
		getBusiness().getPageAttributeBusiness().setAttribute(page, name, 
				language, value, applyToChildren);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}


}
