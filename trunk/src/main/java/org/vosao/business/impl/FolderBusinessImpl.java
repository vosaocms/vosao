package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.business.FolderBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

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
	
	@Override
	public TreeItemDecorator<FolderEntity> findFolderByPath(
			TreeItemDecorator<FolderEntity> root, final String path) {
		String[] names = path.split("/");
		TreeItemDecorator<FolderEntity> current = root;
		for (String name : names) {
			if (!StringUtil.isEmpty(name)) {
				TreeItemDecorator<FolderEntity> child = findByChildName(
					current, name);
				if (child == null) {
					return null;
				}
				current = child;
			}
		}
		return current;
	}
	
	private TreeItemDecorator<FolderEntity> findByChildName(
			final TreeItemDecorator<FolderEntity> folder, final String name) {
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			if (child.getEntity().getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	public List<String> validateBeforeUpdate(final FolderEntity folder) {
		List<String> errors = new ArrayList<String>();
		if (folder.getId() == null) {
			FolderEntity myFolder = getDao().getFolderDao().getByParentName(
					folder.getParent(), folder.getName());
			if (myFolder != null) {
				errors.add("Folder with such name already exists");
			}
		}
		if (StringUtil.isEmpty(folder.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(folder.getName())) {
			errors.add("Name is empty");
		}
		return errors;
	}
	
}
