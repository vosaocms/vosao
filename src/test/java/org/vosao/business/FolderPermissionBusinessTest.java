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

package org.vosao.business;

import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.ContentPermissionType;
import org.vosao.enums.FolderPermissionType;
import org.vosao.enums.UserRole;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionBusinessTest extends AbstractBusinessTest {

	private FolderEntity addFolder(String title, FolderEntity parent) {
		FolderEntity folder = new FolderEntity(title, 
				parent != null ? parent.getId() : null);
		getDao().getFolderDao().save(folder);
		return folder;
	}
	
	private UserEntity addUser(String name, String email, UserRole role) {
		UserEntity user = new UserEntity(name, name, email, role);
		getDao().getUserDao().save(user);
		return user;
	}
	
	private GroupEntity addGroup(String name) {
		GroupEntity group = new GroupEntity(name);
		getDao().getGroupDao().save(group);
		return group;
	}
	
	private void addUserGroup(UserEntity user, GroupEntity group) {
		UserGroupEntity userGroup = new UserGroupEntity(group.getId(), 
				user.getId());
		getDao().getUserGroupDao().save(userGroup);
	}
	
	private void addPermission(FolderEntity folder, GroupEntity group, 
			FolderPermissionType perm) {
		FolderPermissionEntity p = new FolderPermissionEntity(folder.getId(),
				perm, group.getId());
		getDao().getFolderPermissionDao().save(p);
	}
	
	public void testGetPermission() {
		UserEntity developer = addUser("alex","kinyelo@gmail.com", UserRole.USER); 
		UserEntity admin = addUser("admin","admin@gmail.com", UserRole.ADMIN); 
		UserEntity manager = addUser("test1","test1@gmail.com", UserRole.USER); 
		UserEntity dev2 = addUser("test2","test2@gmail.com", UserRole.USER);
		GroupEntity guests = addGroup("guests");
		GroupEntity managers = addGroup("managers");
		GroupEntity developers = addGroup("developers");
		addUserGroup(developer, developers);
		addUserGroup(manager, managers);
		addUserGroup(dev2, developers);
		addUserGroup(dev2, managers);
		FolderEntity home = addFolder("home", null);
		FolderEntity about = addFolder("about", home);
		FolderEntity company = addFolder("company", about);
		FolderEntity team = addFolder("team", about);
		FolderEntity services = addFolder("services", home);
		FolderEntity php = addFolder("php", services);
		addPermission(home, guests, FolderPermissionType.READ);
		addPermission(about, guests, FolderPermissionType.DENIED);
		addPermission(about, developers, FolderPermissionType.READ);
		addPermission(team, developers, FolderPermissionType.WRITE);
		FolderPermissionEntity p = getBusiness().getFolderPermissionBusiness()
				.getPermission(home, developer);
		assertEquals(FolderPermissionType.READ, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getPermission(
				about, developer);
		assertEquals(FolderPermissionType.READ, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getPermission(
				team, developer);
		assertEquals(FolderPermissionType.WRITE, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getGuestPermission(
				home);
		assertEquals(FolderPermissionType.READ, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getGuestPermission(
				about);
		assertEquals(FolderPermissionType.DENIED, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getGuestPermission(
				team);
		assertEquals(FolderPermissionType.DENIED, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getGuestPermission(
				services);
		assertEquals(FolderPermissionType.READ, p.getPermission());
		p = getBusiness().getFolderPermissionBusiness().getGuestPermission(
				php);
		assertEquals(FolderPermissionType.READ, p.getPermission());
	}
	
	
	
}
