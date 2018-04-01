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

package org.vosao.dao;

import org.vosao.dao.tool.FormTool;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;

public class FormDaoTest extends AbstractDaoTest {

	private FormTool formTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        formTool = new FormTool(getDao());
	}    
	
	public void testGetByName() {
		formTool.addForm("john");
		formTool.addForm("test");
		formTool.addForm("jack");
		FormEntity f = getDao().getFormDao().getByName(null);
		assertNull(f);
		f = getDao().getFormDao().getByName("test");
		assertEquals("test", f.getName());
	}

	public void testGetConfig() {
		FormConfigEntity c = getDao().getFormConfigDao().getConfig();
		assertNotNull(c);
		c.setFormTemplate("template");
		getDao().getFormConfigDao().save(c);
		c = getDao().getFormConfigDao().getConfig();
		assertEquals("template", c.getFormTemplate());
		c.setFormTemplate("template2");
		getDao().getFormConfigDao().save(c);
		c = getDao().getFormConfigDao().getConfig();
		assertEquals("template2", c.getFormTemplate());
	}
	
}
