package org.vosao.webdav;

import org.vosao.business.Business;
import org.vosao.entity.FileEntity;

public class FileResource extends AbstractFileResource {

	private FileEntity file;
	
	public FileResource(Business aBusiness, FileEntity aFile) {
		super(aBusiness, aFile.getFilename(), aFile.getLastModifiedTime());
		file = aFile;
		setData(getDao().getFileDao().getFileContent(file));
	}

}
