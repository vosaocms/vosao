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
import java.util.List;

import org.vosao.dao.tool.FolderPermissionTool;
import org.vosao.dao.tool.FolderTool;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.enums.FolderPermissionType;

public class FolderPermissionDaoTest extends AbstractDaoTest {

	private FolderPermissionTool folderPermissionTool;
	private FolderTool folderTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        folderTool = new FolderTool(getDao());
        folderPermissionTool = new FolderPermissionTool(getDao());
	}    
	
	public void testSelectByFolder() {
		FolderEntity folder = folderTool.addFolder("test", null);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.READ, 1L);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.WRITE, 2L);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.ADMIN, 3L);
		List<FolderPermissionEntity> list = getDao().getFolderPermissionDao()
				.selectByFolder(folder.getId());
		assertEquals(3, list.size());
	}	
	
	public void testSelectByGroup() {
		FolderEntity folder = folderTool.addFolder("test", null);
		FolderEntity folder2 = folderTool.addFolder("test2", null);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.READ, 1L);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.WRITE, 2L);
		folderPermissionTool.addPermission(folder.getId(), 
				FolderPermissionType.ADMIN, 3L);
		folderPermissionTool.addPermission(folder2.getId(), 
				FolderPermissionType.READ, 1L);
		folderPermissionTool.addPermission(folder2.getId(), 
				FolderPermissionType.WRITE, 2L);
		folderPermissionTool.addPermission(folder2.getId(), 
				FolderPermissionType.ADMIN, 3L);
		List<Long> ids = new ArrayList<Long>();
		ids.add(1L);
		getDao().getFolderPermissionDao().removeByGroup(ids);
		List<FolderPermissionEntity> list = getDao().getFolderPermissionDao()
				.selectByFolder(folder.getId());
		assertEquals(2, list.size());
		list = getDao().getFolderPermissionDao().selectByFolder(folder2.getId());
		assertEquals(2, list.size());
	}	
	
}
