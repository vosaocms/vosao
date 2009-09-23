package org.vosao.business;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;


public interface FolderBusiness {

	TreeItemDecorator<FolderEntity> getTree(final List<FolderEntity> list);

	TreeItemDecorator<FolderEntity> getTree();
	
	TreeItemDecorator<FolderEntity> findFolderByPath(
			final TreeItemDecorator<FolderEntity> root, final String path);
	
	List<String> validateBeforeUpdate(final FolderEntity folder);
	
	/**
	 * Create all directories in the path.
	 * @param path - directories path.
	 * @return last folder in the path.
	 * @throws UnsupportedEncodingException
	 */
	FolderEntity createFolder(final String path) throws UnsupportedEncodingException; 
	
	String getFolderPath(final FolderEntity folder);
	String getFolderPath(final FolderEntity folder, 
			TreeItemDecorator<FolderEntity> root);
	
	void recursiveRemove(final List<String> folderIds);
}
