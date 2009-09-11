package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FolderEntity;

public interface FolderDao extends AbstractDao {

	void save(final FolderEntity page);
	
	FolderEntity getById(final String id);

	List<FolderEntity> getByParent(final String id);

	FolderEntity getByParentName(final String parentid, final String name);
	
	List<FolderEntity> select();
	
	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
