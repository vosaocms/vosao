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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.dao.DaoStat;
import org.vosao.dao.cache.EntityCache;
import org.vosao.entity.BaseEntity;
import org.vosao.global.CacheService;
import org.vosao.global.SystemService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class EntityCacheImpl implements EntityCache, Serializable {

	protected static final Log logger = LogFactory.getLog(
			EntityCacheImpl.class);
	
	private static DaoStat getDaoStat() {
		return VosaoContext.getInstance().getBusiness().getDao().getDaoStat();
	}

	public EntityCacheImpl() {
	}
	
	private String getEntityKey(Class clazz, Object id) {
		return "entity:" + clazz.getName() + id.toString();
	}
	
	@Override
	public Object getEntity(Class clazz, Object id) {
		try {
			CacheItem item = (CacheItem)getCache().get(getEntityKey(clazz, id));
			if (item != null) {
				Date globalResetDate = getCache().getResetDate();
				if (globalResetDate == null 
						|| item.getTimestamp().after(globalResetDate)) {
					getDaoStat().incEntityCacheHits();
					return item.getData();
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Map<Long, BaseEntity> getEntities(Class clazz, List<Long> ids) {
		List<String> keys = new ArrayList<String>(); 
		Map<Long, BaseEntity> result = new HashMap<Long, BaseEntity>();
		for (Long id : ids) {
			keys.add(getEntityKey(clazz, id));
			result.put(id, null);
		}
		try {
			Map items = getCache().getAll(keys);
			for (CacheItem item : (Collection<CacheItem>)items.values()) {
				if (item != null) {
					Date globalResetDate = getCache().getResetDate();
					if (globalResetDate == null 
							|| item.getTimestamp().after(globalResetDate)) {
						getDaoStat().incEntityCacheHits();
						BaseEntity entity = (BaseEntity)item.getData();
						result.put(entity.getId(), entity);
					}
					
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public void putEntity(Class clazz, Object id, Object entity) {
		String key = getEntityKey(clazz, id);
		getCache().put(key, new CacheItem(entity));
	}

	@Override
	public void putEntities(Class clazz, List<BaseEntity> list) {
		Map<String, CacheItem> map = new HashMap<String, CacheItem>(); 
		for (BaseEntity entity : list) {
			map.put(getEntityKey(clazz, entity.getId()), new CacheItem(entity));
		}
		getCache().putAll(map);
	}

	@Override
	public void removeEntity(Class clazz, Object id) {
		getCache().remove(getEntityKey(clazz, id));
	}

	public SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}

	private CacheService getCache() {
		return getSystemService().getCache();
	}

}
