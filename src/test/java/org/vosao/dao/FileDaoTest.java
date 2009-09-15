package org.vosao.dao;

import java.util.Date;
import java.util.List;

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
		FileEntity file = new FileEntity(title, name, contentType, new Date(),
				data, folder);
		folder.addFile(file);
		getDao().getFileDao().save(file);
		return file;
	}
	
	public void testSave() {
		FolderEntity folder = addFolder("test");
		addFile("title", "test.bat", "text/plain", "file content".getBytes(), 
				folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		assertEquals(1, folder.getFiles().size());
		FileEntity file1 = folder.getFiles().get(0);
		assertEquals("title", file1.getTitle());
	}	
	
	public void testGetById() {
		FolderEntity folder = addFolder("test");
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertEquals(file.getTitle(), file2.getTitle());
		assertNotNull("blob data not null", file2.getFile());
		assertEquals(file.getFile().getMimeType(), file2.getFile().getMimeType());
		assertEquals(file.getFile().getFilename(), file2.getFile().getFilename());
	}	
	
	public void testBlobStore() {
		FolderEntity folder = addFolder("test");
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes(), folder);
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertNotNull("blob data not null", file2.getFile());
		assertNotNull("blob data not null", file2.getFile().getContent());
		for (int i=0; i < file.getFile().getContent().length; i++) {
			assertEquals(file.getFile().getContent()[i], 
					file2.getFile().getContent()[i]);
		}
	}	

	public void testSelect() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), folder);
		addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> files = folder.getFiles();
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
	
	public void testResultList() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), folder);
		addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> list = folder.getFiles();
		FileEntity file = new FileEntity("title4", "test.bat4", "text/plain", 
				new Date(), "file content4".getBytes(), folder);
		list.add(file);
		assertEquals(4, list.size());
	}

	public void testDelete() {
		FolderEntity folder = addFolder("test");
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes(), folder);
		FileEntity file = addFile("title2", "test.bat2", "text/plain", "file content2".getBytes(), folder);
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes(), folder);
		folder = getDao().getFolderDao().getById(folder.getId());
		assertEquals(3, folder.getFiles().size());
		getDao().getFileDao().remove(file.getId());
		folder = getDao().getFolderDao().getById(folder.getId());
		List<FileEntity> list = folder.getFiles();
		assertEquals(2, list.size());
	}
	
	
}
