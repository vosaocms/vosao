package org.vosao.dao;

public interface DaoFilter<T> {

	boolean inResult(T entity);
	
}
