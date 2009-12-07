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

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.BaseDao;
import org.vosao.dao.DaoAction;
import org.vosao.dao.DaoActionOne;

/**
 * @author Alexander Oleynik
 */
public class BaseDaoImpl<K,T> extends AbstractDaoImpl implements BaseDao<K,T> {

	private Class clazz;
	
	public BaseDaoImpl(Class aClass) {
		clazz = aClass;
	}
	
	@Override
	public void save(final T entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(entity);
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
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return (T)pm.getObjectById(clazz, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final K id) {
		if (id == null) {
			return;
		}
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
		PersistenceManager pm = getPersistenceManager();
		try {
			for (K id : ids) {
				if (id != null) {
					pm.deletePersistent(pm.getObjectById(clazz, id));
				}
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<T> select(String query, Object[] params) {
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return copy((List<T>)pm.newQuery(query).executeWithArray(params));
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<T> select() {
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return copy((List<T>)pm.newQuery("select from " + clazz.getName())
					.execute());
		}
		finally {
			pm.close();
		}
	}

	@Override
	public T selectOne(String query, Object[] params) {
		List<T> list = select(query, params);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
		
	protected Object[] params(Object...objects) {
		return objects;
	}

	@Override
	public List<T> select(DaoAction<T> action) {
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return copy(action.execute(pm));
		}
		finally {
			pm.close();
		}
	}

	@Override
	public T selectOne(DaoActionOne<T> action) {
		PersistenceManager pm = getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		try {
			return action.execute(pm);
		}
		finally {
			pm.close();
		}
	}
	
}
