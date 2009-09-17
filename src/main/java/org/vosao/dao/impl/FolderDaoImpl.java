package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.FolderDao;
import org.vosao.entity.FolderEntity;

public class FolderDaoImpl extends AbstractDaoImpl implements FolderDao {

	public void save(final FolderEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FolderEntity p = pm.getObjectById(FolderEntity.class, 
						entity.getId());
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
	
	public FolderEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FolderEntity.class, id);
		}
		finally {
			pm.close();
		}
	}
	
	public List<FolderEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName();
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
					.execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FolderEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(FolderEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	public List<FolderEntity> getByParent(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent parameters String pParent";
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
				.execute(id);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public FolderEntity getByParentName(final String parentId, 
			final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent && name == pName " 
			    + "parameters String pParent, String pName";
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
				.execute(parentId, name);
			if (result.size() == 0) {
				return null;
			}
			return result.get(0);
		}
		finally {
			pm.close();
		}
		
	}

	
}
