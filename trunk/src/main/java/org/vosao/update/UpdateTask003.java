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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class UpdateTask003 implements UpdateTask {

	@Override
	public String getFromVersion() {
		return "0.0.2";
	}

	@Override
	public String getToVersion() {
		return "0.0.3";
	}

	@Override
	public void update() throws UpdateException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService(); 
		Query query = new Query("FormEntity");
		for (Entity e : datastore.prepare(query).asIterable()) {
			e.setProperty("sendButtonTitle", "");
			e.setProperty("showResetButton", false);
			e.setProperty("resetButtonTitle", "");
			e.setProperty("enableCaptcha", false);
			datastore.put(e);
		}
		
	}

}
