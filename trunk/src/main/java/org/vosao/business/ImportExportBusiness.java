package org.vosao.business;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.dom4j.DocumentException;
import org.vosao.entity.TemplateEntity;

public interface ImportExportBusiness {

	void setFolderBusiness(FolderBusiness bean);
	FolderBusiness getFolderBusiness();
	
	byte[] createExportFile(final List<TemplateEntity> list) throws IOException;
	
	void importThemes(ZipInputStream in) throws IOException, DocumentException;
	
}
