package org.vosao.business.plugin;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public interface PluginEntryPoint {

	Dao getDao();
	void setDao(Dao bean);
	
	Business getBusiness();
	void setBusiness(Business bean);

	BackService getBackService();
	void setBackService(BackService bean);
	
	FrontService getFrontService();
	void setFrontService(FrontService bean);

	PluginServiceManager getPluginBackService();

	PluginServiceManager getPluginFrontService();
	
	Object getPluginVelocityService();
}
