package org.vosao.service.impl;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.AbstractService;

public abstract class AbstractServiceImpl implements AbstractService {

	private Dao dao;
	private Business business;
	
	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public void setDao(Dao aDao) {
		dao = aDao;		
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;		
	}

	@Override
	public Business getBusiness() {
		return business;
	}

	
}
