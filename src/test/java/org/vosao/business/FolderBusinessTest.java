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

import java.io.UnsupportedEncodingException;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.FolderPermissionType;
import org.vosao.enums.UserRole;

public class FolderBusinessTest extends AbstractBusinessTest {

	private FolderEntity addFolder(final String name, final String parent) {
		FolderEntity Folder = new FolderEntity(name, parent);
		getDao().getFolderDao().save(Folder);
		return Folder;
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

	private void addGuestPerission(FolderEntity root) {
		UserEntity tester = addUser("tester","kinyelo@gmail.com", UserRole.ADMIN);
		CurrentUser.setInstance(tester);
		GroupEntity guests = addGroup("guests");
		GroupEntity developers = addGroup("developers");
		addUserGroup(tester, developers);
		addPermission(root, guests, FolderPermissionType.READ);
	}
	
	/**
	 * /images/
	 *        /logos/
	 *              /vosao 
	 *        
	 *        /photos
	 *        /test
	 */
	public void testFindFolderByPath() {
		FolderEntity root = addFolder("/", null);
		addGuestPerission(root);
		FolderEntity images = addFolder("images", root.getId());
		FolderEntity logos = addFolder("logos", images.getId());
		FolderEntity photos = addFolder("photos", images.getId());
		FolderEntity test = addFolder("test", images.getId());
		FolderEntity vosao = addFolder("vosao", logos.getId());
		TreeItemDecorator<FolderEntity> treeRoot = getBusiness().getFolderBusiness()
				.getTree();
		
		TreeItemDecorator<FolderEntity> result = getBusiness().getFolderBusiness()
				.findFolderByPath(treeRoot, "/images");
		assertNotNull(result);
		assertEquals(images.getId(), result.getEntity().getId());
		
		result = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
				"/images/logos");
		assertNotNull(result);
		assertEquals(logos.getId(), result.getEntity().getId());
		
		result = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
				"/images/photos");
		assertNotNull(result);
		assertEquals(photos.getId(), result.getEntity().getId());

		result = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
				"/images/test");
		assertNotNull(result);
		assertEquals(test.getId(), result.getEntity().getId());

		result = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
			"/images/logos/vosao");
		assertNotNull(result);
		assertEquals(vosao.getId(), result.getEntity().getId());

		result = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
			"/images/logos/vosao1");
		assertNull(result);
	}	

	public void testCreateFolder() throws UnsupportedEncodingException {
		FolderEntity root = addFolder("/", null);
		addGuestPerission(root);
		getBusiness().getFolderBusiness().createFolder("/one/two/free/four");
		TreeItemDecorator<FolderEntity> treeRoot = getBusiness()
			.getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> folder = getBusiness()
			.getFolderBusiness().findFolderByPath(treeRoot,	"/one");
		assertNotNull(folder);
		assertEquals("one", folder.getEntity().getName());

		folder = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
				"/one/two");
		assertNotNull(folder);
		assertEquals("two", folder.getEntity().getName());

		folder = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
			"/one/two/free");
		assertNotNull(folder);
		assertEquals("free", folder.getEntity().getName());

		folder = getBusiness().getFolderBusiness().findFolderByPath(treeRoot, 
			"/one/two/free/four");
		assertNotNull(folder);
		assertEquals("four", folder.getEntity().getName());
	}
	
	/**
	 * /images/
	 *        /logos/
	 *              /vosao 
	 *        
	 *        /photos
	 *        /test
	 */
	public void testGetFolderPath() {
		FolderEntity root = addFolder("/", null);
		addGuestPerission(root);
		FolderEntity images = addFolder("images", root.getId());
		FolderEntity logos = addFolder("logos", images.getId());
		FolderEntity photos = addFolder("photos", images.getId());
		FolderEntity test = addFolder("test", images.getId());
		FolderEntity vosao = addFolder("vosao", logos.getId());
		assertEquals("/images", getBusiness().getFolderBusiness()
				.getFolderPath(images));
		assertEquals("/images/logos", getBusiness().getFolderBusiness()
				.getFolderPath(logos));
		assertEquals("/images/logos/vosao", getBusiness().getFolderBusiness()
				.getFolderPath(vosao));
		assertEquals("/images/photos", getBusiness().getFolderBusiness()
				.getFolderPath(photos));
	}
}
