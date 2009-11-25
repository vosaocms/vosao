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
import org.vosao.entity.UserEntity;
import org.vosao.enums.PageState;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PageService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.PageVO;
import org.vosao.utils.DateUtil;

public class PageServiceImpl extends AbstractServiceImpl 
		implements PageService {

	private static Log logger = LogFactory.getLog(PageServiceImpl.class);

	@Override
	public ServiceResponse updateContent(String pageId, String content,
			String languageCode, boolean approve) {
		PageEntity page = getBusiness().getPageBusiness().getById(pageId);
		if (page != null) {
			UserEntity user = CurrentUser.getInstance();
			ContentPermissionEntity perm = getBusiness()
				.getContentPermissionBusiness().getPermission(
					page.getFriendlyURL(), CurrentUser.getInstance());
			if (approve && perm.isPublishGranted()) {
				page.setState(PageState.APPROVED);
			}
			else {
				page.setState(PageState.EDIT);
			}
			page.setModDate(new Date());
			page.setModUserEmail(user.getEmail());
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
	public ServiceResponse savePage(Map<String, String> pageMap) {
		UserEntity user = CurrentUser.getInstance();
		PageEntity page = getPage(pageMap.get("id"));
		if (page == null) {
			page = new PageEntity();
			page.setCreateUserEmail(user.getEmail());
		}
		page.setModDate(new Date());
		page.setModUserEmail(user.getEmail());
		page.setCommentsEnabled(Boolean.valueOf(pageMap.get("commentsEnabled")));
		page.setFriendlyURL(pageMap.get("friendlyUrl"));
		ContentPermissionEntity perm = getBusiness()
			.getContentPermissionBusiness().getPermission(
				page.getFriendlyURL(), CurrentUser.getInstance());
		if (Boolean.valueOf(pageMap.get("approve")) 
			&& perm.isPublishGranted()) {
			page.setState(PageState.APPROVED);
		}
		else {
			page.setState(PageState.EDIT);
		}
		if (!perm.isChangeGranted()) {
			return ServiceResponse.createErrorResponse("Access denied");
		}
		try {
			page.setPublishDate(DateUtil.toDate(pageMap.get("publishDate")));
		}
		catch (ParseException e) {
			return ServiceResponse.createErrorResponse("Date is in wrong format");
		}
		page.setTemplate(pageMap.get("template"));
		page.setTitle(pageMap.get("title"));
		List<String> errors = getBusiness().getPageBusiness()
			.validateBeforeUpdate(page);
		if (errors.isEmpty()) {
			getDao().getPageDao().save(page);
			return ServiceResponse.createSuccessResponse(
					"Page was successfully saved.");
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

}
