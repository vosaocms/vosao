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

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.CurrentUser;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.UserEntity;
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
	public ServiceResponse updateContent(String pageId, String content,
			String title, String languageCode, boolean approve) {
		PageEntity page = getBusiness().getPageBusiness().getById(pageId);
		if (page != null) {
			if (!getBusiness().getPageBusiness().canChangeContent(
					page.getFriendlyURL(), languageCode)) {
				return ServiceResponse.createErrorResponse("Access denied");
			}
			UserEntity user = CurrentUser.getInstance();
			ContentPermissionEntity perm = getBusiness()
					.getContentPermissionBusiness().getPermission(
							page.getFriendlyURL(), user);
			if (approve && perm.isPublishGranted()) {
				page.setState(PageState.APPROVED);
			}
			else {
				page.setState(PageState.EDIT);
			}
			page.setModDate(new Date());
			page.setModUserEmail(user.getEmail());
			page.setLocalTitle(title, languageCode);
			getDao().getPageDao().save(page);
			getDao().getPageDao().setContent(pageId, languageCode, content);
			return ServiceResponse.createSuccessResponse(
					"Page content was successfully updated");
		}
		else {
			return ServiceResponse.createErrorResponse("Page not found " 
					+ pageId);
		}
	}

	@Override
	public TreeItemDecorator<PageVO> getTree() {
		List<PageVO> pages = PageVO.create(getBusiness().getPageBusiness()
				.select());
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
		return root;
	}

	@Override
	public PageEntity getPage(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return getBusiness().getPageBusiness().getById(id);
	}

	@Override
	public PageEntity getPageByUrl(String url) {
		return getBusiness().getPageBusiness().getByUrl(url);
	}

	@Override
	public ServiceResponse savePage(Map<String, String> vo) {
		UserEntity user = CurrentUser.getInstance();
		PageEntity page = getPage(vo.get("id"));
		if (page == null) {
			page = new PageEntity();
			page.setCreateUserEmail(user.getEmail());
		}
		page.setModDate(new Date());
		page.setModUserEmail(user.getEmail());
		page.setCommentsEnabled(Boolean.valueOf(vo.get("commentsEnabled")));
		page.setFriendlyURL(vo.get("friendlyUrl"));
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
		if (!perm.isChangeGranted()) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		try {
			page.setPublishDate(DateUtil.toDate(vo.get("publishDate")));
		}
		catch (ParseException e) {
			return ServiceResponse.createErrorResponse("Date is in wrong format");
		}
		page.setTemplate(vo.get("template"));
		page.setTitleValue(vo.get("titles"));
		page.setPageType(PageType.valueOf(vo.get("pageType")));
		page.setStructureId(vo.get("structureId"));
		page.setStructureTemplateId(vo.get("structureTemplateId"));
		page.setKeywords(vo.get("keywords"));
		page.setDescription(vo.get("description"));
		List<String> errors = getBusiness().getPageBusiness()
			.validateBeforeUpdate(page);
		if (errors.isEmpty()) {
			getDao().getPageDao().save(page);
			getDao().getPageDao().setContent(page.getId(), 
					vo.get("languageCode"), vo.get("content"));
			return ServiceResponse.createSuccessResponse(page.getId());
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Errors occured during saving page.", errors);
		}
		
	}

	@Override
	public List<PageVO> getChildren(String url) {
		return PageVO.create(getBusiness().getPageBusiness().getByParent(url));
	}

	@Override
	public ServiceResponse deletePages(List<String> ids) {
		getBusiness().getPageBusiness().remove(ids);
		return ServiceResponse.createSuccessResponse(
				"Pages were successfully deleted");
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
		return getBusiness().getPageBusiness().getContents(pageId);
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
						CurrentUser.getInstance()).getId());
		}
		return ServiceResponse.createErrorResponse("Page not found");
	}

	@Override
	public ServiceResponse approve(String pageId) {
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
	public PageRequestVO getPageRequest(final String id, 
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

}
