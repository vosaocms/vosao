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

import java.util.Collections;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.vosao.dao.BaseDao;
import org.vosao.dao.DaoAction;
import org.vosao.dao.DaoActionOne;
import org.vosao.dao.cache.EntityCache;
import org.vosao.dao.cache.QueryCache;
import org.vosao.entity.BaseEntity;

/**
 * @author Alexander Oleynik
 */
public class BaseDaoImpl<K,T extends BaseEntity> extends AbstractDaoImpl 
		implements BaseDao<K,T> {

	private Class clazz;
	private EntityCache entityCache;
	private QueryCache queryCache;
	
	public BaseDaoImpl(Class aClass) {
		clazz = aClass;
	}

	@Override
	public T save(final T entity) {
		getQueryCache().removeQueries(clazz);
		if (entity.getEntityId() != null) {
			getEntityCache().removeEntity(clazz, entity.getEntityId());
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.makePersistent(entity);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public T getById(final K id) {
		if (id == null) {
			return null;
		}
		T entity = (T) getEntityCache().getEntity(clazz, id);
		if (entity == null) {
			PersistenceManager pm = getPersistenceManager();
			pm.setDetachAllOnCommit(true);
			try {
				entity = (T)pm.getObjectById(clazz, id);
				getEntityCache().putEntity(clazz, id, entity);
			}
			catch (JDOObjectNotFoundException e) {
				entity = null;
			}
			catch (IllegalArgumentException e) {
				entity = null;
			}
			finally {
				pm.close();
			}
		}
		return entity;
	}
	
	@Override
	public void remove(final K id) {
		if (id == null) {
			return;
		}
		getEntityCache().removeEntity(clazz, id);
		getQueryCache().removeQueries(clazz);
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(clazz, id));
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final List<K> ids) {
		getQueryCache().removeQueries(clazz);
		PersistenceManager pm = getPersistenceManager();
		try {
			for (K id : ids) {
				if (id != null) {
					getEntityCache().removeEntity(clazz, id);
					pm.deletePersistent(pm.getObjectById(clazz, id));
				}
			}
		}
		finally {
			pm.close();
		}
	}

	protected List<T> select(String query, Object[] params) {
		List<T> result = (List<T>) getQueryCache().getQuery(clazz, query, params);
		if (result == null) {
			PersistenceManager pm = getPersistenceManager();
			pm.setDetachAllOnCommit(true);
			try {
				result =  copy((List<T>) pm.newQuery(query)
						.executeWithArray(params));
				getQueryCache().putQuery(clazz, query, params, result);
			}
			finally {
				pm.close();
			}
		}
		return result;
	}

	@Override
	public List<T> select() {
		List<T> result = (List<T>) getQueryCache().getQuery(clazz, 
				clazz.getName(), null);
		if (result == null) {
			PersistenceManager pm = getPersistenceManager();
			pm.setDetachAllOnCommit(true);
			try {
				result = copy((List<T>)pm.newQuery("select from " + clazz.getName())
						.execute());
				getQueryCache().putQuery(clazz, clazz.getName(), null, result);
			}
			finally {
				pm.close();
			}
		}
		return result;
	}

	protected T selectOne(String query, Object[] params) {
		List<T> list = select(query, params);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
		
	protected Object[] params(Object...objects) {
		return objects;
	}

	protected List<T> select(DaoAction<T> action) {
		getQueryCache().removeQueries(clazz);
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return copy(action.execute(pm));
		}
		finally {
			pm.close();
		}
	}

	protected T selectOne(DaoActionOne<T> action) {
		getQueryCache().removeQueries(clazz);
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return action.execute(pm);
		}
		finally {
			pm.close();
		}
	}

	public EntityCache getEntityCache() {
		return entityCache;
	}

	public void setEntityCache(EntityCache bean) {
		this.entityCache = bean;
	}

	public QueryCache getQueryCache() {
		return queryCache;
	}

	public void setQueryCache(QueryCache queryCache) {
		this.queryCache = queryCache;
	}
	
}
