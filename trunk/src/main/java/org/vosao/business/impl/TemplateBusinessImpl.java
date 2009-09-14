package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.TemplateBusiness;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class TemplateBusinessImpl extends AbstractBusinessImpl 
	implements TemplateBusiness {

	private static final Log logger = LogFactory.getLog(TemplateBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final TemplateEntity template) {
		List<String> errors = new ArrayList<String>();
		if (template.getId() == null) {
			TemplateEntity myTemplate = getDao().getTemplateDao().getByUrl(
					template.getUrl());
			if (myTemplate != null) {
				errors.add("Template with such URL already exists");
			}
		}
		if (StringUtil.isEmpty(template.getUrl())) {
			errors.add("URL is empty");
		}
		if (StringUtil.isEmpty(template.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(template.getContent())) {
			errors.add("Content is empty");
		}
		return errors;
	}
	
}
