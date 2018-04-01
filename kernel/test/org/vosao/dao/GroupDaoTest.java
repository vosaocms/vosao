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

import org.vosao.dao.tool.GroupTool;
import org.vosao.dao.tool.UserTool;
import org.vosao.entity.GroupEntity;
import org.vosao.enums.UserRole;

public class GroupDaoTest extends AbstractDaoTest {

	private GroupTool groupTool;
	private UserTool userTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        groupTool = new GroupTool(getDao());
        userTool = new UserTool(getDao());
	}    
	
	public void testSelectByGroup() {
		GroupEntity group1 = groupTool.addGroup("group1");
		GroupEntity group2 = groupTool.addGroup("group2");
		groupTool.addUserGroup(group1.getId(), 
				userTool.addUser("roma", UserRole.USER).getId());
		groupTool.addUserGroup(group1.getId(), 
				userTool.addUser("sasha", UserRole.USER).getId());
		groupTool.addUserGroup(group2.getId(), 
				userTool.addUser("alex1", UserRole.USER).getId());
		groupTool.addUserGroup(group2.getId(), 
				userTool.addUser("alex2", UserRole.USER).getId());
		groupTool.addUserGroup(group2.getId(), 
				userTool.addUser("alex3", UserRole.USER).getId());
	}
	
}
