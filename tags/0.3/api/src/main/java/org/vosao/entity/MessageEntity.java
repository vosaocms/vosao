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

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class MessageEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	private String code;
	private String languageCode;
	private String value;

	public MessageEntity() {
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		code = getStringProperty(entity, "code");
		languageCode = getStringProperty(entity, "languageCode");
		value = getStringProperty(entity, "value");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("code", code);
		entity.setProperty("languageCode", languageCode);
		entity.setProperty("value", value);
	}

	public MessageEntity(final String code, final String languageCode, 
			final String value) {
		this.code = code;
		this.languageCode = languageCode;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
