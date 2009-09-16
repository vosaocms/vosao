package org.vosao.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;

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
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(ConfigEntity.class, id);
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
