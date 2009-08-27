package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FolderEntity;


public class FolderBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static Log log = LogFactory.getLog(FolderBean.class);
	
	private List<FolderEntity> list;
	private FolderEntity current;
	private Map<String, Boolean> selected;
	private String id;
	private TreeItemDecorator<FolderEntity> root;
	private List<FolderEntity> children;
	private List<SelectItem> templates;

	public void init() {
		initList();
		current = new FolderEntity();
		initSelected();
		initDecorator();
	}
	
	private void initList() {
		list = getDao().getFolderDao().select();
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
		//log.info("update record " + current.getTitle());
		if (current.getId() == null) {
			current.setParent(getParent());
		}
		getDao().getFolderDao().save(current);
		list.add(current);
		initDecorator();
		return "pretty:folders";
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
			current = getDao().getFolderDao().getById(id);
			initChildren();
		}
	}
	
	public void list() {
	}
	
	public String addChild() {
		setParent(current.getId());
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
		result.append("<li><a href=\"folder/edit/")
			.append(folder.getEntity().getId())
			.append("\">")
			.append(folder.getEntity().getName())
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

	public List<SelectItem> getTemplates() {
		return templates;
	}

	public void setTemplates(List<SelectItem> templates) {
		this.templates = templates;
	}
	
}
