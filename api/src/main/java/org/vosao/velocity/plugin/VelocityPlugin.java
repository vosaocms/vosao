package org.vosao.velocity.plugin;

import org.vosao.business.Business;
import org.vosao.dao.Dao;

public interface VelocityPlugin {

	Dao getDao();
	void setDao(Dao bean);
	
	Business getBusiness();
	void setBusiness(Business bean);

}
