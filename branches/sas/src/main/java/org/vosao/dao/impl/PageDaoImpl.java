package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.PageDao;
import org.vosao.entity.PageEntity;

public class PageDaoImpl extends AbstractDaoImpl implements PageDao {

	public void save(final PageEntity page) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (page.getId() != null) {
				PageEntity p = pm.getObjectById(PageEntity.class, page.getId());
				p.copy(page);
			}
			else {
				pm.makePersistent(page);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public PageEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(PageEntity.class, id);
		}
		finally {
			pm.close();
		}
	}
	
	public List<PageEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName();
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(PageEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<Long> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (Long id : ids) {
				pm.deletePersistent(pm.getObjectById(PageEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
