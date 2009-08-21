package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.vosao.dao.AbstractDao;

public class AbstractDaoImpl implements AbstractDao {

	PersistenceManagerFactory pmf;
	
	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmf;
	}

	public void setPersistenceManagerFactory(PersistenceManagerFactory factory) {
		pmf = factory;		
	}
	
	protected PersistenceManager getPersistenceManager() {  
		return PersistenceManagerFactoryUtils  
	    		.getPersistenceManager(pmf, true);
	} 

	protected <T> List<T> copy(final List<T> list) {
		List<T> result = new ArrayList<T>();
		result.addAll(list);
		return result;
	}
	
	
}
