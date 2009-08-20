package org.vosao.jsf;
import java.io.Serializable;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;


public class PageBean implements Serializable {

	private List<PageEntity> list;
	private PageEntity current;
	private Dao dao;
	private Business business;
	
	private PageBeanSession beanSession;
	
	public void init() {
		list = getDao().getPageDao().select();
		beanSession = getBusiness().getUserPreferences().getPageBeanSession();
		current = new PageEntity();
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
	
}
