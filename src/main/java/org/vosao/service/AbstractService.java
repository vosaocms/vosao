package org.vosao.service;

import org.vosao.business.Business;
import org.vosao.dao.Dao;

public interface AbstractService {
	
	Dao getDao();
	void setDao(Dao dao);

	Business getBusiness();
	void setBusiness(Business bean);
	
}
