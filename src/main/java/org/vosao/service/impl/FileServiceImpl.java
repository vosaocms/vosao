package org.vosao.service.impl;

import java.util.List;

import org.vosao.entity.FileEntity;
import org.vosao.service.FileService;

public class FileServiceImpl extends AbstractServiceImpl 
		implements FileService {

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		return getDao().getFileDao().getByFolder(folderId);
	}

}
