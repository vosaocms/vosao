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

	public void testSelectByUrl() {
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
		List<ContentPermissionEntity> list = getDao().getContentPermissionDao()
				.selectByUrl("/");
		assertEquals(3, list.size());
	}	
	
}
