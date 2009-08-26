package org.vosao.business.impl;

import org.vosao.business.AbstractBusiness;
import org.vosao.dao.Dao;

public abstract class AbstractBusinessImpl implements AbstractBusiness {

	private Dao dao;
	
	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public void setDao(Dao aDao) {
		dao = aDao;		
	}

}
