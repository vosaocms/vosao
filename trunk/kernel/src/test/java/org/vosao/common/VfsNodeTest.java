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
