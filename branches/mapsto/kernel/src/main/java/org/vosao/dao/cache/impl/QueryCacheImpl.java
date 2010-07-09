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

package org.vosao.dao.cache.impl;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.dao.cache.CacheStat;
import org.vosao.dao.cache.QueryCache;
import org.vosao.global.CacheService;
import org.vosao.global.SystemService;

public class QueryCacheImpl implements QueryCache, Serializable {

	protected static final Log logger = LogFactory.getLog(
			QueryCacheImpl.class);

	private long calls;
	private long hits;
	
	public QueryCacheImpl() {
		calls = 0;
		hits = 0;
	}

	public SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}

	private CacheService getCache() {
		return getSystemService().getCache();
	}

	private String getQueryKey(Class clazz, String query, Object[] params) {
		StringBuffer result = new StringBuffer(clazz.getName());
		result.append(query);
		if (params != null) {
			for (Object param : params) {
				result.append(param != null ? param.toString() : "null"); 
			}
		}
		return result.toString();
	}

	private String getClassResetdateKey(Class clazz) {
		return "classResetDate:" + clazz.getName();
	}
	
	private Date getClassResetDate(Class clazz) {
		return (Date)getCache().get(getClassResetdateKey(clazz));
	}
	
	@Override
	public Object getQuery(Class clazz, String query, Object[] params) {
		try {
			calls++;
			CacheItem item = (CacheItem)getCache().get(getQueryKey(clazz, query, 
					params));
			if (item != null) {
				Date globalResetDate = getCache().getResetDate();
				if (globalResetDate == null 
						|| item.getTimestamp().after(globalResetDate)) {
					Date classResetDate = getClassResetDate(clazz);
					if (classResetDate == null
							|| item.getTimestamp().after(classResetDate)) {
						hits++;
						return item.getData();
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null; 
	}

	@Override
	public void putQuery(Class clazz, String query, Object[] params, 
			Object value) {
		String key = getQueryKey(clazz, query, params);
		getCache().put(key, new CacheItem(value));
	}

	@Override
	public void removeQueries(Class clazz) {
		getCache().put(getClassResetdateKey(clazz), new Date());
	}

	@Override
	public CacheStat getStat() {
		return new CacheStat(calls, hits);
	}
	
}
