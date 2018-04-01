/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.service.back.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormDataEntity;
import org.vosao.entity.FormEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FormService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FormServiceImpl extends AbstractServiceImpl 
		implements FormService {

	@Override
	public FormEntity getForm(Long formId) {
		return getDao().getFormDao().getById(formId);
	}

	@Override
	public ServiceResponse saveForm(Map<String, String> vo) {
		FormEntity form = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			form = getDao().getFormDao().getById(Long.valueOf(vo.get("id")));
		}
		if (form == null) {
			form = new FormEntity();
		}
		form.setTitle(vo.get("title"));
		form.setName(vo.get("name"));
		form.setEmail(vo.get("email"));
		form.setLetterSubject(vo.get("letterSubject"));
		form.setResetButtonTitle(vo.get("resetButtonTitle"));
		form.setSendButtonTitle(vo.get("sendButtonTitle"));
		form.setShowResetButton(Boolean.valueOf(vo.get("showResetButton")));
		form.setEnableCaptcha(Boolean.valueOf(vo.get("enableCaptcha")));
		form.setEnableSave(Boolean.valueOf(vo.get("enableSave")));
		List<String> errors = getBusiness().getFormBusiness()
			.validateBeforeUpdate(form);
		if (errors.isEmpty()) {
			getDao().getFormDao().save(form);
			return ServiceResponse.createSuccessResponse(form.getId().toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public List<FormEntity> select() {
		return getDao().getFormDao().select();
	}

	@Override
	public ServiceResponse deleteForm(List<String> ids) {
		getDao().getFormDao().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				Messages.get("form.success_delete"));
	}

	@Override
	public FormConfigEntity getFormConfig() {
		return getDao().getFormConfigDao().getConfig();
	}

	@Override
	public ServiceResponse saveFormConfig(Map<String, String> vo) {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		config.setFormTemplate(vo.get("formTemplate"));
		config.setLetterTemplate(vo.get("letterTemplate"));
		getDao().getFormConfigDao().save(config);
		return ServiceResponse.createSuccessResponse(
				Messages.get("form.config_success_save"));
	}

	@Override
	public ServiceResponse restoreFormLetter() throws IOException {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		config.setLetterTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.FORM_LETTER_FILE));
		getDao().getFormConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				Messages.get("form.letter_success_restore"));
	}

	@Override
	public ServiceResponse restoreFormTemplate() throws IOException {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		config.setFormTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.FORM_TEMPLATE_FILE));
		getDao().getFormConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				Messages.get("form.template_success_restore"));
	}

	@Override
	public ServiceResponse removeData(List<String> ids) {
		getDao().getFormDataDao().remove(StrUtil.toLong(ids));
		return ServiceResponse.createSuccessResponse(
				Messages.get("success"));

	}

	@Override
	public List<FormDataEntity> getFormData(Long formId) {
		FormEntity form = getDao().getFormDao().getById(formId);
		if (form == null) {
			return null;
		}
		return getDao().getFormDataDao().getByForm(form);
	}

	@Override
	public ServiceResponse sendFormLetter(Long formDataId) {
		FormDataEntity formData = getDao().getFormDataDao().getById(
				formDataId);
		if (formData == null) {
			return ServiceResponse.createErrorResponse(Messages.get("not_found"));
		}
		FormEntity form = getDao().getFormDao().getById(formData.getFormId());
		String error = getBusiness().getFormBusiness().sendEmail(formData);
		if (error != null) {
			return ServiceResponse.createErrorResponse(error);
		}
		return ServiceResponse.createSuccessResponse(Messages.get(
				"form.success_send", form.getEmail()));
	}

}
