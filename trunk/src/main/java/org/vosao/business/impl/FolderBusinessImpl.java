package org.vosao.business.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.FolderBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.servlet.FolderUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class FolderBusinessImpl extends AbstractBusinessImpl 
	implements FolderBusiness {

	private static final Log logger = LogFactory.getLog(FolderBusinessImpl.class);
	
	@Override
	public TreeItemDecorator<FolderEntity> getTree(
			final List<FolderEntity> folders) {
		Map<String, TreeItemDecorator<FolderEntity>> buf = 
			new HashMap<String, TreeItemDecorator<FolderEntity>>();
		for (FolderEntity page : folders) {
			buf.put(page.getId(), new TreeItemDecorator<FolderEntity>(page, 
					null));
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
					folder.setParent(parent);
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

	@Override
	public void createFolder(String aPath) throws UnsupportedEncodingException {
		logger.info("createFolder " + aPath);
		if (StringUtils.isEmpty(aPath)) {
			return;
		}
		String path = aPath; 
		if (!aPath.equals("/") && aPath.charAt(aPath.length() - 1) == '/') {
			path = aPath.substring(0, aPath.length() - 1);
		}
		logger.info("normalized " + path);
		TreeItemDecorator<FolderEntity> root = getTree();
		String[] chain = FolderUtil.getPathChain(path);
		String currentDir = "";
		FolderEntity parent = root.getEntity();
		for (String dir : chain) {
			currentDir += "/" + dir;
			TreeItemDecorator<FolderEntity> folder = findFolderByPath(root, 
					currentDir);
			if (folder == null) {
				FolderEntity newFolder = new FolderEntity(dir);
				newFolder.setParent(parent.getId());
				getDao().getFolderDao().save(newFolder);
				parent = newFolder;
			}
			else {
				parent = folder.getEntity();
			}
		}
		
	}

	@Override
	public String getFolderPath(FolderEntity folder) {
		TreeItemDecorator<FolderEntity> root = getTree();
		return getFolderPath(folder, root);
	}

	@Override
	public String getFolderPath(FolderEntity folder,
			TreeItemDecorator<FolderEntity> root) {
		
		Map<String, TreeItemDecorator<FolderEntity>> buf = 
			new HashMap<String, TreeItemDecorator<FolderEntity>>();
		addItemToMap(buf, root);
		TreeItemDecorator<FolderEntity> folderItem = buf.get(folder.getId());
		String result = "";
		while (folderItem.getParent() != null) {
			result = "/" + folderItem.getEntity().getName() + result;			
			folderItem = folderItem.getParent(); 
		}
		return result;
	}
	
	private void addItemToMap(Map<String, TreeItemDecorator<FolderEntity>> map,
			TreeItemDecorator<FolderEntity> item) {
		map.put(item.getEntity().getId(), item);
		for (TreeItemDecorator<FolderEntity> child : item.getChildren()) {
			addItemToMap(map, child);
		}
	}

	@Override
	public void recursiveRemove(List<String> folderIds) {
		for (String id : folderIds) {
			FolderEntity folder = getDao().getFolderDao().getById(id);
			if (id != null) {
				recursiveRemove(folder);
			}
		}
		
	}
	
	private void recursiveRemove(final FolderEntity folder) {
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			recursiveRemove(child);
		}
		getDao().getFileDao().removeByFolder(folder.getId());
		getDao().getFolderDao().remove(folder.getId());
	}
	
}
