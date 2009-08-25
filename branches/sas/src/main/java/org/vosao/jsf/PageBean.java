package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.PageEntity;


public class PageBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private static Log log = LogFactory.getLog(PageBean.class);
	
	private List<PageEntity> list;
	private PageEntity current;
	private Map<Long, Boolean> selected;
	private Long id;
	
	public void init() {
		initList();
		current = new PageEntity();
		initSelected();
	}
	
	private void initList() {
		list = getDao().getPageDao().select();
	}

	private void initSelected() {
		selected = new HashMap<Long, Boolean>();
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
		List<Long> ids = new ArrayList<Long>();
		for (Long id : selected.keySet()) {
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

	public Map<Long, Boolean> getSelected() {
		return selected;
	}

	public void setSelected(Map<Long, Boolean> selected) {
		this.selected = selected;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
