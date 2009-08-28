package org.vosao.servlet;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

public class FolderUtilTest extends TestCase {

	public void testGetPathChain() throws UnsupportedEncodingException {
		String path = "/one/two/free/image.jpg";
		String[] chain = FolderUtil.getPathChain(path);
		assertNotNull(chain);
		assertEquals(4, chain.length);
		assertEquals("one", chain[0]);
		assertEquals("two", chain[1]);
		assertEquals("free", chain[2]);
		assertEquals("image.jpg", chain[3]);
	}

	public void testGetFolderChain() throws UnsupportedEncodingException {
		String path = "/one/two/free/image.jpg";
		String[] chain = FolderUtil.getPathChain(path);
		assertNotNull(chain);
		String[] folderChain = FolderUtil.getFolderChain(chain);
		assertNotNull(folderChain);
		assertEquals(3, folderChain.length);
		assertEquals("one", folderChain[0]);
		assertEquals("two", folderChain[1]);
		assertEquals("free", folderChain[2]);
	}
	
}
