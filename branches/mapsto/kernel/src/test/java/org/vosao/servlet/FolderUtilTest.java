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

package org.vosao.servlet;

import java.io.UnsupportedEncodingException;

import org.vosao.utils.FolderUtil;

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
	
	public void testGetFileExt() {
		assertEquals("zip", FolderUtil.getFileExt("/home/x/file.zip"));
	}

	public void testGetFilePath() {
		assertEquals("/home/x", FolderUtil.getFilePath("/home/x/file.zip"));
	}

	public void testGetFileName() {
		assertEquals("file.zip", FolderUtil.getFileName("/home/x/file.zip"));
	}

	public void testGetFolderName() {
		assertEquals("file", FolderUtil.getFolderName("/home/x/file"));
	}
}
