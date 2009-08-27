package org.vosao.dao;

import java.util.List;

import org.vosao.entity.TemplateEntity;

public interface TemplateDao extends AbstractDao {

	void save(final TemplateEntity page);
	
	TemplateEntity getById(final String id);

	List<TemplateEntity> select();
	
	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
