package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

import com.google.appengine.api.datastore.Key;

public interface FileDao extends AbstractDao {

	void save(final FileEntity page);
	
	FileEntity getById(final String id);

	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
