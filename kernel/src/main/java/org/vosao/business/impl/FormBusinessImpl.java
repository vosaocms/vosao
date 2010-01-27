/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.vosao.business.FormBusiness;
import org.vosao.common.UploadException;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.utils.EmailUtil;
import org.vosao.utils.FileItem;

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
		if (StringUtils.isEmpty(entity.getName())) {
			errors.add("Name is empty");
		}
		if (StringUtils.isEmpty(entity.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtils.isEmpty(entity.getEmail())) {
			errors.add("Email is empty");
		}
		return errors;
	}

	@Override
	public void submit(FormEntity form, Map<String, String> parameters,
			List<FileItem> files) throws UploadException {
		ConfigEntity config = getDao().getConfigDao().getConfig();
		FormConfigEntity formConfig = getDao().getFormDao().getConfig();
		VelocityContext context = new VelocityContext();
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		context.put("form", form);
		context.put("fields", fields);
		context.put("values", parameters);
		context.put("config", config);
		String letter = getSystemService().render(
				formConfig.getLetterTemplate(), context);
		String error = EmailUtil.sendEmail(letter, form.getLetterSubject(), 
				config.getSiteEmail(), "Site admin", form.getEmail(), files);
		if (error != null) {
			throw new UploadException(error);
		}
		logger.info("Form successfully submited and emailed.");
	}
	
}
