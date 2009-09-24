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

import org.vosao.entity.FolderEntity;

public class FolderDaoTest extends AbstractDaoTest {

	private FolderEntity addFolder(final String name, final String parent) {
		FolderEntity Folder = new FolderEntity(name, parent);
		getDao().getFolderDao().save(Folder);
		return Folder;
	}
	
	public void testSave() {
		addFolder("test", null);
		List<FolderEntity> folders = getDao().getFolderDao().select();
		assertEquals(1, folders.size());
		FolderEntity folder1 = folders.get(0);
		assertEquals("test", folder1.getName());
	}	
	
	public void testGetById() {
		FolderEntity folder = addFolder("test", null);
		FolderEntity folder2 = getDao().getFolderDao().getById(folder.getId());
		assertEquals(folder.getName(), folder2.getName());
	}	
	
	public void testSelect() {
		addFolder("test1", null);
		addFolder("test2", null);
		addFolder("test3", null);
		List<FolderEntity> Folders = getDao().getFolderDao().select();
		assertEquals(3, Folders.size());
	}	
	
	public void testUpdate() {
		FolderEntity folder = addFolder("test", null);
		FolderEntity folder2 = getDao().getFolderDao().getById(folder.getId());
		folder2.setName("update");
		getDao().getFolderDao().save(folder2);
		FolderEntity folder3 = getDao().getFolderDao().getById(folder.getId());
		assertEquals("update", folder3.getName());
	}
	
	public void testResultList() {
		addFolder("test1", null);
		addFolder("test2", null);
		addFolder("test3", null);
		List<FolderEntity> list = getDao().getFolderDao().select();
		FolderEntity Folder = new FolderEntity("test4");
		list.add(Folder);
		assertEquals(4, list.size());
	}
	
	public void testGetByParent() {
		FolderEntity root = addFolder("root", null);
		addFolder("title1", root.getId());
		addFolder("title2", null);
		addFolder("title3", root.getId());
		List<FolderEntity> list = getDao().getFolderDao().getByParent(root.getId());
		assertEquals(2, list.size());
	}	

	public void testGetByParentName() {
		FolderEntity root = addFolder("root", null);
		addFolder("title1", root.getId());
		addFolder("title2", null);
		addFolder("title3", root.getId());
		FolderEntity result = getDao().getFolderDao().getByParentName(
				root.getId(), "title1");
		assertNotNull(result);
		assertEquals("title1", result.getName());
		result = getDao().getFolderDao().getByParentName(root.getId(), "title3");
		assertNotNull(result);
		assertEquals("title3", result.getName());
		result = getDao().getFolderDao().getByParentName(root.getId(), "title2");
		assertNull(result);
	}	
	
}
