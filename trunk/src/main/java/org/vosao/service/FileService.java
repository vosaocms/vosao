package org.vosao.service;

import java.util.List;

import org.vosao.entity.FileEntity;


public interface FileService extends AbstractService {
	
	List<FileEntity> getByFolder(final String folderId);

	ServiceResponse deleteFiles(final List<String> fileIds);
	
}
