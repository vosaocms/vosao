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

package org.vosao.entity;

import com.google.appengine.api.datastore.Entity;
import static org.vosao.utils.EntityUtil.*;

public class LanguageEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	public static final String ENGLISH_CODE = "en";
	public static final String ENGLISH_TITLE = "English";
	
	private String code;
	private String title;

	public LanguageEntity() {
		code = "";
		title = "";
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		code = getStringProperty(entity, "code");
		title = getStringProperty(entity, "title");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "code", code, true);
		setProperty(entity, "title", title, false);
	}

	public LanguageEntity(final String code, final String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
