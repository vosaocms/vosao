package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

import com.google.appengine.api.datastore.Key;

public interface FileDao extends AbstractDao {

	void save(final FileEntity page);
	
	FileEntity getById(final Key id);

	void remove(final Key id);
	
	void remove(final List<Key> ids);
	
	
}
