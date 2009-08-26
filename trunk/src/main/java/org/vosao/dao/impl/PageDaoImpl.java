package org.vosao.dao.impl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.vosao.dao.PageDao;
import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;

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
	
	public PageEntity getById(final String id) {
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
	
	public void remove(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(PageEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(PageEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	public List<PageEntity> getByParent(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName()
			    + " where parent == pParent parameters String pParent";
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query)
				.execute(id);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	public PageEntity getByUrl(final String url) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + PageEntity.class.getName()
			    + " where friendlyURL == pUrl parameters String pUrl";
			List<PageEntity> result = (List<PageEntity>)pm.newQuery(query)
				.execute(url);
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
