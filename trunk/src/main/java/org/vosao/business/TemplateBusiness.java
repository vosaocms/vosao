package org.vosao.business;

import java.io.IOException;
import java.util.List;

import org.vosao.entity.TemplateEntity;

public interface TemplateBusiness {

	void setFolderBusiness(FolderBusiness bean);
	FolderBusiness getFolderBusiness();
	
	byte[] createExportFile(final List<TemplateEntity> list) throws IOException;
	
}
