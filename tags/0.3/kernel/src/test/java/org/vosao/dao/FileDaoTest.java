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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.vosao.dao.tool.FileTool;
import org.vosao.dao.tool.FolderTool;
import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.utils.StreamUtil;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class FileDaoTest extends AbstractDaoTest {

	private FolderTool folderTool;
	private FileTool fileTool;

	@Override
    public void setUp() throws Exception {
        super.setUp();
        folderTool = new FolderTool(getDao());
        fileTool = new FileTool(getDao());
	}    

	public void testSave() {
		FolderEntity folder = folderTool.addFolder("test");
		fileTool.addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(1, files.size());
		FileEntity file1 = files.get(0);
		assertEquals("title", file1.getTitle());
		assertEquals(12, file1.getSize());
		assertEquals("file content", 
				new String(getDao().getFileDao().getFileContent(file1)));
	}	
	
	public void testGetById() {
		FolderEntity folder = folderTool.addFolder("test");
		FileEntity file = fileTool.addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertNotNull("blob data not null", file2);
		assertEquals(file.getTitle(), file2.getTitle());
		assertEquals(file.getMimeType(), file2.getMimeType());
		assertEquals(file.getFilename(), file2.getFilename());
	}	
	
	public void testBlobStore() {
		FolderEntity folder = folderTool.addFolder("test");
		byte[] c = "file content".getBytes();
		FileEntity file = fileTool.addFile("title", "test.bat", "text/plain", 
				c, folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		byte[] content = getDao().getFileDao().getFileContent(file2);
		assertNotNull("blob data not null", file2);
		assertNotNull("blob data not null", content);
		for (int i=0; i < content.length; i++) {
			assertEquals(content[i], c[i]);
		}
	}	

	public void testSelect() {
		FolderEntity folder = folderTool.addFolder("test");
		fileTool.addFile("title1", "test.bat1", "text/plain", 
				"file content1".getBytes(), folder);
		fileTool.addFile("title2", "test.bat2", "text/plain", 
				"file content2".getBytes(), folder);
		fileTool.addFile("title3", "test.bat3", "text/plain", 
				"file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(
				folder.getId());
		assertEquals(3, files.size());
	}	
	
	public void testUpdate() {
		FolderEntity folder = folderTool.addFolder("test");
		FileEntity file = fileTool.addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		file2.setTitle("update");
		getDao().getFileDao().save(file2);
		FileEntity file3 = getDao().getFileDao().getById(file.getId());
		assertEquals("update", file3.getTitle());
	}
	
	public void testDelete() {
		FolderEntity folder = folderTool.addFolder("test");
		fileTool.addFile("title1", "test.bat1", "text/plain", 
				"file content1".getBytes(), folder);
		FileEntity file = fileTool.addFile("title2", "test.bat2", "text/plain", 
				"file content2".getBytes(), folder);
		fileTool.addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(3, files.size());
		getDao().getFileDao().remove(file.getId());
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> list = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(2, list.size());
		Long id = null;
		getDao().getFileDao().remove(id);

	}
	
	public void testGetByName() {
		FolderEntity folder = folderTool.addFolder("test");
		fileTool.addFile("title1", "test.bat1", "text/plain", 
				"file content1".getBytes(),	folder);
		fileTool.addFile("title2", "test.bat2", "text/plain", 
				"file content2".getBytes(),	folder);
		fileTool.addFile("title3", "test.bat3", "text/plain", 
				"file content3".getBytes(),	folder);
		FileEntity file = getDao().getFileDao().getByName(folder.getId(), 
				"test.bat2");
		assertNotNull(file);
		assertEquals("test.bat2", file.getFilename());
	}	
	
	public void testSaveFileEntity() {
		FolderEntity folder = folderTool.addFolder("test");
		byte[] content = new byte[1200000];
		Arrays.fill(content, (byte)123);
		FileEntity file = fileTool.addFile("title1", "test.bat1", "text/plain", 
				content, folder);
		byte[] content1 = getDao().getFileDao().getFileContent(file);
		assertEquals(content.length, content1.length);
		
	}

	public void testCreateChunks() {
		FolderEntity folder = folderTool.addFolder("test");
		byte[] content = new byte[200000];
		Arrays.fill(content, (byte)123);
		FileEntity file = new FileEntity("title1", "test.bat1", folder.getId(),
				"text/plain", new Date(), content.length);
		List<FileChunkEntity> list = getDao().getFileChunkDao().createChunks(file, 
				content);
		assertNotNull(list);
		assertEquals(1, list.size());
		content = new byte[1200000];
		Arrays.fill(content, (byte)100);
		list = getDao().getFileChunkDao().createChunks(file, content);
		assertNotNull(list);
		assertEquals(2, list.size());
		getDao().getFileDao().save(file, content);
		byte[] cont = getDao().getFileDao().getFileContent(file);
		assertEquals(content.length, cont.length);	
	}

	public void testRemove() {
		FolderEntity folder = folderTool.addFolder("test");
		FileEntity t1 = fileTool.addFile("title1", "test.bat1", "text/plain", 
				"file content1".getBytes(),	folder);
		FileEntity t2 = fileTool.addFile("title2", "test.bat2", "text/plain", 
				"file content2".getBytes(),	folder);
		FileEntity t3 = fileTool.addFile("title3", "test.bat3", "text/plain", 
				"file content3".getBytes(),	folder);
		List<Long> ids = new ArrayList<Long>();
		ids.add(t1.getId());
		ids.add(t2.getId());
		getDao().getFileDao().remove(ids);
		List<FileEntity> list = getDao().getFileDao().select();
		assertEquals(1, list.size());
	}	

	public void testRemoveByFolder() {
		FolderEntity folder = folderTool.addFolder("test");
		FileEntity t1 = fileTool.addFile("title1", "test.bat1", "text/plain", 
				"file content1".getBytes(),	folder);
		FileEntity t2 = fileTool.addFile("title2", "test.bat2", "text/plain", 
				"file content2".getBytes(),	folder);
		FileEntity t3 = fileTool.addFile("title3", "test.bat3", "text/plain", 
				"file content3".getBytes(),	folder);
		List<FileEntity> list = getDao().getFileDao().select();
		assertEquals(3, list.size());
		getDao().getFileDao().removeByFolder(folder.getId());
		list = getDao().getFileDao().select();
		assertEquals(0, list.size());
	}	

	
}
