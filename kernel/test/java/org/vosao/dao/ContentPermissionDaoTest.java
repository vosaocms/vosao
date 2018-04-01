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

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.tool.ContentPermissionTool;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.enums.ContentPermissionType;

public class ContentPermissionDaoTest extends AbstractDaoTest {

	private ContentPermissionTool contentPermissionTool;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        contentPermissionTool = new ContentPermissionTool(getDao());
	}    

	private void createTestCase() {
		contentPermissionTool.addPermission("/", ContentPermissionType.ADMIN, 
				1L);
		contentPermissionTool.addPermission("/", ContentPermissionType.WRITE, 
				2L);
		contentPermissionTool.addPermission("/", ContentPermissionType.READ, 
				3L);
		contentPermissionTool.addPermission("/man", ContentPermissionType.READ, 
				3L);
		contentPermissionTool.addPermission("/do/man", ContentPermissionType.READ, 
				3L);
		contentPermissionTool.addPermission("/test", ContentPermissionType.READ, 
				3L);
	}	

	public void testSelectByUrl() {
		createTestCase();
		List<ContentPermissionEntity> list = getDao().getContentPermissionDao()
				.selectByUrl("/");
		assertEquals(3, list.size());
	}	

	public void testRemoveByGroup() {
		createTestCase();
		List<Long> groupIds = new ArrayList<Long>();
		groupIds.add(3L);
		groupIds.add(2L);
		getDao().getContentPermissionDao().removeByGroup(groupIds);
		List<ContentPermissionEntity> list = getDao().getContentPermissionDao()
				.selectByUrl("/");
		assertEquals(1, list.size());
		list = getDao().getContentPermissionDao().select();
		assertEquals(1, list.size());
	}	
	
}
