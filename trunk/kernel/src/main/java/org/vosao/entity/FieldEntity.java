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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.enums.FieldType;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FieldEntity implements Serializable {

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

	
	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String formId;

	@Persistent
	private String name;
	
	@Persistent
	private String title;

	@Persistent
	private FieldType fieldType;

	@Persistent
	private boolean mandatory;

	@Persistent
	private String values;

	@Persistent
	private String defaultValue;

	@Persistent
	private int height;

	@Persistent
	private int width;

	public FieldEntity() {
		height = 1;
		width = 20;
	}
	
	public void copy(final FieldEntity entity) {
		setFormId(entity.getFormId());
		setName(entity.getName());
		setTitle(entity.getTitle());
		setFieldType(entity.getFieldType());
		setMandatory(entity.isMandatory());
		setValues(entity.getValues());
		setDefaultValue(entity.getDefaultValue());
		setHeight(entity.getHeight());
		setWidth(entity.getWidth());
	}
	
	public FieldEntity(String formId, String name, String title,
			FieldType fieldType, boolean optional, String defaultValue) {
		this();
		this.formId = formId;
		this.name = name;
		this.title = title;
		this.fieldType = fieldType;
		this.mandatory = optional;
		this.defaultValue = defaultValue;
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

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

	public boolean equals(Object object) {
		if (object instanceof FieldEntity) {
			FieldEntity entity = (FieldEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
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
	
}
