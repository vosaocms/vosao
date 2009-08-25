package org.vosao.jsf;

import org.vosao.business.Business;
import org.vosao.dao.Dao;

public abstract class AbstractJSFBean {

	private Dao dao;
	private Business business;

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public Business getBusiness() {
		return business;
	}
	
	public void setBusiness(Business business) {
		this.business = business;
	}
	
}
