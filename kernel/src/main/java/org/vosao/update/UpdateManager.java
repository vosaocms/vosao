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

import org.vosao.dao.Dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class UpdateManager {

	private List<UpdateTask> tasks;
	private DatastoreService datastore;
	private Dao dao;
	
	public UpdateManager(Dao aDao) {
		dao = aDao;
		datastore = DatastoreServiceFactory.getDatastoreService(); 
		tasks = new ArrayList<UpdateTask>();
		tasks.add(new UpdateTask003());
		tasks.add(new UpdateTask004(dao));
		tasks.add(new UpdateTask01(dao));
		tasks.add(new UpdateTask02(dao));
		tasks.add(new UpdateTask03(dao));
	}
	
	public void update() throws UpdateException {
		if (getConfig().getProperty("version") == null) {
			addConfigVersion();
		}
		for (UpdateTask task : tasks) {
			if (getConfig().getProperty("version").equals(task.getFromVersion())) {
				task.update();
				Entity config = getConfig();
				config.setProperty("version", task.getToVersion());
				datastore.put(config);
			}
		}
	}
	
	private Entity getConfig() {
		Query query = new Query("ConfigEntity");
		return datastore.prepare(query).asIterator().next();
	}
	
	private void addConfigVersion() {
		Entity config = getConfig();
		config.setProperty("version", "0.0.2");
		config.setProperty("enableRecaptcha", false);
		datastore.put(config);
	}
	
}
