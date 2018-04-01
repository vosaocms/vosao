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

package org.vosao.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.entity.BaseEntity;
import org.vosao.utils.EntityUtil;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class BaseDaoImpl<T extends BaseEntity> 
		extends AbstractDaoImpl implements BaseDao<T>{
	
	protected static final Log logger = LogFactory.getLog(
			BaseDaoImpl.class);

	private static int QUERY_LIMIT = 100000;
	private static int CHUNK_SIZE = 1000;
	
	private Class clazz;
	private String kind;

	public BaseDaoImpl(Class aClass) {
		clazz = aClass;
		kind = createKind();
	}
	
	public BaseDaoImpl(Class aClass, String aKind) {
		clazz = aClass;
		kind = aKind;
	}

	private DatastoreService getDatastore() {
		return getSystemService().getDatastore();
	}

	@Override
	public void clearCache() {
		try {
			getQueryCache().removeQueries(clazz);
		}
		catch (Exception e) {
			logger.error("clearCache " + clazz.getName() + " " + e.getMessage());
		}
	}

	@Override
	public T getById(Long id) {
		if (id == null || id <= 0) {
			return null;
		}
		T model = (T) getEntityCache().getEntity(clazz, id);
		if (model != null) {
			return model;
		}
		try {
			getDao().getDaoStat().incGetCalls();
			model = createModel(getDatastore().get(getKey(id)));
			getEntityCache().putEntity(clazz, id, model);
			return model;
		}
		catch (EntityNotFoundException e) {
			return null;
		}
	}

	@Override
	public List<T> getById(List<Long> ids) {
		if (ids == null) {
			return Collections.EMPTY_LIST;
		}
		List<Key> keys = new ArrayList<Key>();
		List<T> result = new ArrayList<T>();
		for (Long id : ids) {
			if (id != null && id > 0) {
				T model = (T) getEntityCache().getEntity(clazz, id);
				if (model != null) {
					result.add(model);
				}
				else {
					keys.add(getKey(id));
				}
			}
		}
		getDao().getDaoStat().incGetCalls();
		List<T> models = createModels(getDatastore().get(keys).values());
		for (T model : models) {
			getEntityCache().putEntity(clazz, model.getId(), model);
		}
		result.addAll(models);
		return result;
	}

	private T createModel(Entity entity) {
		try {
			T model = (T)clazz.newInstance();
			model.load(entity);
			return model;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void remove(Long id) {
		if (id == null) {
			return;
		}
		getEntityCache().removeEntity(clazz, id);
		getQueryCache().removeQueries(clazz);
		getDatastore().delete(getKey(id));
	}

	@Override
	public void remove(List<Long> ids) {
		for (Long id : ids) {
			getEntityCache().removeEntity(clazz, id);
		}
		getQueryCache().removeQueries(clazz);
		getDatastore().delete(getKeys(ids));
	}

	@Override
	public void removeAll() {
		Query q = newQuery();
		removeSelected(q);
	}

	protected void removeSelected(Query q) {
		getQueryCache().removeQueries(clazz);
		getDao().getDaoStat().incQueryCalls();
		PreparedQuery p = getDatastore().prepare(q);
		List<Key> keys = new ArrayList<Key>();
		int count = 0;
		for (Entity entity : p.asIterable(FetchOptions.Builder.withChunkSize(CHUNK_SIZE))) {
			keys.add(entity.getKey());
			// GAE Datastore one call delete limit
			if (count++ >= 499) {
				getDatastore().delete(keys);
				keys.clear();
				count = 0;
			}
		}
		getDatastore().delete(keys);
	}
	
	@Override
	public T saveNoAudit(T model) {
		return save(model, false);
	}
	
	@Override
	public T save(T model) {
		return save(model, true);
	}
		
	private T save(T model, boolean audit) {
		getQueryCache().removeQueries(clazz);
		Entity entity = null;
		if (model.getId() != null) {
			try {
				getDao().getDaoStat().incGetCalls();
				entity = getDatastore().get(getKey(model.getId()));
				getEntityCache().removeEntity(clazz, model.getId());
			}
			catch (EntityNotFoundException e) {
				logger.error("Entity not found " + clazz.getName() + " " 
						+ model.getId());
			}
		}
		if (entity == null) {
			entity = new Entity(getKind());
			model.setCreateUserEmail(getCurrentUserEmail());
		}
		if (audit) {
			model.setModDate(new Date());
			model.setModUserEmail(getCurrentUserEmail());
		}
		model.save(entity);
		getDatastore().put(entity);
		model.setKey(entity.getKey());
		return model;
	}

	@Override
	public List<T> select() {
		List<T> result = (List<T>) getQueryCache().getQuery(clazz, 
				clazz.getName(), null);
		if (result == null) {
			Query q = newQuery();
			result = selectNotCache(q);
			getQueryCache().putQuery(clazz, clazz.getName(), null, 
					(List<BaseEntity>)result);
		}
		return result;
	}

	private List<T> createModels(Collection<Entity> list) {
		List<T> result = new ArrayList<T>();
		for (Entity entity : list) {
			result.add(createModel(entity));
		}
		return result;
	}
	
	@Override
	public Key getKey(Long id) {
		return KeyFactory.createKey(getKind(), (Long)id);
	}

	@Override
	public List<Key> getKeys(List<Long> ids) {
		List<Key> result = new ArrayList<Key>();
		for (Long id : ids) {
			result.add(getKey(id));
		}
		return result;
	}
	
	protected List<T> select(Query query, String queryId, Object[] params) {
		return select(query, queryId, QUERY_LIMIT, params);
	}

	protected List<T> select(Query query, String queryId, int queryLimit, 
			Object[] params) {
		List<T> result = (List<T>) getQueryCache().getQuery(clazz, queryId, 
				params);
		if (result == null) {
			getDao().getDaoStat().incQueryCalls();
			result = selectNotCache(query);
			getQueryCache().putQuery(clazz, queryId, params, 
					(List<BaseEntity>)result);			
		}
		return result;
	}

	protected List<T> selectNotCache(Query query) {
		getDao().getDaoStat().incQueryCalls();
		PreparedQuery p = getDatastore().prepare(query);
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity entity : p.asIterable(FetchOptions.Builder.withChunkSize(CHUNK_SIZE))) {
			entities.add(entity);
		}
		return createModels(entities);
	}

	protected T selectOne(Query query, String queryId, Object[] params) {
		List<T> list = select(query, queryId, params);
		if (list.isEmpty()) {
			return null;
		}
		if (list.size() > 1) {
			logger.error("May be consistency error. Multiple result for select one query " 
					+ clazz.getName() + " " + queryId + params.toString());
		}
		return list.get(0);
	}
		
	protected Object[] params(Object...objects) {
		return objects;
	}

	protected Query newQuery() {
		return new Query(getKind());
	}
	
	@Override
	public String getKind() {
		return kind;
	}

	private String createKind() {
		return EntityUtil.getKind(clazz);
	}
	
	private String getCurrentUserEmail() {
		return VosaoContext.getInstance().getUser() == null ? "guest" 
				: VosaoContext.getInstance().getUser().getEmail();
	}
	
	protected int count(Query query) {
		getDao().getDaoStat().incQueryCalls();
		query.setKeysOnly();
		PreparedQuery p = getDatastore().prepare(query);
		int count = 0;
		for (Entity entity : p.asIterable(FetchOptions.Builder.withChunkSize(CHUNK_SIZE))) count++;
		return count;
	}

	@Override
	public int count() {
		return count(newQuery());
	}
	
}
