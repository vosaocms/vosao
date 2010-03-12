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

package org.vosao.service.back.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.CurrentUser;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.helper.PageHelper;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
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
import org.vosao.utils.DateUtil;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;

/**
 * @author Alexander Oleynik
 */
public class PageServiceImpl extends AbstractServiceImpl 
		implements PageService {

	private static Log logger = LogFactory.getLog(PageServiceImpl.class);

	private CommentService commentService;
	private TemplateService templateService;
	private LanguageService languageService;
	private ContentPermissionService contentPermissionService;
	private GroupService groupService;

	@Override
	public TreeItemDecorator<PageVO> getTree() {
		List<PageVO> pages = selectLastVersionPages(
				getBusiness().getPageBusiness().select());
		Map<String, TreeItemDecorator<PageVO>> buf = 
				new HashMap<String, TreeItemDecorator<PageVO>>();
		for (PageVO page : pages) {
			buf.put(page.getFriendlyURL(), 
					new TreeItemDecorator<PageVO>(page, null));
		}
		TreeItemDecorator<PageVO> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageVO> page = buf.get(id);
			if (StringUtils.isEmpty(page.getEntity().getParentUrl())) {
				root = page;
			}
			else {
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
						if (o1.getEntity().getSortIndex() > 
							o2.getEntity().getSortIndex()) {
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
			if (pageMap.containsKey(page.getFriendlyURL())) {
				if (pageMap.get(page.getFriendlyURL()).getVersion() 
						< page.getVersion()) {
					pageMap.put(page.getFriendlyURL(), page);
				}
				if (page.isApproved()) {
					published.put(page.getFriendlyURL(), true);
				}
			}
			else {
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
		return getBusiness().getPageBusiness().getById(id);
	}

	@Override
	public PageEntity getPageByUrl(String url) {
		return getBusiness().getPageBusiness().getByUrl(url);
	}

	@Override
	public ServiceResponse savePage(Map<String, String> vo) {
		PageEntity page = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			page = getPage(Long.valueOf(vo.get("id")));
		}
		if (page == null) {
			page = new PageEntity();
			page.setSortIndex(getBusiness().getPageBusiness().getNextSortIndex(
					vo.get("friendlyUrl")));
		}
		if (vo.get("commentsEnabled") != null) {
			page.setCommentsEnabled(Boolean.valueOf(vo.get("commentsEnabled")));
		}
		boolean oldSearchable = page.isSearchable();
		if (vo.get("searchable") != null) {
			page.setSearchable(Boolean.valueOf(vo.get("searchable")));
		}
		if (vo.get("velocityProcessing") != null) {
			page.setVelocityProcessing(Boolean.valueOf(vo.get("velocityProcessing")));
		}
		if (vo.get("friendlyUrl") != null) {
			page.setFriendlyURL(vo.get("friendlyUrl"));
		}
		String languageCode = vo.get("languageCode");
		ContentPermissionEntity perm = getBusiness()
			.getContentPermissionBusiness().getPermission(
				page.getFriendlyURL(), CurrentUser.getInstance());
		boolean approve = Boolean.valueOf(vo.get("approve"));
		if (approve	&& perm.isPublishGranted()) {
			page.setState(PageState.APPROVED);
		}
		else {
			page.setState(PageState.EDIT);
		}
		if (!perm.isChangeGranted(languageCode)) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		if (vo.get("publishDate") != null) {
			try {
				page.setPublishDate(DateUtil.toDate(vo.get("publishDate")));
			}
			catch (ParseException e) {
				return ServiceResponse.createErrorResponse("Date is in wrong format");
			}
		}
		if (vo.get("template") != null) {
			page.setTemplate(Long.valueOf(vo.get("template")));
		}
		if (vo.get("titles") != null) {
			page.setTitleValue(vo.get("titles"));
		}
		if (vo.get("pageType") != null) {
			page.setPageType(PageType.valueOf(vo.get("pageType")));
		}
		if (vo.get("structureId") != null) {
			page.setStructureId(Long.valueOf(vo.get("structureId")));
		}
		if (vo.get("structureTemplateId") != null) {
			page.setStructureTemplateId(Long.valueOf(
					vo.get("structureTemplateId")));
		}
		if (vo.get("keywords") != null) {
			page.setKeywords(vo.get("keywords"));
		}
		if (vo.get("description") != null) {
			page.setDescription(vo.get("description"));
		}
		List<String> errors = getBusiness().getPageBusiness()
			.validateBeforeUpdate(page);
		if (errors.isEmpty()) {
			getDao().getPageDao().save(page);
			if (vo.containsKey("content")) {
				getBusiness().getPageBusiness().saveContent(page, 
					languageCode, vo.get("content"), 
					oldSearchable, page.isSearchable());
			}
			return ServiceResponse.createSuccessResponse(page.getId().toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Errors occured during saving page.", errors);
		}
		
	}

	@Override
	public List<PageVO> getChildren(String url) {
		List<PageEntity> pages = getBusiness().getPageBusiness()
				.getByParent(url);
		Collections.sort(pages, PageHelper.SORT_INDEX_ASC);
		return PageVO.create(pages);
	}

	@Override
	public ServiceResponse deletePages(List<String> ids) {
		getBusiness().getPageBusiness().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				"Pages were successfully deleted");
	}

	@Override
	public ServiceResponse deletePageVersion(Long id) {
		getBusiness().getPageBusiness().removeVersion(id);
		return ServiceResponse.createSuccessResponse(
				"Page was successfully deleted");
	}

	private List<ContentEntity> getContents(Long pageId) {
		return getDao().getPageDao().getContents(pageId);
	}

	@Override
	public List<PageVO> getPageVersions(String url) {
		return PageVO.create(getBusiness().getPageBusiness().selectByUrl(url));
	}

	@Override
	public ServiceResponse addVersion(String url, String versionTitle) {
		if (!getBusiness().getContentPermissionBusiness().getPermission(
				url, CurrentUser.getInstance()).isChangeGranted()) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		List<PageEntity> list = getBusiness().getPageBusiness().selectByUrl(url);
		if (list.size() > 0) {
			PageEntity lastPage = list.get(list.size() - 1);
			return ServiceResponse.createSuccessResponse(
				getBusiness().getPageBusiness().addVersion(
					lastPage, lastPage.getVersion() + 1, versionTitle, 
						CurrentUser.getInstance()).getId().toString());
		}
		return ServiceResponse.createErrorResponse("Page not found");
	}

	@Override
	public ServiceResponse approve(Long pageId) {
		if (pageId == null) {
			return ServiceResponse.createErrorResponse("Page not found");
		}
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse("Page not found");
		}
		if (!getBusiness().getContentPermissionBusiness().getPermission(
				page.getFriendlyURL(), CurrentUser.getInstance())
					.isPublishGranted()) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		page.setState(PageState.APPROVED);
		getDao().getPageDao().save(page);
		return ServiceResponse.createSuccessResponse(
				"Page was successfully approved.");
	}

	@Override
	public PageRequestVO getPageRequest(final Long id, 
			final String parentUrl) {
		try {
		PageRequestVO result = new PageRequestVO();
		result.setPage(getPage(id));
		String permUrl = parentUrl;
		if (result.getPage() != null) {
			String url = result.getPage().getFriendlyURL();
			result.setVersions(getPageVersions(url));
			result.setChildren(getChildren(url));
			result.setComments(getCommentService().getByPage(url));
			result.setContents(getContents(id));
			result.setPermissions(getContentPermissionService().selectByUrl(
					url));
			permUrl = result.getPage().getFriendlyURL();
			if (result.getPage().isStructured()) {
				StructureEntity structure = getDao().getStructureDao().getById(
						result.getPage().getStructureId());
				if (structure != null) {
					result.setStructureFields(structure.getFields());
				}
			}
		}
		result.setTemplates(getTemplateService().getTemplates());
		result.setLanguages(getLanguageService().select());
		result.setGroups(getGroupService().select());
		result.setPagePermission(getContentPermissionService().getPermission(
				permUrl));
		result.setStructures(getDao().getStructureDao().select());
		return result;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public CommentService getCommentService() {
		return commentService;
	}

	@Override
	public void setCommentService(CommentService bean) {
		commentService = bean;		
	}

	@Override
	public LanguageService getLanguageService() {
		return languageService;
	}

	@Override
	public TemplateService getTemplateService() {
		return templateService;
	}

	@Override
	public void setLanguageService(LanguageService bean) {
		languageService = bean;	
	}

	@Override
	public void setTemplateService(TemplateService bean) {
		templateService = bean;
	}

	@Override
	public ContentPermissionService getContentPermissionService() {
		return contentPermissionService;
	}

	@Override
	public void setContentPermissionService(ContentPermissionService bean) {
		contentPermissionService = bean;		
	}

	@Override
	public GroupService getGroupService() {
		return groupService;
	}

	@Override
	public void setGroupService(GroupService bean) {
		groupService = bean;
	}

	private String loadResource(final String url) {
		try {
			return StreamUtil.getTextResource(url);
		}
		catch(IOException e) {
			logger.error("Can't read comments template." + e);
			return "Error during load resources " + url;
		}
	}

	@Override
	public ServiceResponse restore(Long pageId, String pageType, 
			String language) {
		PageEntity page = getPage(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse("Page not found");
		}
		String content = getPredefinedContent(pageType);
		if (content == null) {
			return ServiceResponse.createErrorResponse("Wrong page type");
		}
		if (page.isStructured()) {
			return ServiceResponse.createErrorResponse("Change page type to Simle from Structured first");
		}
		page.setModDate(new Date());
		page.setModUserEmail(CurrentUser.getInstance().getEmail());
		ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness().getPermission(
					page.getFriendlyURL(), CurrentUser.getInstance());
		page.setState(PageState.EDIT);
		if (!perm.isChangeGranted()) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		getDao().getPageDao().save(page);
		getBusiness().getPageBusiness().saveContent(page, language, content,
				page.isSearchable(), page.isSearchable());
		return ServiceResponse.createSuccessResponse(
				"Page successfully restored.");
	}

	private String getPredefinedContent(String pageType) {
		String result = null;
		if (pageType.equals("home")) {
			result = loadResource(SetupBeanImpl.HOME_PAGE_FILE);
		}
		else if (pageType.equals("login")) {
			result = loadResource(SetupBeanImpl.LOGIN_PAGE_FILE);
		}
		else if (pageType.equals("search")) {
			result = loadResource(SetupBeanImpl.SEARCH_PAGE_FILE);
		}
		return result;
	}

	@Override
	public ServiceResponse moveDown(Long pageId) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse("Page not found");
		}
		getBusiness().getPageBusiness().moveDown(page);
		return ServiceResponse.createSuccessResponse("Success");
	}

	@Override
	public ServiceResponse moveUp(Long pageId) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return ServiceResponse.createErrorResponse("Page not found");
		}
		getBusiness().getPageBusiness().moveUp(page);
		return ServiceResponse.createSuccessResponse("Success");
	}
	
}
