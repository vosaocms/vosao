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

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.enums.ContentPermissionType;
import org.vosao.enums.FolderPermissionType;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class UpdateTask02 implements UpdateTask {

	private static final Log logger = LogFactory.getLog(UpdateTask02.class);

	private Dao dao;
	private DatastoreService datastore;
	
	private Dao getDao() {
		return dao;
	}
	
	public UpdateTask02(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public String getFromVersion() {
		return "0.1";
	}

	@Override
	public String getToVersion() {
		return "0.2";
	}

	@Override
	public void update() throws UpdateException {
		datastore = DatastoreServiceFactory.getDatastoreService();
		updatePages();
	}

	private void updatePages() {
		Query query = new Query("PageEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("pageType", PageType.SIMPLE.name());
			e.setProperty("structureId", "");
			e.setProperty("structureTemplateId", "");
			datastore.put(e);
		}
	}

}
