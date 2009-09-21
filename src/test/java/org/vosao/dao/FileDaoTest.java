package org.vosao.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.vosao.entity.FileChunkEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;

public class FileDaoTest extends AbstractDaoTest {

	private FolderEntity addFolder(final String name) {
		FolderEntity folder = new FolderEntity(name);
		getDao().getFolderDao().save(folder);
		return folder;
	}

	private FileEntity addFile(final String title, final String name, 
			final String contentType,final byte[] data, 
			final FolderEntity folder) {
		FileEntity file = new FileEntity(title, name, folder.getId(), 
				contentType, new Date(), data.length);
		getDao().getFileDao().save(file);
		getDao().getFileDao().saveFileContent(file, data);
		return file;
	}
	
	public void testSave() {
		FolderEntity folder = addFolder("test");
		addFile("title", "test.bat", "text/plain", "file content".getBytes(), 
				folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(1, files.size());
		FileEntity file1 = files.get(0);
		assertEquals("title", file1.getTitle());
	}	
	
	public void testGetById() {
		FolderEntity folder = addFolder("test");
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertNotNull("blob data not null", file2);
		assertEquals(file.getTitle(), file2.getTitle());
		assertEquals(file.getMimeType(), file2.getMimeType());
		assertEquals(file.getFilename(), file2.getFilename());
	}	
	
	public void testBlobStore() {
		FolderEntity folder = addFolder("test");
		byte[] c = "file content".getBytes();
		FileEntity file = addFile("title", "test.bat", "text/plain", c, folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		byte[] content = getDao().getFileDao().getFileContent(file2);
		assertNotNull("blob data not null", file2);
		assertNotNull("blob data not null", content);
		for (int i=0; i < content.length; i++) {
			assertEquals(content[i], c[i]);
		}
	}	

	public void testSelect() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), folder);
		addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(
				folder.getId());
		assertEquals(3, files.size());
	}	
	
	public void testUpdate() {
		FolderEntity folder = addFolder("test");
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		file2.setTitle("update");
		getDao().getFileDao().save(file2);
		FileEntity file3 = getDao().getFileDao().getById(file.getId());
		assertEquals("update", file3.getTitle());
	}
	
	public void testDelete() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), folder);
		FileEntity file = addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(3, files.size());
		getDao().getFileDao().remove(file.getId());
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> list = getDao().getFileDao().getByFolder(folder.getId());
		assertEquals(2, list.size());
	}
	
	public void testGetByName() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), 
				folder);
		addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), 
				folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), 
				folder);
		FileEntity file = getDao().getFileDao().getByName(folder.getId(), 
				"test.bat2");
		assertNotNull(file);
		assertEquals("test.bat2", file.getFilename());
	}	
	
	public void testSaveFileEntity() {
		FolderEntity folder = addFolder("test");
		byte[] content = new byte[1200000];
		Arrays.fill(content, (byte)123);
		FileEntity file = addFile("title1", "test.bat1", "text/plain", content, 
				folder);
		byte[] content1 = getDao().getFileDao().getFileContent(file);
		assertEquals(content.length, content1.length);
		
	}

	public void testCreateChunks() {
		FolderEntity folder = addFolder("test");
		byte[] content = new byte[200000];
		Arrays.fill(content, (byte)123);
		FileEntity file = new FileEntity("title1", "test.bat1", folder.getId(),
				"text/plain", new Date(), content.length);
		List<FileChunkEntity> list = getDao().getFileDao().createChunks(file, 
				content);
		assertNotNull(list);
		assertEquals(1, list.size());
		content = new byte[1200000];
		Arrays.fill(content, (byte)100);
		list = getDao().getFileDao().createChunks(file, content);
		assertNotNull(list);
		assertEquals(2, list.size());
	}
	
}
