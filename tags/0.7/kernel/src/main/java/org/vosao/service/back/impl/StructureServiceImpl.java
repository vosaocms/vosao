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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.business.vo.StructureFieldVO;
import org.vosao.entity.StructureEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.StructureService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.utils.StrUtil;

/**
 * @author Alexander Oleynik
 */
public class StructureServiceImpl extends AbstractServiceImpl 
		implements StructureService {

	@Override
	public List<StructureEntity> select() {
		return getDao().getStructureDao().select();
	}

	@Override
	public ServiceResponse remove(List<String> ids) {
		List<String> errors = getBusiness().getStructureBusiness().remove(
				StrUtil.toLong(ids));
		if (errors.isEmpty()) {
			return ServiceResponse.createSuccessResponse(
				Messages.get("structures.success_delete"));
		}
		else {
			return ServiceResponse.createErrorResponse(
				Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public StructureEntity getById(Long id) {
		return getDao().getStructureDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		StructureEntity entity = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			entity = getDao().getStructureDao().getById(Long.valueOf(
					vo.get("id")));
		}
		if (entity == null) {
			entity = new StructureEntity();
		}
		entity.setTitle(vo.get("title"));
		entity.setContent(vo.get("content"));
		List<String> errors = getBusiness().getStructureBusiness()
				.validateBeforeUpdate(entity);
		if (errors.isEmpty()) {
			getDao().getStructureDao().save(entity);
			return ServiceResponse.createSuccessResponse(entity.getId()
					.toString());
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public List<StructureFieldVO> getFields(Long structureId) {
		StructureEntity entity = getById(structureId);
		if (entity == null) {
			return Collections.EMPTY_LIST;
		}
		return entity.getFields();
	}

}
