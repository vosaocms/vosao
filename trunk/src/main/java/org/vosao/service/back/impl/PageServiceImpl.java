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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.PageEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PageService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.PageVO;
import org.vosao.utils.DateUtil;

public class PageServiceImpl extends AbstractServiceImpl 
		implements PageService {

	private static Log logger = LogFactory.getLog(PageServiceImpl.class);

	@Override
	public ServiceResponse updateContent(String pageId, String content) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page != null) {
			page.setContent(content);
			getDao().getPageDao().save(page);
			return ServiceResponse.createSuccessResponse(
					"Page was successfully updated");
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
			buf.put(page.getId(), new TreeItemDecorator<PageVO>(page, null));
		}
		TreeItemDecorator<PageVO> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<PageVO> page = buf.get(id);
			if (page.getEntity().getParent() == null) {
				root = page;
			}
			else {
				TreeItemDecorator<PageVO> parent = buf.get(page.getEntity()
						.getParent());
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
	public ServiceResponse savePage(Map<String, String> pageMap) {
		logger.info(pageMap.toString());
		
		PageEntity page = getPage(pageMap.get("id"));
		if (page == null) {
			page = new PageEntity();
		}
		page.setCommentsEnabled(Boolean.valueOf(pageMap.get("commentsEnabled")));
		page.setContent(pageMap.get("content"));
		page.setFriendlyURL(pageMap.get("friendlyUrl"));
		page.setParent(pageMap.get("parent"));
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
	public List<PageVO> getChildren(String id) {
		return PageVO.create(getDao().getPageDao().getByParent(id));
	}

	@Override
	public ServiceResponse deletePages(List<String> ids) {
		getDao().getPageDao().remove(ids);
		return ServiceResponse.createSuccessResponse(
				"Pages were successfully deleted");
	}

}
