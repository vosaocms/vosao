package org.vosao.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.vosao.entity.TemplateEntity;

public interface TemplateBusiness {

	void setFolderBusiness(FolderBusiness bean);
	FolderBusiness getFolderBusiness();
	
	byte[] createExportFile(final List<TemplateEntity> list) throws IOException;
	
	List<String> validateBeforeUpdate(final TemplateEntity template);
	
	void importThemes(InputStream in);
}
