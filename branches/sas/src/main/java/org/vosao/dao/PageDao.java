package org.vosao.dao;

import java.util.List;

import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;

public interface PageDao extends AbstractDao {

	void save(final PageEntity page);
	
	PageEntity getById(final String id);

	List<PageEntity> getByParent(final String id);

	List<PageEntity> select();
	
	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
