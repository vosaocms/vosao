package org.vosao.dao;

import java.util.List;

import org.vosao.entity.FileEntity;

public class FileDaoTest extends AbstractDaoTest {

	private FileEntity addFile(final String title, final String name, 
			final String contentType,final byte[] data) {
		FileEntity file = new FileEntity(title, name, contentType, data);
		getDao().getFileDao().save(file);
		return file;
	}
	
	public void testSave() {
		addFile("title", "test.bat", "text/plain", "file content".getBytes());
		List<FileEntity> files = getDao().getFileDao().select();
		assertEquals(1, files.size());
		FileEntity File1 = files.get(0);
		assertEquals("title", File1.getTitle());
	}	
	
	public void testGetById() {
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes());
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertEquals(file.getTitle(), file2.getTitle());
		assertNotNull("blob data not null", file2.getFile());
		assertEquals(file.getFile().getMimeType(), file2.getFile().getMimeType());
		assertEquals(file.getFile().getFilename(), file2.getFile().getFilename());
	}	
	
	public void testBlobStore() {
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes());
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		assertNotNull("blob data not null", file2.getFile());
		assertNotNull("blob data not null", file2.getFile().getContent());
		for (int i=0; i < file.getFile().getContent().length; i++) {
			assertEquals(file.getFile().getContent()[i], 
					file2.getFile().getContent()[i]);
		}
	}	

	public void testSelect() {
		addFile("title1", "test.bat1", "text/plain", "file content1".getBytes());
		addFile("title2", "test.bat2", "text/plain", "file content2".getBytes());
		addFile("title3", "test.bat3", "text/plain", "file content3".getBytes());
		List<FileEntity> files = getDao().getFileDao().select();
		assertEquals(3, files.size());
	}	
	
	public void testUpdate() {
		FileEntity file = addFile("title", "test.bat", "text/plain", 
				"file content".getBytes());
		FileEntity file2 = getDao().getFileDao().getById(file.getId());
		file2.setTitle("update");
		getDao().getFileDao().save(file2);
		FileEntity file3 = getDao().getFileDao().getById(file.getId());
		assertEquals("update", file3.getTitle());
	}
	
}
