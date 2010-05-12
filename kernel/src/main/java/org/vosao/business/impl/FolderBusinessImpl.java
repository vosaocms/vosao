/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.FolderBusiness;
import org.vosao.business.FolderPermissionBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.Messages;
import org.vosao.common.VosaoContext;
import org.vosao.entity.FolderEntity;
import org.vosao.utils.FolderUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

/**
 * @author Alexander Oleynik
 */
public class FolderBusinessImpl extends AbstractBusinessImpl 
	implements FolderBusiness {

	private FolderPermissionBusiness folderPermissionBusiness;
	
	@Override
	public TreeItemDecorator<FolderEntity> getTree(
			final List<FolderEntity> folders) {
		Map<Long, TreeItemDecorator<FolderEntity>> buf = 
			new HashMap<Long, TreeItemDecorator<FolderEntity>>();
		for (FolderEntity folder : folders) {
			buf.put(folder.getId(), new TreeItemDecorator<FolderEntity>(folder, 
					null));
		}
		TreeItemDecorator<FolderEntity> root = null;
		for (Long id : buf.keySet()) {
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
		return securityFilter(root);
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
		if (haveReadAccess(current.getEntity())) {
			return current;
		}
		return null;
	}
	
	private TreeItemDecorator<FolderEntity> findByChildName(
			final TreeItemDecorator<FolderEntity> folder, final String name) {
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			if (child.getEntity().getName().equals(name)) {
				if (haveReadAccess(child.getEntity())) {
					return child;
				}
			}
		}
		return null;
	}

	public List<String> validateBeforeUpdate(final FolderEntity folder) {
		List<String> errors = new ArrayList<String>();
		FolderEntity securityFolder = folder;
		if (folder.getId() == null) {
			FolderEntity myFolder = getDao().getFolderDao().getByParentName(
					folder.getParent(), folder.getName());
			if (myFolder != null) {
				errors.add(Messages.get("folder_already_exists"));
			}
			securityFolder = getDao().getFolderDao().getById(folder.getParent());
		}
		if (!haveWriteAccess(securityFolder)) {
			errors.add(Messages.get("access_denied"));
		}
		if (StringUtil.isEmpty(folder.getTitle())) {
			errors.add(Messages.get("title_is_empty"));
		}
		if (StringUtil.isEmpty(folder.getName())) {
			errors.add(Messages.get("name_is_empty"));
		}
		return errors;
	}

	@Override
	public FolderEntity createFolder(String aPath) {
		if (StringUtils.isEmpty(aPath)) {
			return null;
		}
		String path = aPath; 
		if (!aPath.equals("/") && aPath.charAt(aPath.length() - 1) == '/') {
			path = aPath.substring(0, aPath.length() - 1);
		}
		TreeItemDecorator<FolderEntity> root = getTree();
		if (aPath.equals("/")) {
			return root.getEntity();
		}
		String[] chain;
		chain = FolderUtil.getPathChain(path);
		String currentDir = "";
		FolderEntity parent = root.getEntity();
		for (String dir : chain) {
			currentDir += "/" + dir;
			TreeItemDecorator<FolderEntity> folder = findFolderByPath(root, 
					currentDir);
			if (folder == null) {
				if (haveWriteAccess(parent)) {
					FolderEntity newFolder = new FolderEntity(dir);
					newFolder.setParent(parent.getId());
					getDao().getFolderDao().save(newFolder);
					parent = newFolder;
				}
				else return null;
			}
			else {
				parent = folder.getEntity();
			}
		}
		return parent;
	}

	@Override
	public String getFolderPath(FolderEntity folder) {
		TreeItemDecorator<FolderEntity> root = getTree();
		return getFolderPath(folder, root);
	}

	@Override
	public String getFolderPath(FolderEntity folder,
			TreeItemDecorator<FolderEntity> root) {
		
		Map<Long, TreeItemDecorator<FolderEntity>> buf = 
			new HashMap<Long, TreeItemDecorator<FolderEntity>>();
		addItemToMap(buf, root);
		TreeItemDecorator<FolderEntity> folderItem = buf.get(folder.getId());
		String result = "";
		while (folderItem.getParent() != null) {
			result = "/" + folderItem.getEntity().getName() + result;			
			folderItem = folderItem.getParent(); 
		}
		return result;
	}
	
	private void addItemToMap(Map<Long, TreeItemDecorator<FolderEntity>> map,
			TreeItemDecorator<FolderEntity> item) {
		map.put(item.getEntity().getId(), item);
		for (TreeItemDecorator<FolderEntity> child : item.getChildren()) {
			addItemToMap(map, child);
		}
	}

	@Override
	public void recursiveRemove(List<Long> folderIds) {
		for (Long id : folderIds) {
			FolderEntity folder = getDao().getFolderDao().getById(id);
			if (id != null) {
				recursiveRemove(folder);
			}
		}
		
	}
	
	private void recursiveRemove(final FolderEntity folder) {
		if (!haveWriteAccess(folder)) {
			return;
		}
		List<FolderEntity> children = getDao().getFolderDao().getByParent(
				folder.getId());
		for (FolderEntity child : children) {
			recursiveRemove(child);
		}
		getDao().getFileDao().removeByFolder(folder.getId());
		getDao().getFolderDao().remove(folder.getId());
	}
	
	private TreeItemDecorator<FolderEntity> securityFilter(
			TreeItemDecorator<FolderEntity> root) {
		if (!haveReadAccess(root.getEntity())) {
			return null;
		}
		List<TreeItemDecorator<FolderEntity>> newChildren = 
				new ArrayList<TreeItemDecorator<FolderEntity>>();
		for (TreeItemDecorator<FolderEntity> child : root.getChildren()) {
			TreeItemDecorator<FolderEntity> filteredChild = securityFilter(
					child);
			if (filteredChild != null) {
				newChildren.add(filteredChild);
			}
		}
		root.setChildren(newChildren);
		return root;
	}

	@Override
	public FolderPermissionBusiness getFolderPermissionBusiness() {
		return folderPermissionBusiness;
	}

	@Override
	public void setFolderPermissionBusiness(FolderPermissionBusiness bean) {
		folderPermissionBusiness = bean;		
	}

	private boolean haveReadAccess(FolderEntity folder) {
		return !getFolderPermissionBusiness().getPermission(folder, 
				VosaoContext.getInstance().getUser()).isDenied();
	}

	private boolean haveWriteAccess(FolderEntity folder) {
		return getFolderPermissionBusiness().getPermission(folder, 
				VosaoContext.getInstance().getUser()).isChangeGranted();
	}

	@Override
	public FolderEntity getById(Long id) {
		FolderEntity folder = getDao().getFolderDao().getById(id);
		if (folder != null && haveReadAccess(folder)) {
			return folder;
		}
		return null;
	}

	@Override
	public List<FolderEntity> getByParent(Long id) {
		return securityReadFilter(getDao().getFolderDao().getByParent(id));
	}

	private List<FolderEntity> securityReadFilter(List<FolderEntity> list) {
		List<FolderEntity> result = new ArrayList<FolderEntity>();
		for (FolderEntity folder : list) {
			if (haveReadAccess(folder)) {
				result.add(folder);
			}
		}
		return result;
	}

	@Override
	public void recursiveRemove(String path) {
		TreeItemDecorator<FolderEntity> root = getTree();
		TreeItemDecorator<FolderEntity> folder = findFolderByPath(root, path);
		if (folder != null) {
			recursiveRemove(folder.getEntity());
		}
	}
	
}
