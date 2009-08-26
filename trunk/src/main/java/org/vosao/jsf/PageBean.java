package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;


public class PageBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static Log log = LogFactory.getLog(PageBean.class);
	
	private List<PageEntity> list;
	private PageEntity current;
	private Map<String, Boolean> selected;
	private String id;
	
	public void init() {
		initList();
		current = new PageEntity();
		initSelected();
	}
	
	private void initList() {
		list = getDao().getPageDao().select();
	}

	private void initSelected() {
		selected = new HashMap<String, Boolean>();
		for (PageEntity page : list) {
			selected.put(page.getId(), false);
		}
	}
	
	public String addPage() {
		getBeanSession().setEdit(true);
		return "pretty:pageCreate";
	}
	
	public String cancelEdit() {
		getBeanSession().setEdit(false);
		return "pretty:pages";
	}
	
	public String update() {
		log.info("update record " + current.getTitle());
		getDao().getPageDao().save(current);
		list.add(current);
		getBeanSession().setEdit(false);
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
			getBeanSession().setNewEntity(false);
			getBeanSession().setEdit(true);
		}
	}
	
	public void list() {
		getBeanSession().setEdit(false);
	}
	
	public List<PageEntity> getList() {
		return list;
	}
	
	public boolean isEdit() {
		return getBeanSession().isEdit();
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
	
}
