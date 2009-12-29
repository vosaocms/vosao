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

import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.entity.TemplateEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.TemplateService;
import org.vosao.service.impl.AbstractServiceImpl;

public class TemplateServiceImpl extends AbstractServiceImpl 
		implements TemplateService {

	@Override
	public ServiceResponse updateContent(String templateId, String content) {
		TemplateEntity template = getDao().getTemplateDao().getById(templateId);
		if (template != null) {
			template.setContent(content);
			getDao().getTemplateDao().save(template);
			return ServiceResponse.createSuccessResponse(
					"Template was successfully updated");
		}
		else {
			return ServiceResponse.createErrorResponse("Template not found " 
					+ templateId);
		}
	}

	@Override
	public List<TemplateEntity> getTemplates() {
		return getDao().getTemplateDao().select();
	}

	@Override
	public ServiceResponse deleteTemplates(List<String> ids) {
		List<String> errors = getBusiness().getTemplateBusiness().remove(ids);
		if (errors.isEmpty()) {
			return ServiceResponse.createSuccessResponse(
				"Templates were successfully deleted");
		}
		else {
			return ServiceResponse.createErrorResponse(
				"Templates were deleted with errors", errors);
			
		}
	}

	@Override
	public TemplateEntity getTemplate(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return getDao().getTemplateDao().getById(id);
	}

	@Override
	public ServiceResponse saveTemplate(Map<String, String> vo) {
		TemplateEntity template = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			template = getDao().getTemplateDao().getById(vo.get("id"));
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
			getDao().getTemplateDao().save(template);
			return ServiceResponse.createSuccessResponse(template.getId());
		}
		else {
			return ServiceResponse.createErrorResponse(
					"There was an errors during template save.", errors);			
		}
	}

}
