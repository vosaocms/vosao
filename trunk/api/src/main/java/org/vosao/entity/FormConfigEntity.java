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
import com.google.appengine.api.datastore.Text;
import static org.vosao.utils.EntityUtil.*;

public class FormConfigEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	public static final String FORM_TEMPLATE = "formTemplate";
	public static final String LETTER_TEMPLATE = "letterTemplate";

	private String formTemplate;
	private String letterTemplate;

	public FormConfigEntity() {
		setFormTemplate("");
		setLetterTemplate("");
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		formTemplate = getTextProperty(entity, "formTemplate");
		letterTemplate = getTextProperty(entity, "letterTemplate");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setTextProperty(entity, "formTemplate", formTemplate);
		setTextProperty(entity, "letterTemplate", letterTemplate);
	}

	public String getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(String formTemplate) {
		this.formTemplate = formTemplate;
	}

	public String getLetterTemplate() {
		return letterTemplate;
	}

	public void setLetterTemplate(String letterTemplate) {
		this.letterTemplate = letterTemplate;
	}
	
}
