package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FileBusiness;
import org.vosao.entity.FileEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FileBusinessImpl extends AbstractBusinessImpl 
	implements FileBusiness {

	private static final Log logger = LogFactory.getLog(FileBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final FileEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (StringUtil.isEmpty(entity.getFilename())) {
			errors.add("Filename is empty");
		}
		else {
			FileEntity file = getDao().getFileDao().getByName(
					entity.getFolderId(), entity.getFilename());
			if ((StringUtils.isEmpty(entity.getId()) && file != null)
				||
				(!StringUtils.isEmpty(entity.getId()) 
				 && !file.getId().equals(entity.getId())) ) {
				errors.add("File with such name already exists in this folder");
			}
		}
		if (StringUtil.isEmpty(entity.getTitle())) {
			errors.add("Title is empty");
		}
		return errors;
	}
	
}
