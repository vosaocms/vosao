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

package org.vosao.common;

import org.vosao.test.AbstractVosaoContextTest;

public class VfsNodeTest extends AbstractVosaoContextTest {

	public void testCreateDirectory() {
		VfsNode.createDirectory("/my/test/dir");
		assertNotNull(VfsNode.find("/"));
		assertNotNull(VfsNode.find("/my"));
		assertNotNull(VfsNode.find("/my/test"));
		assertNotNull(VfsNode.find("/my/test/dir"));
		VfsNode.createDirectory("/my/test/dir2");
		VfsNode.createDirectory("/my/test/dir3");
		assertEquals(3, VfsNode.find("/my/test").getChildren().size());
	}
	
	public void testSaveFind() {
		VfsNode.createDirectory("/one/test/dir");
		VfsNode node = VfsNode.find("/one/test/dir");
		assertNotNull(node);
		assertTrue(node.isDirectory());
		assertEquals("/one/test/dir", node.getPath());
	}
	
	public void testCreateFile() {
		VfsNode.createDirectory("/two/test/dir");
		VfsNode.createFile("/two/test/dir/f1.txt", "test1".getBytes());
		VfsNode.createFile("/two/test/dir/f2.txt", "test2".getBytes());
		VfsNode.createFile("/two/test/dir/f3.txt", "test3".getBytes());
		assertEquals(3, VfsNode.find("/two/test/dir").getChildren().size());
	}
	
}
