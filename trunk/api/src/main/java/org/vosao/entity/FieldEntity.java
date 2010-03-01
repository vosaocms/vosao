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

package org.vosao.entity;

import java.util.ArrayList;
import java.util.List;

import org.vosao.enums.FieldType;

import com.google.appengine.api.datastore.Entity;

public class FieldEntity extends BaseNativeEntityImpl {

	public static class Option {
		private String value;
		private boolean selected;
		
		public Option(String value, boolean selected) {
			super();
			this.value = value;
			this.selected = selected;
		}

		public String getValue() {
			return value;
		}

		public boolean isSelected() {
			return selected;
		}
	}

	
	private static final long serialVersionUID = 2L;

	private Long formId;
	private String name;
	private String title;
	private FieldType fieldType;
	private boolean mandatory;
	private String values;
	private String defaultValue;
	private int height;
	private int width;
	private int index;

	public FieldEntity() {
		height = 1;
		width = 20;
	}
	
	public FieldEntity(Long formId, String name, String title,
			FieldType fieldType, boolean optional, String defaultValue) {
		this();
		this.formId = formId;
		this.name = name;
		this.title = title;
		this.fieldType = fieldType;
		this.mandatory = optional;
		this.defaultValue = defaultValue;
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		formId = getLongProperty(entity, "formId");
		name = getStringProperty(entity, "name");
		title = getStringProperty(entity, "title");
		fieldType = FieldType.valueOf(getStringProperty(entity, "fieldType"));
		mandatory = getBooleanProperty(entity, "mandatory", false);
		values = getStringProperty(entity, "values");
		defaultValue = getStringProperty(entity, "defaultValue");
		height = getIntegerProperty(entity, "height", 1);
		width = getIntegerProperty(entity, "width", 20);
		index = getIntegerProperty(entity, "index", 0);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("formId", formId);
		entity.setProperty("name", name);
		entity.setProperty("title", title);
		entity.setProperty("fieldType", fieldType.name());
		entity.setProperty("mandatory", mandatory);
		entity.setProperty("values", values);
		entity.setProperty("defaultValue", defaultValue);
		entity.setProperty("height", height);
		entity.setProperty("width", width);
		entity.setProperty("index", index);
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean optional) {
		this.mandatory = optional;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getTitle() {
		return title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public List<Option> getOptions() {
		List<Option> result = new ArrayList<Option>();
		String[] opts = getValues().split("\n");
		for (String opt : opts) {
			String val = opt.replace("*", "");
			boolean selected = opt.charAt(0) == '*';
			result.add(new Option(val, selected));
		}
		return result;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
