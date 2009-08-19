package org.vosao.dao.impl;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

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
		//return PMF.get().getPersistenceManager();
		
	} 

	public void test() {
		PersistenceManager pm = getPersistenceManager();
		//Transaction tx = pm.currentTransaction();
		try {
			//tx.begin();
			
			PageEntity page = new PageEntity("Page title", 
				"Page content", "/mypage", null);
			pm.makePersistent(page);
			page.setTitle("Test title2");
			pm.makePersistent(page);
			//tx.commit();
		}
		finally {
			//if (tx.isActive()) {
			//	tx.rollback();
			//}
			pm.close();
		}
	}

}
