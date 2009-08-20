package org.vosao.jsf;
import java.io.Serializable;
import java.util.List;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;


public class TestBean implements Serializable {

	private String text;
	private List<PageEntity> pages;
	
	private Dao dao;
	private Business business;
	
	public void init() {
		text = "This is bean message";
		pages = getDao().getPageDao().select();
	}

	public void changeText() {
		getBusiness().getUserPreferences().incrementCounter();
		text = "message changed by JSF action " 
			+ getBusiness().getUserPreferences().getCounter();
	}
	
	public void addPage() {
		int c = getBusiness().getUserPreferences().getCounter();
		PageEntity page = new PageEntity("Page title " + c,"page content",
				"/page/" + c, null);
		getDao().getPageDao().save(page);
		pages.add(page);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCounter() {
		return getBusiness().getUserPreferences().getCounter();
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

	public List<PageEntity> getPages() {
		return pages;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}
	
}
