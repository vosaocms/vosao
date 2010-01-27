package org.vosao.dao;

import javax.jdo.PersistenceManager;

public interface DaoActionOne<T> {
	
	T execute(PersistenceManager pm);
	
}
