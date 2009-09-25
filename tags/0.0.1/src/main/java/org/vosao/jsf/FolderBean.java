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
package org.vosao.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;


public class FolderBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 3L;
	private static Log log = LogFactory.getLog(FolderBean.class);
	
	private List<FolderEntity> list;
	private FolderEntity current;
	private Map<String, Boolean> selected;
	private String id;
	private TreeItemDecorator<FolderEntity> root;
	private List<FolderEntity> children;
	private List<SelectItem> templates;
	private Map<String, Boolean> fileSelected;

	public void init() {
		initList();
		initCurrent();
		initSelected();
		initDecorator();
	}
	
	private void initList() {
		list = getDao().getFolderDao().select();
	}

	private void initCurrent() {
		if (getCurrentId() != null) {
			current = getDao().getFolderDao().getById(getCurrentId());
		}
		else {
			current = new FolderEntity();
		}
	}
	
	private void initDecorator() {
		root = getBusiness().getFolderBusiness().getTree(list);
	}

	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (FolderEntity folder : list) {
			selected.put(folder.getId(), false);
		}
	}
	
	private void initChildren() {
		if (current != null) {
			children = getDao().getFolderDao().getByParent(current.getId());
		}
	}
	
	public String cancelEdit() {
		return "pretty:folders";
	}
	
	public String update() {
		if (current.getId() == null) {
			current.setParent(getParent());
		}
		List<String> errors = getBusiness().getFolderBusiness()
				.validateBeforeUpdate(current);
		if (errors.isEmpty()) {
			getDao().getFolderDao().save(current);
			list.add(current);
			initDecorator();
			return "pretty:folders";
		}
		else {
			JSFUtil.addErrorMessages(errors);
			return null;
		}
	}
	
	public String delete() {
		List<String> ids = new ArrayList<String>();
		for (String id : selected.keySet()) {
			if (selected.get(id)) {
				ids.add(id);
			}
		}
		getBusiness().getFolderBusiness().recursiveRemove(ids);
		initList();
		return "pretty:folders";
	}
	
	public void edit() {
		if (id != null) {
			setCurrentId(id);
			initCurrent();
			initChildren();
		}
	}
	
	public void list() {
	}
	
	public String addChild() {
		setParent(current.getId());
		setCurrentId(null);
		return "pretty:folderCreate";
	}
	
	public String addChildParam() {
		if (id != null) {
			current = getDao().getFolderDao().getById(id);
		}
		setParent(current.getId());
		setCurrentId(null);
		return "pretty:folderCreate";
	}
	
	public List<FolderEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}

	public FolderEntity getCurrent() {
		return current;
	}

	public void setCurrent(FolderEntity current) {
		this.current = current;
	}

	public Map<String, Boolean> getSelected() {
		return selected;
	}

	public void setSelected(Map<String, Boolean> selected) {
		this.selected = selected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTree() {
		if (root != null) {
			return renderFolderTree(root).toString();
		}
		else {
			return "empty tree";
		}
	}
	
	private static StringBuffer renderFolderTree(
			final TreeItemDecorator<FolderEntity> folder) {
		StringBuffer result = new StringBuffer();
		String editFolderLink = "<a href=\"folder/edit/" 
			+ folder.getEntity().getId() + "\">" + folder.getEntity().getTitle()
			+ "</a>";
		String addChildParamLink = "&nbsp;<a title=\"Add child\" href=\"/cms/folder/createChild/"
			+ folder.getEntity().getId() + "\">+</a>";
		
		result.append("<li>").append(editFolderLink).append(addChildParamLink);
		
		if (folder.getChildren().size() > 0) {
			result.append("<ul>");
		}
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			result.append(renderFolderTree(child));
		}
		if (folder.getChildren().size() > 0) {
			result.append("</ul>");
		}
		result.append("</li>");
		return result;
	}

	public List<FolderEntity> getChildren() {
		return children;
	}

	public void setChildren(List<FolderEntity> children) {
		this.children = children;
	}
	
	public boolean isShowChildren() {
		return children != null;
	}

	public String getParent() {
		String name = this.getClass().getName() + "parent";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setParent(String parent) {
		String name = this.getClass().getName() + "parent";
		JSFUtil.setSessionObject(name, parent);
	}

	public String getCurrentId() {
		String name = this.getClass().getName() + "currentId";
		return (String)JSFUtil.getSessionObject(name);
	}

	public void setCurrentId(String data) {
		String name = this.getClass().getName() + "currentId";
		JSFUtil.setSessionObject(name, data);
	}

	public List<SelectItem> getTemplates() {
		return templates;
	}

	public void setTemplates(List<SelectItem> templates) {
		this.templates = templates;
	}
	
	public boolean isShowFiles() {
		return current.getId() != null; 
	}

	public Map<String, Boolean> getFileSelected() {
		return fileSelected;
	}

	public void setFileSelected(Map<String, Boolean> fileSelected) {
		this.fileSelected = fileSelected;
	}
	
}
