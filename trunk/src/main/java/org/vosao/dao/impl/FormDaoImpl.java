package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.FormDao;
import org.vosao.entity.FormEntity;

public class FormDaoImpl extends AbstractDaoImpl implements FormDao {

	@Override
	public void save(final FormEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FormEntity p = pm.getObjectById(FormEntity.class, entity.getId());
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
	
	@Override
	public FormEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FormEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<FormEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FormEntity.class.getName();
			List<FormEntity> result = (List<FormEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public FormEntity getByName(final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FormEntity.class.getName() 
					+ " where name == pName parameters String pName";
			List<FormEntity> result = (List<FormEntity>)pm.newQuery(query)
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

	@Override
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FormEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(FormEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
