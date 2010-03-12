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

package org.vosao.service.back.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.business.SetupBean;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FormService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;
import org.vosao.utils.StreamUtil;

public class FormServiceImpl extends AbstractServiceImpl 
		implements FormService {

	private static final Log logger = LogFactory.getLog(FormServiceImpl.class);

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
		List<String> errors = getBusiness().getFormBusiness()
			.validateBeforeUpdate(form);
		if (errors.isEmpty()) {
			getDao().getFormDao().save(form);
			return ServiceResponse.createSuccessResponse(form.getId().toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Errors occured during form save", errors);
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
				"Forms were successfully deleted.");
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
				"Form configuration was successfully saved.");
	}

	@Override
	public ServiceResponse restoreFormLetter() throws IOException {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		config.setLetterTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.FORM_LETTER_FILE));
		getDao().getFormConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				"Form letter was successfully restored.");
	}

	@Override
	public ServiceResponse restoreFormTemplate() throws IOException {
		FormConfigEntity config = getDao().getFormConfigDao().getConfig();
		config.setFormTemplate(StreamUtil.getTextResource(
			SetupBeanImpl.FORM_TEMPLATE_FILE));
		getDao().getFormConfigDao().save(config);			
		return ServiceResponse.createSuccessResponse(
				"Form template was successfully restored.");
	}

}
