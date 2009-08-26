package org.vosao.dao;

import javax.jdo.PersistenceManagerFactory;

public interface AbstractDao {

	void setPersistenceManagerFactory(PersistenceManagerFactory factory);
	
	PersistenceManagerFactory getPersistenceManagerFactory();

}
