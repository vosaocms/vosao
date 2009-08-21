package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

public interface FileDao extends AbstractDao {

	void save(final FileEntity page);
	
	FileEntity getById(final Long id);

	List<FileEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	
}
