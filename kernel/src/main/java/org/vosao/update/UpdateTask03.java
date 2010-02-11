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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.BCrypt;
import org.vosao.dao.Dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

/**
 * @author Alexander Oleynik
 */
public class UpdateTask03 implements UpdateTask {

	private static final Log logger = LogFactory.getLog(UpdateTask02.class);

	private Dao dao;
	private DatastoreService datastore;
	
	private Dao getDao() {
		return dao;
	}
	
	public UpdateTask03(Dao aDao) {
		dao = aDao;
	}
	
	@Override
	public String getFromVersion() {
		return "0.2";
	}

	@Override
	public String getToVersion() {
		return "0.3";
	}

	@Override
	public void update() throws UpdateException {
		datastore = DatastoreServiceFactory.getDatastoreService();
		dao.clearCache();
		updatePages();
		updateUsers();
	}

	private void updatePages() {
		Query query = new Query("PageEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("keywords", new Text(""));
			e.setProperty("description", new Text(""));
			String title = "en" + (String)e.getProperty("title");
			e.setProperty("title", new Text(title));
			datastore.put(e);
		}
	}

	private void updateUsers() {
		Query query = new Query("UserEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("password", BCrypt.hashpw(
					(String)e.getProperty("password"), BCrypt.gensalt()));
			datastore.put(e);
		}
	}
	
}
