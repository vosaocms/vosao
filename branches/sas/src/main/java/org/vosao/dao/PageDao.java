package org.vosao.dao;

import javax.jdo.PersistenceManagerFactory;

public interface PageDao {

	void setPersistenceManagerFactory(PersistenceManagerFactory factory);
	
	PersistenceManagerFactory getPersistenceManagerFactory();

	void test();
	
}
