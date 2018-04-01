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

package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.vosao.common.VosaoContext;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.CommentDao;
import org.vosao.dao.ContentDao;
import org.vosao.dao.PageDao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.PageHelper;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * @author Alexander Oleynik
 */
public class PageDaoImpl extends BaseDaoImpl<PageEntity> 
		implements PageDao {

	private static final String PAGE_CLASS_NAME = PageEntity.class.getName();

	public PageDaoImpl() {
		super(PageEntity.class);
	}

	@Override
	public void remove(Long id) {
		PageEntity page = getById(id);
		if (page != null) {
			List<PageEntity> versions = selectByUrl(page.getFriendlyURL());
			for (PageEntity version : versions) {
				removeVersion(version.getId());
			}
			List<PageEntity> children = selectAllChildren(page.getFriendlyURL());
			for (PageEntity child : children) {
				remove(child.getId());
			}
			getCommentDao().removeByPage(page.getFriendlyURL());
		}
	}
	
	@Override
	public void removeVersion(Long id) {
		PageEntity page = getById(id);
		if (page != null) {
			getContentDao().removeById(PAGE_CLASS_NAME, id);
			super.remove(id);
		}
	}

	public List<PageEntity> selectAllChildren(final String parentUrl) {
		Query q = newQuery();
		q.addFilter("parentUrl", FilterOperator.EQUAL, parentUrl);
		return select(q, "selectAllChildren", params(parentUrl));
	}
	
	public List<PageEntity> selectAllChildren(final String parentUrl,
			Date startDate, Date endDate) {
		Query q = newQuery();
		q.addFilter("parentUrl", FilterOperator.EQUAL, parentUrl);
		q.addFilter("publishDate", FilterOperator.GREATER_THAN_OR_EQUAL, 
				startDate);
		q.addFilter("publishDate", FilterOperator.LESS_THAN, endDate);
		return select(q, "selectAllChildrenDate", params(parentUrl, startDate,
				endDate));
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}

	@Override
	public List<PageEntity> getByParent(final String url) {
		List<PageEntity> result = filterLatestVersion(selectAllChildren(url));
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}

	@Override
	public PageEntity getByUrl(final String url) {
		List<PageEntity> result = filterApproved(selectByUrl(url));
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public String getContent(Long pageId, String languageCode) {
		ContentEntity content = getContentDao().getByLanguage(
				PAGE_CLASS_NAME, pageId, languageCode);
		if (content != null) {
			return content.getContent();
		}
		return null;
	}

	@Override
	public ContentEntity setContent(Long pageId, String languageCode, 
				String content) {
		ContentEntity contentEntity = getContentDao().getByLanguage(
				PAGE_CLASS_NAME, pageId, languageCode);
		if (contentEntity == null) {
			contentEntity = new ContentEntity(PAGE_CLASS_NAME, pageId, 
					languageCode, content);
		}
		else {
			contentEntity.setContent(content);
		}
		return getContentDao().save(contentEntity);
	}

	private ContentDao getContentDao() {
		return VosaoContext.getInstance().getBusiness().getDao().getContentDao();
	}

	@Override
	public List<ContentEntity> getContents(Long pageId) {
		return getContentDao().select(PAGE_CLASS_NAME, pageId);
	}
	
	private List<PageEntity> filterLatestVersion(List<PageEntity> list) {
		Map<String, PageEntity> pages = new HashMap<String, PageEntity>();
		for (PageEntity page : list) {
			String key = page.getFriendlyURL();
			if (!pages.containsKey(key)
					|| pages.get(key).getVersion() < page.getVersion()) {
				pages.put(key, page);
			}
		}
		List<PageEntity> result = new ArrayList<PageEntity>();
		result.addAll(pages.values());
		return result;
	}
	
	private List<PageEntity> filterApproved(List<PageEntity> list) {
		Map<String, PageEntity> pages = new HashMap<String, PageEntity>();
		for (PageEntity page : list) {
			if (page.isApproved() && !page.isForInternalUse()
					&& page.isPublished()) {
				String key = page.getFriendlyURL();
				if (!pages.containsKey(key)
					|| pages.get(key).getVersion() < page.getVersion()) {
					pages.put(key, page);
				}
			}
		}
		List<PageEntity> result = new ArrayList<PageEntity>();
		result.addAll(pages.values());
		return result;
	}
	
	@Override
	public List<PageEntity> selectByUrl(final String url) {
		Query q = newQuery();
		q.addFilter("friendlyURL", FilterOperator.EQUAL, url);
		List<PageEntity> result = select(q, "selectByUrl", params(url));
		Collections.sort(result, PageHelper.VERSION_ASC);
		return result;
	}
	
	@Override
	public PageEntity getByUrlVersion(final String url, final Integer version) {
		Query q = newQuery();
		q.addFilter("friendlyURL", FilterOperator.EQUAL, url);
		q.addFilter("version", FilterOperator.EQUAL, version);
		return selectOne(q, "getByUrlVersion", params(url, version));
	}
	
	@Override
	public List<PageEntity> getByParentApproved(final String url) {
		List<PageEntity> result = filterApproved(selectAllChildren(url));
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}

	@Override
	public List<PageEntity> getByParentApproved(final String url, 
			Date startDate, Date endDate) {
		List<PageEntity> result = filterApproved(selectAllChildren(url,
				startDate, endDate));
		Collections.sort(result, PageHelper.PUBLISH_DATE);
		return result;
	}

	@Override
	public List<PageEntity> selectByTemplate(Long templateId) {
		Query q = newQuery();
		q.addFilter("template", FilterOperator.EQUAL, templateId);
		return select(q, "selectByTemplate", params(templateId));
	}

	@Override
	public List<PageEntity> selectByStructure(Long structureId) {
		Query q = newQuery();
		q.addFilter("structureId", FilterOperator.EQUAL, structureId);
		return select(q, "selectByStructure", params(structureId));
	}

	@Override
	public List<PageEntity> selectByStructureTemplate(
			Long structureTemplateId) {
		Query q = newQuery();
		q.addFilter("structureTemplateId", FilterOperator.EQUAL, 
				structureTemplateId);
		return select(q, "selectByStructureTemplate", 
				params(structureTemplateId));
	}

	private CommentDao getCommentDao() {
		return VosaoContext.getInstance().getBusiness().getDao().getCommentDao();
	}

	@Override
	public List<PageEntity> getCurrentHourPublishedPages() {
		Date endDate = new Date();
		Date startDate = DateUtils.addHours(endDate, -1);
		Query q = newQuery();
		q.addFilter("publishDate", FilterOperator.GREATER_THAN_OR_EQUAL, 
				startDate);
		q.addFilter("publishDate", FilterOperator.LESS_THAN_OR_EQUAL, 
				endDate);
		return select(q, "getCurrentHourPublishedPages", 
				params(startDate, endDate));
	}
	
	@Override
	public List<PageEntity> getCurrentHourUnpublishedPages() {
		Date endDate = new Date();
		Date startDate = DateUtils.addHours(endDate, -1);
		Query q = newQuery();
		q.addFilter("endPublishDate", FilterOperator.GREATER_THAN_OR_EQUAL, 
				startDate);
		q.addFilter("endPublishDate", FilterOperator.LESS_THAN_OR_EQUAL, 
				endDate);
		return select(q, "getCurrentHourUnpublishedPages", 
				params(startDate, endDate));
	}
	
}
