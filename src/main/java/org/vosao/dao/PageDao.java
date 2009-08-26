package org.vosao.dao;

import java.util.List;

import org.vosao.entity.PageEntity;

public interface PageDao extends AbstractDao {

	void save(final PageEntity page);
	
	PageEntity getById(final String id);

	List<PageEntity> getByParent(final String id);

	PageEntity getByUrl(final String url);

	List<PageEntity> select();
	
	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
