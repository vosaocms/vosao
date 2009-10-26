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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.FieldService;
import org.vosao.service.back.impl.vo.FieldVO;
import org.vosao.service.impl.AbstractServiceImpl;

public class FieldServiceImpl extends AbstractServiceImpl 
		implements FieldService {

	private static final Log logger = LogFactory.getLog(FieldServiceImpl.class);

	@Override
	public ServiceResponse updateField(Map<String, String> fieldMap) {
		logger.info("updateField");
		FieldEntity field = new FieldEntity(); 
		List<String> errors = getBusiness().getFieldBusiness().convertFromVO(
				field, fieldMap);
		if (errors.isEmpty()) {
			List<String> validateErrors = getBusiness().getFieldBusiness()
					.validateBeforeUpdate(field);
			if (validateErrors.isEmpty()) {
				getDao().getFieldDao().save(field);
				return ServiceResponse.createSuccessResponse(
						"Field was successfully updated");
			}
			else {
				ServiceResponse resp = ServiceResponse.createErrorResponse( 
						"Validation errors");
				resp.setMessages(validateErrors);
				return resp;
			}
		}
		else {
			ServiceResponse resp = ServiceResponse.createErrorResponse( 
					"Convertion errors");
			resp.setMessages(errors);
			return resp;
		}
	}

	@Override
	public List<FieldVO> getByForm(String formId) {
		List<FieldEntity> result = new ArrayList<FieldEntity>();
		FormEntity form = getDao().getFormDao().getById(formId);
		if (form != null) {
			result = getDao().getFieldDao().getByForm(form);
		}
		return FieldVO.create(result);
	}

	@Override
	public FieldVO getById(String fieldId) {
		FieldEntity field = getDao().getFieldDao().getById(fieldId);
		if (field != null) {
			return new FieldVO(field);
		}
		return null;
	}

	@Override
	public void remove(List<String> ids) {
		getDao().getFieldDao().remove(ids);
	}

}
