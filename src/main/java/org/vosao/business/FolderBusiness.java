package org.vosao.business;

import java.util.List;

import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;


public interface FolderBusiness {

	TreeItemDecorator<FolderEntity> getTree(final List<FolderEntity> list);

	TreeItemDecorator<FolderEntity> getTree();
	
	TreeItemDecorator<FolderEntity> findFolderByPath(
			final TreeItemDecorator<FolderEntity> root, final String path);
	
	List<String> validateBeforeUpdate(final FolderEntity folder);
}
