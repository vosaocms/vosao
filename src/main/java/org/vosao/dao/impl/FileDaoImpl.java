package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.FileDao;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

public class FileDaoImpl extends AbstractDaoImpl implements FileDao {

	public void save(final FileEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FileEntity p = pm.getObjectById(FileEntity.class, entity.getId());
				p.copy(entity);
			}
			else {
				FolderEntity folder = pm.getObjectById(FolderEntity.class, 
						entity.getFolder().getId());
				folder.addFile(entity);
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

}
