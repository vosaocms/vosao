package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FormBusiness;
import org.vosao.entity.FormEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FormBusinessImpl extends AbstractBusinessImpl 
	implements FormBusiness {

	private static final Log logger = LogFactory.getLog(FormBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final FormEntity entity) {
		List<String> errors = new ArrayList<String>();
		if (entity.getId() == null) {
			FormEntity myForm = getDao().getFormDao().getByName(entity.getName());
			if (myForm != null) {
				errors.add("Form with such name already exists");
			}
		}
		if (StringUtil.isEmpty(entity.getName())) {
			errors.add("Name is empty");
		}
		if (StringUtil.isEmpty(entity.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(entity.getEmail())) {
			errors.add("Email is empty");
		}
		return errors;
	}
	
}
