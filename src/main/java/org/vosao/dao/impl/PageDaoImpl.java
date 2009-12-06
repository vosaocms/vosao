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

import javax.jdo.PersistenceManager;

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
	
	public PageDaoImpl() {
		super(PageEntity.class);
	}

	private void removePage(String pageId, PersistenceManager pm) {
		List<ContentEntity> contents = getContentDao().select(
				PAGE_CLASS_NAME, pageId);
		for (ContentEntity content : contents) {
			pm.deletePersistent(pm.getObjectById(ContentEntity.class, 
					content.getId()));
		}
		pm.deletePersistent(pm.getObjectById(PageEntity.class, pageId));
	}
	
	@Override
	public List<PageEntity> getByParent(final String url) {
		String query = "select from " + PageEntity.class.getName()
			    + " where parentUrl == pParentUrl" 
			    + " parameters String pParentUrl";
		List<PageEntity> result = filterLatestVersion(
				select(query, params(url)));
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
		String query = "select from " + PageEntity.class.getName()
			    + " where parentUrl == pParentUrl" 
			    + " parameters String pParentUrl";
		List<PageEntity> result = filterApproved(select(
					query, params(url)));
		Collections.sort(result, new PageHelper.PublishDateDesc());
		return result;
	}
}
