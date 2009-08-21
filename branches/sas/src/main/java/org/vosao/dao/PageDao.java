package org.vosao.dao;

import java.util.List;

import javax.jdo.PersistenceManagerFactory;

import org.vosao.entity.PageEntity;

public interface PageDao {

	void setPersistenceManagerFactory(PersistenceManagerFactory factory);
	
	PersistenceManagerFactory getPersistenceManagerFactory();

	void save(final PageEntity page);
	
	PageEntity getById(final Long id);

	List<PageEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	
}
