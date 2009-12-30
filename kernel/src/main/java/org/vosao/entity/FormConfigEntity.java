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
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FormConfigEntity implements BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String FORM_TEMPLATE = "formTemplate";
	public static final String LETTER_TEMPLATE = "letterTemplate";

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent(defaultFetchGroup = "true")
	private Text formTemplate;

	@Persistent(defaultFetchGroup = "true")
	private Text letterTemplate;

	public FormConfigEntity() {
		setFormTemplate("");
		setLetterTemplate("");
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final FormConfigEntity entity) {
		setFormTemplate(entity.getFormTemplate());
		setLetterTemplate(entity.getLetterTemplate());
	}
	
	/**
	 * Get all configs with values.
	 * @return configs with not null values.
	 */
	public Map<String, String> getConfigMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.put(FORM_TEMPLATE, getNotNull(getFormTemplate()));
		result.put(LETTER_TEMPLATE, getNotNull(getLetterTemplate()));
		return result;
	}

	private String getNotNull(String value) {
		if (value != null) {
			return value;
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public boolean equals(Object object) {
		if (object instanceof ConfigEntity) {
			ConfigEntity entity = (ConfigEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public String getFormTemplate() {
		if (formTemplate == null) {
			return null;
		}
		return formTemplate.getValue();
	}

	public void setFormTemplate(String formTemplate) {
		this.formTemplate = new Text(formTemplate);
	}

	public String getLetterTemplate() {
		if (letterTemplate == null) {
			return null;
		}
		return letterTemplate.getValue();
	}

	public void setLetterTemplate(String letterTemplate) {
		this.letterTemplate = new Text(letterTemplate);
	}
	
}
