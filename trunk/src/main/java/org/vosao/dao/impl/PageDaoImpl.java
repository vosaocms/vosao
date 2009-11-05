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

import java.util.List;

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.ContentDao;
import org.vosao.dao.PageDao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;

public class PageDaoImpl extends AbstractDaoImpl implements PageDao {

	private static final String PAGE_CLASS_NAME = PageEntity.class.getName();

	private ContentDao contentDao;
	
	public void save(final PageEntity page) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (page.getId() != null) {
				PageEntity p = pm.getObjectById(PageEntity.class, page.getId());
				p.copy(page);
			}
			else {
				pm.makePersistent(page);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public PageEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(PageEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<PageEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName();
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			removePage(id, pm);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				removePage(id, pm);
			}
		}
		finally {
			pm.close();
		}
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
	
	public List<PageEntity> getByParent(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName()
			    + " where parent == pParent" 
			    + " parameters String pParent";
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query)
				.execute(id);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	public PageEntity getByUrl(final String url) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName()
			    + " where friendlyURL == pUrl parameters String pUrl";
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query)
				.execute(url);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
		finally {
			pm.close();
		}
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

	public ContentDao getContentDao() {
		return contentDao;
	}

	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

	@Override
	public List<ContentEntity> getContents(String pageId) {
		return getContentDao().select(PAGE_CLASS_NAME, pageId);
	}
	
}
