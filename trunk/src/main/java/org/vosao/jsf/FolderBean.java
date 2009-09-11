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
		initFileSelected();
	}
	
	private void initList() {
		list = getDao().getFolderDao().select();
	}

	private void initCurrent() {
		current = new FolderEntity();
		if (getCurrentId() != null) {
			current = getDao().getFolderDao().getById(getCurrentId());
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
	
	private void initFileSelected() {
		fileSelected = new HashMap<String, Boolean>();
		if (current != null) {
			for (FileEntity file : current.getFiles()) {
				fileSelected.put(file.getId(), false);
			}
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
		getDao().getFolderDao().remove(ids);
		initList();
		return "pretty:folders";
	}
	
	public void edit() {
		if (id != null) {
			setCurrentId(id);
			current = getDao().getFolderDao().getById(id);
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
	
	public void deleteFiles() {
		List<String> ids = new ArrayList<String>();
		for (String id : fileSelected.keySet()) {
			if (fileSelected.get(id)) {
				ids.add(id);
			}
		}
		getDao().getFileDao().remove(ids);
		initCurrent();
		initChildren();
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
		result.append("<li><a href=\"folder/edit/")
			.append(folder.getEntity().getId())
			.append("\">")
			.append(folder.getEntity().getTitle())
			.append("</a>");
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
