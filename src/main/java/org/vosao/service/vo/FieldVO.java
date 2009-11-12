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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.FieldEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class FieldVO {

	private FieldEntity field;
	
	public static List<FieldVO> create(List<FieldEntity> list) {
		List<FieldVO> result = new ArrayList<FieldVO>();
		for (FieldEntity entity : list) {
			result.add(new FieldVO(entity));
		}
		return result;
	}

	public FieldVO(final FieldEntity aField) {
		field = aField;
	}
	
	public String getFieldType() {
		return field.getFieldType().name();
	}

	public boolean isOptional() {
		return field.isMandatory();
	}

	public String getValues() {
		return field.getValues();
	}

	public String getDefaultValue() {
		return field.getDefaultValue();
	}

	public String getTitle() {
		return field.getTitle();
	}

	public String getId() {
		return field.getId();
	}
	
	public String getName() {
		return field.getName();
	}

	public String getFormId() {
		return field.getFormId();
	}

	public int getHeight() {
		return field.getHeight();
	}

	public int getWidth() {
		return field.getWidth();
	}
	
}
