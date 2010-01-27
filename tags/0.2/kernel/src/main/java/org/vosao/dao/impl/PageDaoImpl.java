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

package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.dao.CommentDao;
import org.vosao.dao.ContentDao;
import org.vosao.dao.PageDao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.PageHelper;

/**
 * @author Alexander Oleynik
 */
public class PageDaoImpl extends BaseDaoImpl<String, PageEntity> 
		implements PageDao {

	private static final String PAGE_CLASS_NAME = PageEntity.class.getName();

	private ContentDao contentDao;
	private CommentDao commentDao;
	
	public PageDaoImpl() {
		super(PageEntity.class);
	}

	@Override
	public void remove(String id) {
		PageEntity page = getById(id);
		if (page != null) {
			List<PageEntity> children = selectAllChildren(page.getFriendlyURL());
			for (PageEntity child : children) {
				remove(child.getId());
			}
			getContentDao().removeById(PAGE_CLASS_NAME, id);
			getCommentDao().removeByPage(page.getFriendlyURL());
		}
		super.remove(id);
	}
	
	private List<PageEntity> selectAllChildren(final String parentUrl) {
		String query = "select from " + PageEntity.class.getName()
			+ " where parentUrl == pParentUrl" 
			+ " parameters String pParentUrl";
		return copy(select(query, params(parentUrl)));
	}
	
	@Override
	public void remove(List<String> ids) {
		for (String id : ids) {
			remove(id);
		}
	}

	@Override
	public List<PageEntity> getByParent(final String url) {
		List<PageEntity> result = filterLatestVersion(selectAllChildren(url));
		Collections.sort(result, new PageHelper.PublishDateDesc());
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
	public String getContent(String pageId, String languageCode) {
		ContentEntity content = getContentDao().getByLanguage(
				PAGE_CLASS_NAME, pageId, languageCode);
		if (content != null) {
			return content.getContent();
		}
		return null;
	}

	@Override
	public void setContent(String pageId, String languageCode, String content) {
		ContentEntity contentEntity = getContentDao().getByLanguage(
				PAGE_CLASS_NAME, pageId, languageCode);
		if (contentEntity == null) {
			contentEntity = new ContentEntity(PAGE_CLASS_NAME, pageId, 
					languageCode, content);
		}
		else {
			contentEntity.setContent(content);
		}
		getContentDao().save(contentEntity);
	}

	@Override
	public ContentDao getContentDao() {
		return contentDao;
	}

	@Override
	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
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
	
	private List<PageEntity> filterApproved(
			List<PageEntity> list) {
		Map<String, PageEntity> pages = new HashMap<String, PageEntity>();
		for (PageEntity page : list) {
			if (page.isApproved()) {
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
		String query = "select from " + PageEntity.class.getName()
			    + " where friendlyURL == pUrl"
			    + " parameters String pUrl";
		List<PageEntity> result = select(query, params(url));
		Collections.sort(result, new PageHelper.VersionAsc());
		return result;
	}
	
	@Override
	public PageEntity getByUrlVersion(final String url, final Integer version) {
		String query = "select from " + PageEntity.class.getName()
			    + " where friendlyURL == pUrl && version == pVersion"
			    + " parameters String pUrl, Integer pVersion";
		return selectOne(query, params(url, version));
	}
	
	@Override
	public List<PageEntity> getByParentApproved(final String url) {
		List<PageEntity> result = filterApproved(selectAllChildren(url));
		Collections.sort(result, new PageHelper.PublishDateDesc());
		return result;
	}

	@Override
	public List<PageEntity> selectByTemplate(String templateId) {
		String query = "select from " + PageEntity.class.getName()
				+ " where template == pTemplate"
				+ " parameters String pTemplate";
		return select(query, params(templateId));
	}

	@Override
	public List<PageEntity> selectByStructure(String structureId) {
		String query = "select from " + PageEntity.class.getName()
				+ " where structureId == pStructureId"
				+ " parameters String pStructureId";
		return select(query, params(structureId));
	}

	@Override
	public List<PageEntity> selectByStructureTemplate(
			String structureTemplateId) {
		String query = "select from " + PageEntity.class.getName()
				+ " where structureTemplateId == pStructureTemplateId"
				+ " parameters String pStructureTemplateId";
		return select(query, params(structureTemplateId));
	}

	public CommentDao getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}
}
