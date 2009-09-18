package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

public interface FileDao extends AbstractDao {

	void save(final FileEntity page);
	
	FileEntity getById(final String id);

	List<FileEntity> getByFolder(final String folderId);

	FileEntity getByName(final String folderId, final String name);

	void remove(final String id);
	
	void remove(final List<String> ids);
	
	
}
