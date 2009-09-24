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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.ConfigDao;
import org.vosao.entity.ConfigEntity;

public class ConfigDaoImpl extends AbstractDaoImpl implements ConfigDao {

	public void save(final ConfigEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				ConfigEntity p = pm.getObjectById(ConfigEntity.class, entity.getId());
				p.copy(entity);
			}
			else {
				pm.makePersistent(entity);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public ConfigEntity getById(final Long id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(ConfigEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<ConfigEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ConfigEntity.class.getName();
			List<ConfigEntity> result = (List<ConfigEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public ConfigEntity getByName(final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ConfigEntity.class.getName() 
					+ " where name == pName parameters String pName";
			List<ConfigEntity> result = (List<ConfigEntity>)pm.newQuery(query)
					.execute(name);
			if (result.size() == 0) {
				return null;
			}
			else {
				return result.get(0);
			}
		}
		finally {
			pm.close();
		}
	}

	public void remove(final Long id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(ConfigEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<Long> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (Long id : ids) {
				pm.deletePersistent(pm.getObjectById(ConfigEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public Map<String, String> getConfig() {
		List<ConfigEntity> list = select();
		Map<String, String> result = new HashMap<String, String>(); 
		for (ConfigEntity config : list) {
			result.put(config.getName(), config.getValue());
		}
		return result;
	}

}
