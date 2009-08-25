package org.vosao.business;

import org.vosao.dao.Dao;

public interface SetupBean {
	
	void setup();

	Dao getDao();
	void setDao(Dao dao);

	Business getBusiness();
	void setBusiness(Business business);
	
	
}
