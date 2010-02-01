package org.vosao.velocity.plugin;

import org.vosao.business.Business;
import org.vosao.dao.Dao;

public class AbstractVelocityPlugin implements VelocityPlugin {

	private Dao dao;
	private Business business;
	
	@Override
	public Business getBusiness() {
		return business;
	}

	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;		
	}

	@Override
	public void setDao(Dao bean) {
		dao = bean;
	}

}
