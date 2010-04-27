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

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.PluginDao;
import org.vosao.entity.PluginEntity;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class PluginDaoImpl extends BaseDaoImpl<PluginEntity> 
		implements PluginDao {

	public PluginDaoImpl() {
		super(PluginEntity.class);
	}

	@Override
	public PluginEntity getByName(final String name) {
		Query q = newQuery();
		q.addFilter("name", FilterOperator.EQUAL, name);
		return selectOne(q, "getByName", params(name));
	}
	
	@Override
	public List<PluginEntity> selectEnabled() {
		Query q = newQuery();
		q.addFilter("disabled", FilterOperator.EQUAL, false);
		return select(q, "selectEnabled", params(false));
	}

}
