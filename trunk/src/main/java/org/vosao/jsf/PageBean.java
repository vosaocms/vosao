package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.PageDecorator;
import org.vosao.entity.PageEntity;


public class PageBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static Log log = LogFactory.getLog(PageBean.class);
	
	private List<PageEntity> list;
	private PageEntity current;
	private Map<String, Boolean> selected;
	private String id;
	private PageDecorator root;
	private List<PageEntity> children;
	
	public void init() {
		initList();
		current = new PageEntity();
		initSelected();
		initDecorator();
	}
	
	private void initList() {
		list = getDao().getPageDao().select();
	}
	
	private void initDecorator() {
		root = getBusiness().getPageBusiness().getTree(list);
	}

	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (PageEntity page : list) {
			selected.put(page.getId(), false);
		}
	}
	
	private void initChildren() {
		if (current != null) {
			children = getDao().getPageDao().getByParent(current.getId());
		}
	}
	
	public String cancelEdit() {
		return "pretty:pages";
	}
	
	public String update() {
		//log.info("update record " + current.getTitle());
		if (current.getId() == null) {
			current.setParent(getBeanSession().getParent());
		}
		getDao().getPageDao().save(current);
		list.add(current);
		initDecorator();
		return "pretty:pages";
	}
	
	public String delete() {
		List<String> ids = new ArrayList<String>();
		for (String id : selected.keySet()) {
			if (selected.get(id)) {
				ids.add(id);
			}
		}
		getDao().getPageDao().remove(ids);
		initList();
		return "pretty:pages";
	}
	
	public void edit() {
		if (id != null) {
			current = getDao().getPageDao().getById(id);
			initChildren();
		}
	}
	
	public void list() {
	}
	
	public String addChild() {
		getBeanSession().setParent(current.getId());
		return "pretty:pageCreate";
	}
	
	
	public List<PageEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return current.getId() != null;
	}

	public PageEntity getCurrent() {
		return current;
	}

	public void setCurrent(PageEntity current) {
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

	public PageBeanSession getBeanSession() {
		String name = PageBeanSession.class.getName();
		if (JSFUtil.getSessionObject(name) == null) {
			JSFUtil.setSessionObject(name, new PageBeanSession());
		}
		return (PageBeanSession)JSFUtil.getSessionObject(name);
	}

	public String getTree() {
		if (root != null) {
			return renderPageTree(root).toString();
		}
		else {
			return "empty tree";
		}
	}
	
	private static StringBuffer renderPageTree(final PageDecorator page) {
		StringBuffer result = new StringBuffer();
		result.append("<li><a href=\"page/edit/")
			.append(page.getPage().getId())
			.append("\">")
			.append(page.getPage().getTitle())
			.append("</a>");
		if (page.getChildren().size() > 0) {
			result.append("<ul>");
		}
		for (PageDecorator child : page.getChildren()) {
			result.append(renderPageTree(child));
		}
		if (page.getChildren().size() > 0) {
			result.append("</ul>");
		}
		result.append("</li>");
		return result;
	}

	public List<PageEntity> getChildren() {
		return children;
	}

	public void setChildren(List<PageEntity> children) {
		this.children = children;
	}
	
	public boolean isShowChildren() {
		return children != null;
	}
	
}
