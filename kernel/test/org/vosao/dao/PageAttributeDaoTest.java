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

import java.util.List;

import org.vosao.dao.tool.PageAttributeTool;
import org.vosao.entity.PageAttributeEntity;

public class PageAttributeDaoTest extends AbstractDaoTest {

	private PageAttributeTool tool;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        tool = new PageAttributeTool(getDao());
	}    
	
	public void testSave() {
		tool.addPageAttribute("/", "name", true, "");
		List<PageAttributeEntity> list = getDao().getPageAttributeDao().select();
		assertEquals(1, list.size());
		PageAttributeEntity attr1 = list.get(0);
		assertEquals("name", attr1.getName());
	}	

	public void testGetByPage() {
		tool.addPageAttribute("/", "name1", true, "");
		tool.addPageAttribute("/", "name2", true, "");
		tool.addPageAttribute("/", "dog1", false, "");
		tool.addPageAttribute("/", "dog2", false, "");
		tool.addPageAttribute("/", "dog3", false, "");
		List<PageAttributeEntity> list = getDao().getPageAttributeDao()
				.getByPage("/");
		assertEquals(5, list.size());
		list = getDao().getPageAttributeDao().getByPageInherited("/");
		assertEquals(2, list.size());
	}
	
}
