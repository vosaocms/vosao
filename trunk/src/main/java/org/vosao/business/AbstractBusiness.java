package org.vosao.business;

import org.vosao.dao.Dao;

public interface AbstractBusiness {
	
	Dao getDao();
	
	void setDao(Dao dao);
	
}
