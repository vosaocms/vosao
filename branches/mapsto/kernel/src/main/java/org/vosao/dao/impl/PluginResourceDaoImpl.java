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

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.PluginResourceDao;
import org.vosao.entity.PluginResourceEntity;

public class PluginResourceDaoImpl extends 
		BaseMapstoDaoImpl<PluginResourceEntity> implements PluginResourceDao {

	public PluginResourceDaoImpl() {
		super("PluginResourceEntity");
	}

	@Override
	public PluginResourceEntity getByUrl(String plugin, String url) {
		Query<PluginResourceEntity> q = newQuery();
		q.addFilter("url", Filter.EQUAL, url);
		q.addFilter("pluginName", Filter.EQUAL, plugin);
		return q.selectOne("getByUrl", params(url));
	}

}
