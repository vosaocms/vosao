package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.vosao.dao.PageDao;
import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;

public class PageDaoImpl implements PageDao {

	PersistenceManagerFactory pmf;
	
	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmf;
	}

	public void setPersistenceManagerFactory(PersistenceManagerFactory factory) {
		pmf = factory;		
	}
	
	private PersistenceManager getPersistenceManager() {  
		return PersistenceManagerFactoryUtils  
	    		.getPersistenceManager(pmf, true);
	} 

	public void save(final PageEntity page) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(page);
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
			return copy((List<PageEntity>)pm.newQuery(query).execute());
		}
		finally {
			pm.close();
		}
	}
	
	private static <T> List<T> copy(final List<T> list) {
		List<T> result = new ArrayList<T>();
		result.addAll(list);
		return result;
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
