package org.vosao.business;

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
	
}
