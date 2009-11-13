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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FieldBusiness;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.enums.FieldType;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FieldBusinessImpl extends AbstractBusinessImpl 
	implements FieldBusiness {

	private static final Log logger = LogFactory.getLog(FieldBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(FieldEntity field) {
		List<String> errors = new ArrayList<String>();
		FormEntity form = getDao().getFormDao().getById(field.getFormId());
		if (form == null) {
			errors.add("Form id is wrong " + field.getFormId());
		}
		else {		
			if (field.getId() == null) {
				FieldEntity myField = getDao().getFieldDao().getByName(
						form, field.getName());
				if (myField != null) {
					errors.add("Field with such name already exists");
				}
			}
		}
		if (StringUtil.isEmpty(field.getName())) {
			errors.add("Name is empty");
		}
		if (StringUtil.isEmpty(field.getTitle())) {
			errors.add("Title is empty");
		}
		return errors;
	}

	@Override
	public List<String> convertFromVO(FieldEntity field, 
			Map<String, String> vo) {
		List<String> errors = new ArrayList<String>();
		try {
			field.setId(vo.get("id"));
			field.setFormId(vo.get("formId"));
			field.setName(vo.get("name"));
			field.setTitle(vo.get("title"));
			field.setFieldType(FieldType.valueOf(vo.get("fieldType")));
			field.setMandatory(Boolean.valueOf(vo.get("mandatory")));
			field.setDefaultValue(vo.get("defaultValue"));
			field.setValues(vo.get("values"));
			field.setHeight(Integer.valueOf(vo.get("height")));
			field.setWidth(Integer.valueOf(vo.get("width")));
		}
		catch (Exception e) {
			errors.add(e.getMessage());
		}
		return errors;
	}
	
}
