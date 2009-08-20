package org.vosao.dao;

import java.util.List;

import javax.jdo.PersistenceManagerFactory;

import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;

public interface PageDao {

	void setPersistenceManagerFactory(PersistenceManagerFactory factory);
	
	PersistenceManagerFactory getPersistenceManagerFactory();

	void save(final PageEntity page);
	
	PageEntity getByKey(final Key key);

	List<PageEntity> select();
	
}
