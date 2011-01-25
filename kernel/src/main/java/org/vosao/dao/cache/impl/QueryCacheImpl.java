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

package org.vosao.dao.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.dao.DaoStat;
import org.vosao.dao.cache.EntityCache;
import org.vosao.dao.cache.QueryCache;
import org.vosao.entity.BaseEntity;
import org.vosao.global.CacheService;
import org.vosao.global.SystemService;
import org.vosao.utils.EntityUtil;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

public class QueryCacheImpl implements QueryCache, Serializable {

	protected static final Log logger = LogFactory.getLog(
			QueryCacheImpl.class);

	private static DaoStat getDaoStat() {
		return VosaoContext.getInstance().getBusiness().getDao().getDaoStat();
	}

	private EntityCache entityCache;
	
	public QueryCacheImpl(EntityCache anEntityCache) {
		entityCache = anEntityCache;
	}

	public SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}

	private CacheService getCache() {
		return getSystemService().getCache();
	}

	private EntityCache getEntityCache() {
		return entityCache;
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
	public List<BaseEntity> getQuery(Class clazz, String query, 
			Object[] params) {
		try {
			CacheItem item = (CacheItem)getCache().get(getQueryKey(clazz, query, 
					params));
			if (item != null) {
				Date globalResetDate = getCache().getResetDate();
				if (globalResetDate == null 
						|| item.getTimestamp().after(globalResetDate)) {
					Date classResetDate = getClassResetDate(clazz);
					if (classResetDate == null
							|| item.getTimestamp().after(classResetDate)) {
						getDaoStat().incQueryCacheHits();
						List<Long> ids = (List<Long>)item.getData();
						Map<Long, BaseEntity> cached = getEntityCache()
								.getEntities(clazz, ids);
						for (Long id : cached.keySet()) {
							if (cached.get(id) == null) {
								cached.put(id, loadEntity(clazz, id));
							}
							else {
								getDaoStat().incEntityCacheHits();
							}
						}
						List<BaseEntity> result = new ArrayList<BaseEntity>(
								cached.values());
						
						return result;
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null; 
	}

	private BaseEntity loadEntity(Class clazz, Long id) {
		try {
			getDaoStat().incGetCalls();
			Entity entity = getSystemService().getDatastore().get(
					KeyFactory.createKey(EntityUtil.getKind(clazz), id));
			
			BaseEntity model = (BaseEntity)clazz.newInstance();
			model.load(entity);
			return model;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void putQuery(Class clazz, String query, Object[] params, 
			List<BaseEntity> list) {
		String key = getQueryKey(clazz, query, params);
		List<Long> ids = new ArrayList<Long>();
		for (BaseEntity entity : list) {
			ids.add(entity.getId());
		}
		getCache().put(key, new CacheItem(ids));
		getEntityCache().putEntities(clazz, list);
	}

	@Override
	public void removeQueries(Class clazz) {
		getCache().put(getClassResetdateKey(clazz), new Date());
	}

}
