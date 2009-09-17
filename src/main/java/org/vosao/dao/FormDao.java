package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FormEntity;

public interface FormDao extends AbstractDao {

	void save(final FormEntity entity);
	
	FormEntity getById(final String id);

	FormEntity getByName(final String name);

	List<FormEntity> select();
	
	void remove(final String id);
	
	void remove(final List<String> ids);
	
}
