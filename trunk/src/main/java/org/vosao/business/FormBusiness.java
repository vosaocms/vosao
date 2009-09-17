package org.vosao.business;

import java.util.List;

import org.vosao.entity.FormEntity;

public interface FormBusiness {

	List<String> validateBeforeUpdate(final FormEntity entity);
	
}
