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

import org.vosao.dao.tool.UserTool;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public class UserDaoTest extends AbstractDaoTest {

	private UserTool userTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        userTool = new UserTool(getDao());
	}    
	
	public void testSave() {
		userTool.addUser("name", "password", "test@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		assertEquals(1, users.size());
		UserEntity user1 = users.get(0);
		assertEquals("name", user1.getName());
	}	
	
	public void testGetById() {
		UserEntity user = userTool.addUser("name","password","test@test.com", 
				UserRole.ADMIN);
		UserEntity user2 = getDao().getUserDao().getById(user.getId());
		assertEquals(user.getName(), user2.getName());
		assertEquals(user.getPassword(), user2.getPassword());
		assertEquals(user.getEmail(), user2.getEmail());
		assertEquals(user.getRole(), user2.getRole());
	}	

	public void testSelect() {
		userTool.addUser("name1", "password1", "test1@test.com", UserRole.ADMIN);
		userTool.addUser("name2", "password2", "test2@test.com", UserRole.ADMIN);
		userTool.addUser("name3", "password3", "test3@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		assertEquals(3, users.size());
	}	
	
	public void testUpdate() {
		UserEntity user = userTool.addUser("name1", "password1", "test1@test.com", 
				UserRole.ADMIN);
		UserEntity user2 = getDao().getUserDao().getById(user.getId());
		user2.setName("name2");
		getDao().getUserDao().save(user2);
		UserEntity user3 = getDao().getUserDao().getById(user.getId());
		assertEquals("name2", user3.getName());
	}
	
	public void testResultList() {
		userTool.addUser("name1", "password1", "test1@test.com", UserRole.USER);
		userTool.addUser("name2", "password2", "test2@test.com", UserRole.USER);
		userTool.addUser("name3", "password3", "test3@test.com", UserRole.USER);
		List<UserEntity> users = getDao().getUserDao().select();
		UserEntity user = new UserEntity("name", "password", "test@test.com", UserRole.ADMIN);
		users.add(user);
		assertEquals(4, users.size());
	}

	public void testGetByEmail() {
		userTool.addUser("name1","password1","test1@test.com", UserRole.ADMIN);
		userTool.addUser("name2","password2","test2@test.com", UserRole.USER);
		userTool.addUser("name3","password3","test3@test.com", UserRole.ADMIN);
		UserEntity user1 = getDao().getUserDao().getByEmail("test2@test.com");
		assertNotNull(user1);
		assertEquals("name2", user1.getName());
		assertEquals("password2", user1.getPassword());
		assertEquals(UserRole.USER, user1.getRole());
		UserEntity user2 = getDao().getUserDao().getByEmail("test22@test.com");
		assertNull(user2);
	}	
	
	public void testGetByRole() {
		userTool.addUser("name1","password1","test1@test.com", UserRole.ADMIN);
		userTool.addUser("name2","password2","test2@test.com", UserRole.USER);
		userTool.addUser("name3","password3","test3@test.com", UserRole.ADMIN);
		List<UserEntity> users1 = getDao().getUserDao().getByRole(UserRole.ADMIN);
		assertEquals(2, users1.size());
		List<UserEntity> users2 = getDao().getUserDao().getByRole(UserRole.USER);
		assertEquals(1, users2.size());
	}	
	
}
