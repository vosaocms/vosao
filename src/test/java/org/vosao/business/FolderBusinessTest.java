package org.vosao.business;

import java.io.UnsupportedEncodingException;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;

public class FolderBusinessTest extends AbstractBusinessTest {

	private FolderEntity addFolder(final String name, final String parent) {
		FolderEntity Folder = new FolderEntity(name, parent);
		getDao().getFolderDao().save(Folder);
		return Folder;
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
		addFolder("/", null);
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
