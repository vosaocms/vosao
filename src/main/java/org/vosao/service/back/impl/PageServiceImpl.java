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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.ContentEntity;
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
			String languageCode, HttpServletRequest request) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page != null) {
			UserEntity user = getBusiness().getUserPreferences(request)
				.getUser();
			page.setState(PageState.EDIT);
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
		List<PageVO> pages = PageVO.create(getDao().getPageDao().select());
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
		return getDao().getPageDao().getById(id);
	}

	@Override
	public PageEntity getPageByUrl(String url) {
		return getDao().getPageDao().getByUrl(url);
	}

	@Override
	public ServiceResponse savePage(Map<String, String> pageMap,
			HttpServletRequest request) {
		UserEntity user = getBusiness().getUserPreferences(request).getUser();
		PageEntity page = getPage(pageMap.get("id"));
		if (page == null) {
			page = new PageEntity();
			page.setCreateUserEmail(user.getEmail());
		}
		page.setState(PageState.EDIT);
		page.setModDate(new Date());
		page.setModUserEmail(user.getEmail());
		page.setCommentsEnabled(Boolean.valueOf(pageMap.get("commentsEnabled")));
		page.setFriendlyURL(pageMap.get("friendlyUrl"));
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
		return PageVO.create(getDao().getPageDao().getByParent(url));
	}

	@Override
	public ServiceResponse deletePages(List<String> ids) {
		getDao().getPageDao().remove(ids);
		return ServiceResponse.createSuccessResponse(
				"Pages were successfully deleted");
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
		return getDao().getPageDao().getContents(pageId);
	}

	@Override
	public List<PageVO> getPageVersions(String url) {
		return PageVO.create(getDao().getPageDao().selectByUrl(url));
	}

	@Override
	public String addVersion(String url, String versionTitle, 
			HttpServletRequest request) {
		List<PageEntity> list = getDao().getPageDao().selectByUrl(url);
		UserEntity user = getBusiness().getUserPreferences(request).getUser();
		if (list.size() > 0) {
			PageEntity lastPage = list.get(list.size() - 1);
			return getBusiness().getPageBusiness().addVersion(lastPage, 
					lastPage.getVersion() + 1, versionTitle, user).getId();
		}
		return null;
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
		page.setState(PageState.APPROVED);
		getDao().getPageDao().save(page);
		return ServiceResponse.createSuccessResponse(
				"Page was successfully approved.");
	}

}
