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

package org.vosao.dao;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.dao.cache.EntityCache;
import org.vosao.dao.cache.QueryCache;
import org.vosao.global.SystemService;

public class AbstractDaoImpl implements AbstractDao, Serializable {

	protected static final Log logger = LogFactory.getLog(AbstractDaoImpl.class);

	private EntityCache entityCache;
	private QueryCache queryCache;
	private SystemService systemService;

	public EntityCache getEntityCache() {
		return entityCache;
	}

	public void setEntityCache(EntityCache entityCache) {
		this.entityCache = entityCache;
	}

	public QueryCache getQueryCache() {
		return queryCache;
	}

	public void setQueryCache(QueryCache queryCache) {
		this.queryCache = queryCache;
	}

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

}
