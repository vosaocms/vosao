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

import java.util.List;

import org.vosao.dao.tool.FieldTool;
import org.vosao.dao.tool.FormTool;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;
import org.vosao.enums.FieldType;

public class FieldDaoTest extends AbstractDaoTest {

	private FormTool formTool;
	private FieldTool fieldTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        formTool = new FormTool(getDao());
        fieldTool = new FieldTool(getDao());
	}    
	
	public void testSave() {
		FormEntity form = formTool.addForm("form");
		fieldTool.addField("field1", FieldType.TEXT, "text", form);
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		assertEquals(1, fields.size());
		FieldEntity field1 = fields.get(0);
		assertEquals("field1", field1.getName());
	}	
	
	public void testGetById() {
		FormEntity form = formTool.addForm("form");
		FieldEntity field = fieldTool.addField("field1", FieldType.TEXT, "text", 
				form);
		assertNotNull(field.getId());
		FieldEntity field2 = getDao().getFieldDao().getById(field.getId());
		assertEquals(field.getTitle(), field2.getTitle());
	}	

	public void testUpdate() {
		FormEntity form = formTool.addForm("form");
		FieldEntity field = fieldTool.addField("field1", FieldType.TEXT, "text", 
				form);
		assertNotNull(field.getId());
		FieldEntity field2 = getDao().getFieldDao().getById(field.getId());
		field2.setTitle("update");
		getDao().getFieldDao().save(field2);
		FieldEntity field3 = getDao().getFieldDao().getById(field.getId());
		assertEquals("update", field3.getTitle());
	}
	
	public void testGetByForm() {
		FormEntity form = formTool.addForm("form");
		fieldTool.addField("field1", FieldType.TEXT, "text1", form);
		fieldTool.addField("field2", FieldType.TEXT, "text2", form);
		fieldTool.addField("field3", FieldType.TEXT, "text3", form);
		FormEntity form2 = formTool.addForm("form");
		fieldTool.addField("field21", FieldType.TEXT, "text21", form2);
		fieldTool.addField("field22", FieldType.TEXT, "text22", form2);
		List<FieldEntity> fields = getDao().getFieldDao().getByForm(form);
		assertEquals(3, fields.size());
		fields = getDao().getFieldDao().getByForm(form2);
		assertEquals(2, fields.size());
	}
	
	public void testGetByName() {
		FormEntity form = formTool.addForm("form");
		fieldTool.addField("field1", FieldType.TEXT, "text1", form);
		fieldTool.addField("field2", FieldType.TEXT, "text2", form);
		fieldTool.addField("field3", FieldType.TEXT, "text3", form);
		FormEntity form2 = formTool.addForm("form");
		fieldTool.addField("field21", FieldType.TEXT, "text21", form2);
		fieldTool.addField("field22", FieldType.TEXT, "text22", form2);
		FieldEntity field = getDao().getFieldDao().getByName(form, "field3");
		assertNotNull(field);
		assertEquals("field3", field.getName());
	}
	
}
