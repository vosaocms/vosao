package org.vosao.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.business.FolderBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;

public class FolderBusinessImpl extends AbstractBusinessImpl 
	implements FolderBusiness {

	@Override
	public TreeItemDecorator<FolderEntity> getTree(
			final List<FolderEntity> folders) {
		Map<String, TreeItemDecorator<FolderEntity>> buf = 
			new HashMap<String, TreeItemDecorator<FolderEntity>>();
		for (FolderEntity page : folders) {
			buf.put(page.getId(), new TreeItemDecorator<FolderEntity>(page));
		}
		TreeItemDecorator<FolderEntity> root = null;
		for (String id : buf.keySet()) {
			TreeItemDecorator<FolderEntity> folder = buf.get(id);
			if (folder.getEntity().getParent() == null) {
				root = folder;
			}
			else {
				TreeItemDecorator<FolderEntity> parent = buf.get(
						folder.getEntity().getParent());
				if (parent != null) {
					parent.getChildren().add(folder);
				}
			}
		}
		return root;
	}

	@Override
	public TreeItemDecorator<FolderEntity> getTree() {
		return getTree(getDao().getFolderDao().select());
	}
	
}
