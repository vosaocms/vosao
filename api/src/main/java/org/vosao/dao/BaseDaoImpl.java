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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class BaseDaoImpl<T extends BaseEntity> 
		extends AbstractDaoImpl implements BaseDao<T>{
	
	protected static final Log logger = LogFactory.getLog(
			BaseDaoImpl.class);

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
			getEntityCache().removeEntities(clazz);
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
		getEntityCache().removeEntities(clazz);
		getQueryCache().removeQueries(clazz);
		PreparedQuery p = getDatastore().prepare(q);
		List<Key> keys = new ArrayList<Key>();
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		List<Entity> list = p.asList(withLimit(limit));
		int count = 0;
		for (Entity entity : list) {
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
	public T save(T model) {
		getQueryCache().removeQueries(clazz);
		Entity entity = null;
		if (model.getId() != null) {
			try {
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
		model.setModDate(new Date());
		model.setModUserEmail(getCurrentUserEmail());
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
			PreparedQuery p = getDatastore().prepare(q);
			int limit = p.countEntities() > 0 ? p.countEntities() : 1;
			result = createModels(p.asList(withLimit(limit)));
			getQueryCache().putQuery(clazz, clazz.getName(), null, result);
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
		List<T> result = (List<T>) getQueryCache().getQuery(clazz, queryId, 
				params);
		if (result == null) {
			PreparedQuery p = getDatastore().prepare(query);
			int limit = p.countEntities() > 0 ? p.countEntities() : 1;
			result = createModels(p.asList(withLimit(limit)));
			getQueryCache().putQuery(clazz, queryId, params, result);			
		}
		return result;
	}

	protected List<T> selectNotCache(Query query, String queryId, 
			Object[] params) {
		PreparedQuery p = getDatastore().prepare(query);
		int limit = p.countEntities() > 0 ? p.countEntities() : 1;
		return createModels(p.asList(withLimit(limit)));
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
}
