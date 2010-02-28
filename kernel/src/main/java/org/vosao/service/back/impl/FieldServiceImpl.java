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
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.FieldVO;

public class FieldServiceImpl extends AbstractServiceImpl 
		implements FieldService {

	private static final Log logger = LogFactory.getLog(FieldServiceImpl.class);

	@Override
	public ServiceResponse updateField(Map<String, String> fieldMap) {
		FieldEntity field = new FieldEntity(); 
		List<String> errors = getBusiness().getFieldBusiness().convertFromVO(
				field, fieldMap);
		if (errors.isEmpty()) {
			List<String> validateErrors = getBusiness().getFieldBusiness()
					.validateBeforeUpdate(field);
			if (validateErrors.isEmpty()) {
				boolean newField = field.getId() == null;
				getDao().getFieldDao().save(field);
				if (newField) {
					FormEntity form = getDao().getFormDao().getById(field.getFormId());
					getBusiness().getFieldBusiness().checkOrder(form);
				}
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
	public List<FieldVO> getByForm(Long formId) {
		List<FieldEntity> result = new ArrayList<FieldEntity>();
		FormEntity form = getDao().getFormDao().getById(formId);
		if (form != null) {
			result = getDao().getFieldDao().getByForm(form);
		}
		return FieldVO.create(result);
	}

	@Override
	public FieldVO getById(Long fieldId) {
		FieldEntity field = getDao().getFieldDao().getById(fieldId);
		if (field != null) {
			return new FieldVO(field);
		}
		return null;
	}

	@Override
	public void remove(List<Long> ids) {
		if (ids.size() > 0) {
			FieldEntity field = null;
			FormEntity form = null;
			for (Long id : ids) {
				field = getDao().getFieldDao().getById(id);
				if (field != null) {
					form = getDao().getFormDao().getById(field.getFormId());
					break;
				}
			}
			if (form == null) {
				return;
			}
			getDao().getFieldDao().remove(ids);
			getBusiness().getFieldBusiness().checkOrder(form);
		}
	}

	@Override
	public void moveDown(Long formId, Long fieldId) {
		FormEntity form = getDao().getFormDao().getById(formId);
		if (form == null) {
			return;
		}
		List<FieldEntity> fields = getBusiness().getFieldBusiness().checkOrder(form);
		int index = indexOf(fields, fieldId);
		if (index == -1) {
			return;
		}
		if (index + 1 < fields.size()) {
			fields.get(index).setIndex(index + 1);
			getDao().getFieldDao().save(fields.get(index));
			fields.get(index + 1).setIndex(index);
			getDao().getFieldDao().save(fields.get(index + 1));
		}		
	}
	
	private int indexOf(List<FieldEntity> fields, final Long fieldId) {
		for (int i = 0; i < fields.size(); i++) {
			FieldEntity field = fields.get(i);
			if (field.getId().equals(fieldId)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void moveUp(Long formId, Long fieldId) {
		FormEntity form = getDao().getFormDao().getById(formId);
		if (form == null) {
			return;
		}
		List<FieldEntity> fields = getBusiness().getFieldBusiness().checkOrder(form);
		int index = indexOf(fields, fieldId);
		if (index == -1) {
			return;
		}
		if (index - 1 >= 0) {
			fields.get(index).setIndex(index - 1);
			getDao().getFieldDao().save(fields.get(index));
			fields.get(index - 1).setIndex(index);
			getDao().getFieldDao().save(fields.get(index - 1));
		}		
	}

}
