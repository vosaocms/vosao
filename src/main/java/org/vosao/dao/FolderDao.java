package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

public interface FolderDao extends AbstractDao {

	void save(final FolderEntity page);
	
	FolderEntity getById(final Long id);

	List<FolderEntity> select();
	
	void remove(final Long id);
	
	void remove(final List<Long> ids);
	
	
}
