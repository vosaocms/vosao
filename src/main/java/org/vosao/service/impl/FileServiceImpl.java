package org.vosao.service.impl;

import java.util.List;

import org.vosao.entity.FileEntity;
import org.vosao.service.FileService;
import org.vosao.service.ServiceResponse;

public class FileServiceImpl extends AbstractServiceImpl 
		implements FileService {

	@Override
	public List<FileEntity> getByFolder(String folderId) {
		return getDao().getFileDao().getByFolder(folderId);
	}

	@Override
	public ServiceResponse deleteFiles(List<String> fileIds) {
		getDao().getFileDao().remove(fileIds);
		return new ServiceResponse("success", "Files was successfully deleted.");
	}

}
