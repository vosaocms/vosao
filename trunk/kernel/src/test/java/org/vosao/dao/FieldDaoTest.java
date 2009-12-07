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

package org.vosao.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.enums.FieldType;

public class FieldDaoTest extends AbstractDaoTest {

	private FormEntity addForm(final String name) {
		FormEntity form = new FormEntity(name, name, name, name);
		getDao().getFormDao().save(form);
		return form;
	}

	private FieldEntity addField(final String name, final FieldType type,
			final String defaultValue, final FormEntity form) {
		FieldEntity field = new FieldEntity(form.getId(), name, name, type, 
				false, defaultValue);
		getDao().getFieldDao().save(field);
		return field;
	}
	
	public void testSave() {
		FormEntity form = addForm("form");
		addField("field1", FieldType.TEXT, "text", form);
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		assertEquals(1, fields.size());
		FieldEntity field1 = fields.get(0);
		assertEquals("field1", field1.getName());
	}	
	
	public void testGetById() {
		FormEntity form = addForm("form");
		FieldEntity field = addField("field1", FieldType.TEXT, "text", form);
		assertNotNull(field.getId());
		FieldEntity field2 = getDao().getFieldDao().getById(field.getId());
		assertEquals(field.getTitle(), field2.getTitle());
	}	

	public void testUpdate() {
		FormEntity form = addForm("form");
		FieldEntity field = addField("field1", FieldType.TEXT, "text", form);
		assertNotNull(field.getId());
		FieldEntity field2 = getDao().getFieldDao().getById(field.getId());
		field2.setTitle("update");
		getDao().getFieldDao().save(field2);
		FieldEntity field3 = getDao().getFieldDao().getById(field.getId());
		assertEquals("update", field3.getTitle());
	}
	
	public void testGetByForm() {
		FormEntity form = addForm("form");
		addField("field1", FieldType.TEXT, "text1", form);
		addField("field2", FieldType.TEXT, "text2", form);
		addField("field3", FieldType.TEXT, "text3", form);
		FormEntity form2 = addForm("form");
		addField("field21", FieldType.TEXT, "text21", form2);
		addField("field22", FieldType.TEXT, "text22", form2);
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		assertEquals(3, fields.size());
		fields = getDao().getFieldDao().getByForm(form2);
		assertEquals(2, fields.size());
	}
	
	public void testGetByName() {
		FormEntity form = addForm("form");
		addField("field1", FieldType.TEXT, "text1", form);
		addField("field2", FieldType.TEXT, "text2", form);
		addField("field3", FieldType.TEXT, "text3", form);
		FormEntity form2 = addForm("form");
		addField("field21", FieldType.TEXT, "text21", form2);
		addField("field22", FieldType.TEXT, "text22", form2);
		FieldEntity field = getDao().getFieldDao().getByName(form, "field3");
		assertNotNull(field);
		assertEquals("field3", field.getName());
	}
	
	
}
