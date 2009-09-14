package org.vosao.business;

import java.util.List;

import org.vosao.entity.TemplateEntity;

public interface TemplateBusiness {

	List<String> validateBeforeUpdate(final TemplateEntity template);
	
}
