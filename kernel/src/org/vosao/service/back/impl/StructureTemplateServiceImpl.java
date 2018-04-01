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
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.enums.StructureTemplateType;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.StructureTemplateService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;

/**
 * @author Alexander Oleynik
 */
public class StructureTemplateServiceImpl extends AbstractServiceImpl 
		implements StructureTemplateService {

	@Override
	public ServiceResponse remove(List<String> ids) {
		List<String> errors = getBusiness().getStructureTemplateBusiness()
				.remove(StrUtil.toLong(ids));
		if (errors.isEmpty()) {
			return ServiceResponse.createSuccessResponse(
				Messages.get("structureTemplate.success_delete"));
		}
		else {
			return ServiceResponse.createErrorResponse(
				Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public StructureTemplateEntity getById(Long id) {
		return getDao().getStructureTemplateDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		StructureTemplateEntity entity = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			entity = getDao().getStructureTemplateDao().getById(
					Long.valueOf(vo.get("id")));
		}
		if (entity == null) {
			entity = new StructureTemplateEntity();
		}
		entity.setName(vo.get("name"));
		entity.setTitle(vo.get("title"));
		entity.setContent(vo.get("content"));
		entity.setHeadContent(vo.get("headContent"));
		entity.setStructureId(Long.valueOf(vo.get("structureId")));
		entity.setType(StructureTemplateType.valueOf(vo.get("type")));
		List<String> errors = getBusiness().getStructureTemplateBusiness()
				.validateBeforeUpdate(entity);
		if (errors.isEmpty()) {
			getBusiness().getStructureTemplateBusiness().save(entity);
			return ServiceResponse.createSuccessResponse(entity.getId()
					.toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public List<StructureTemplateEntity> selectByStructure(Long structureId) {
		return getDao().getStructureTemplateDao().selectByStructure(structureId);
	}

}
