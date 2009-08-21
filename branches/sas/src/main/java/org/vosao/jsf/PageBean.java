package org.vosao.jsf;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;


public class PageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<PageEntity> list;
	private PageEntity current;
	private Dao dao;
	private Business business;
	private Map<Long, Boolean> selected;
	
	private PageBeanSession beanSession;
	
	public void init() {
		initList();
		beanSession = getBusiness().getUserPreferences().getPageBeanSession();
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
	
	public void addPage() {
		beanSession.setEdit(true);
	}
	
	public void cancelEdit() {
		beanSession.setEdit(false);
	}
	
	public void update() {
		getDao().getPageDao().save(current);
		list.add(current);
		beanSession.setEdit(false);
	}
	
	public void delete() {
		List<Long> ids = new ArrayList<Long>();
		for (Long id : selected.keySet()) {
			if (selected.get(id)) {
				ids.add(id);
			}
		}
		getDao().getPageDao().remove(ids);
		initList();
	}
	
	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public List<PageEntity> getList() {
		return list;
	}
	
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public boolean isEdit() {
		return beanSession.isEdit();
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
	
}
