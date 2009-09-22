package org.vosao.business;

import java.util.List;

import org.vosao.entity.FileEntity;

public interface FileBusiness {

	List<String> validateBeforeUpdate(final FileEntity entity);
	
}
