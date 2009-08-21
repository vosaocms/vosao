package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.FileDao;
import org.vosao.entity.FileEntity;

public class FileDaoImpl extends AbstractDaoImpl implements FileDao {

	public void save(final FileEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FileEntity p = pm.getObjectById(FileEntity.class, entity.getId());
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
	
	public FileEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FileEntity.class, id);
		}
		finally {
			pm.close();
		}
	}
	
	public List<FileEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileEntity.class.getName();
			List<FileEntity> result = (List<FileEntity>)pm.newQuery(query).execute();
			result.size();
			return result;
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<Long> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (Long id : ids) {
				pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
