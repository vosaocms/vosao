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

package org.vosao.update;

import java.util.Map;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class UpdateTask08 implements UpdateTask {

	private Business business;
	
	public UpdateTask08(Business aBusiness) {
		business = aBusiness;
	}
	
	private Dao getDao() {
		return business.getDao();
	}
	
	private Business getBusiness() {
		return business;
	}

	@Override
	public String getFromVersion() {
		return "0.7";
	}

	@Override
	public String getToVersion() {
		return "0.8";
	}

	@Override
	public String update() throws UpdateException {
		updatePlugins();
		updateConfig();
		return "Successfully updated to 0.8 version.";
	}

	private static final String ATTRIBUTES = "attributes";
	
	private void updateConfig() {
		Query query = new Query("ConfigEntity");
		for (Entity e : getBusiness().getSystemService().getDatastore()
				.prepare(query).asIterable()) {
			String attributes = convertAttributes(
					(Map<String, String>)e.getProperty(ATTRIBUTES));
			e.removeProperty(ATTRIBUTES);
			e.setProperty(ATTRIBUTES, attributes);
			getBusiness().getSystemService().getDatastore().put(e);
		}
	}

	private String convertAttributes(Map<String, String> attributes) {
		return new JSONObject(attributes).toString();
	}
	
	private void updatePlugins() {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			plugin.setDisabled(true);
			getDao().getPluginDao().save(plugin);
		}
	}

}
