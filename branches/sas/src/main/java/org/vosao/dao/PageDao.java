package org.vosao.dao;

import java.util.List;

import org.vosao.entity.PageEntity;

public interface PageDao extends AbstractDao {

	void save(final PageEntity page);
	
	PageEntity getById(final Long id);

	List<PageEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	
}
