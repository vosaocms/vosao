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
	
	public FileEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FileEntity.class, id);
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
			pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(FileEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId parameters String pFolderId";
			List<FileEntity> result = (List<FileEntity>)pm.newQuery(query)
				.execute(folderId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FileEntity getByName(String folderId, String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FileEntity.class.getName()
			    + " where folderId == pFolderId && filename == pName" 
			    + " parameters String pFolderId, String pName";
			List<FileEntity> result = (List<FileEntity>)pm.newQuery(query)
				.execute(folderId, name);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	

}
