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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.TemplateEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.TemplateService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class TemplateServiceImpl extends AbstractServiceImpl 
		implements TemplateService {

	@Override
	public ServiceResponse updateContent(Long templateId, String content) {
		TemplateEntity template = getDao().getTemplateDao().getById(templateId);
		if (template != null) {
			template.setContent(content);
			getBusiness().getTemplateBusiness().save(template);
			return ServiceResponse.createSuccessResponse(
					Messages.get("template.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("template.not_found", templateId));
		}
	}

	@Override
	public List<TemplateEntity> getTemplates() {
		return getDao().getTemplateDao().select();
	}

	@Override
	public ServiceResponse deleteTemplates(List<String> ids) {
		List<String> errors = getBusiness().getTemplateBusiness().remove(
				StrUtil.toLong(ids));
		if (errors.isEmpty()) {
			return ServiceResponse.createSuccessResponse(
					Messages.get("templates.success_delete"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
			
		}
	}

	@Override
	public TemplateEntity getTemplate(Long id) {
		return getDao().getTemplateDao().getById(id);
	}

	@Override
	public ServiceResponse saveTemplate(Map<String, String> vo) {
		TemplateEntity template = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			template = getDao().getTemplateDao().getById(Long.valueOf(
					vo.get("id")));
		}
		if (template == null) {
			template = new TemplateEntity();
		}
		template.setTitle(vo.get("title"));
		template.setUrl(vo.get("url"));
		template.setContent(vo.get("content"));
		List<String> errors = getBusiness().getTemplateBusiness()
			.validateBeforeUpdate(template);
		if (errors.isEmpty()) {
			getBusiness().getTemplateBusiness().save(template);
			return ServiceResponse.createSuccessResponse(template.getId()
					.toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);			
		}
	}

}
