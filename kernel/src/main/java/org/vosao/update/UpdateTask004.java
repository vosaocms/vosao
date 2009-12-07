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

package org.vosao.update;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.enums.PageState;
import org.vosao.utils.UrlUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

/**
 * @author Alexander Oleynik
 */
public class UpdateTask004 implements UpdateTask {

	private static final Log logger = LogFactory.getLog(UpdateTask004.class);

	private Dao dao;
	private DatastoreService datastore;
	
	private Dao getDao() {
		return dao;
	}
	
	public UpdateTask004(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public String getFromVersion() {
		return "0.0.3";
	}

	@Override
	public String getToVersion() {
		return "0.0.4";
	}

	@Override
	public void update() throws UpdateException {
		datastore = DatastoreServiceFactory.getDatastoreService(); 
		addEngLanguage();
		updateComments();
		updatePages();
	}

	private void addEngLanguage() {
		LanguageEntity lang = getDao().getLanguageDao().getByCode(
				LanguageEntity.ENGLISH_CODE);
		if (lang == null) {
			lang = new LanguageEntity();
			lang.setCode(LanguageEntity.ENGLISH_CODE);
			lang.setTitle(LanguageEntity.ENGLISH_TITLE);
			getDao().getLanguageDao().save(lang);
		}
	}
	
	private void updatePages() {
		Query query = new Query("PageEntity");
		String userEmail = "admin@test.com";
		for (Entity e : datastore.prepare(query).asIterable()) {
			String pageId = KeyFactory.keyToString(e.getKey());
			ContentEntity contentEntity = new ContentEntity(
					PageEntity.class.getName(),
					pageId, 
					LanguageEntity.ENGLISH_CODE, 
					((Text) e.getProperty("content")).getValue());
			getDao().getContentDao().save(contentEntity);
			e.removeProperty("content");
			e.setProperty("version", new Integer(1));
			e.setProperty("versionTitle", "1 version");
			e.setProperty("state", PageState.APPROVED.name());
			e.setProperty("createUserEmail", userEmail);
			e.setProperty("modUserEmail", userEmail);
			Date dt = (Date) e.getProperty("publishDate");
			if (dt == null) {
				dt = new Date();
			}
			e.setProperty("createDate", dt);
			e.setProperty("modDate", dt);
			String friendlyUrl = (String) e.getProperty("friendlyURL");
			if (friendlyUrl.equals("/")) {
				e.setProperty("parentUrl", null);
			}
			else {
				e.setProperty("parentUrl", UrlUtil.getParentFriendlyURL(
						friendlyUrl));
			}
			datastore.put(e);
		}
	}
	
	private String getPageUrl(String id) {
		try {
			Entity e = datastore.get(KeyFactory.stringToKey(id));
			return (String)e.getProperty("friendlyURL"); 
		} catch (EntityNotFoundException e1) {
			logger.error("page not found " + id);
			return null;
		}
	}
	
	private void updateComments() {
		Query query = new Query("CommentEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			String url = getPageUrl((String)e.getProperty("pageId"));
			e.setProperty("pageUrl", url);
			e.removeProperty("pageId");
			datastore.put(e);
		}
	}

}
