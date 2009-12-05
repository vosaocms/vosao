package org.vosao.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

public interface DaoAction<T> {
	
	List<T> execute(PersistenceManager pm);
	
}
