package org.vosao.business.plugin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.vosao.business.Business;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public interface PluginEntryPoint {

	/**
	 * Plugin initialization. Called after setting dao, business and services.
	 */
	public void init();
	
	Business getBusiness();
	void setBusiness(Business bean);

	BackService getBackService();
	void setBackService(BackService bean);
	
	FrontService getFrontService();
	void setFrontService(FrontService bean);

	PluginServiceManager getPluginBackService();

	PluginServiceManager getPluginFrontService();
	
	Object getPluginVelocityService();
	
	/**
	 * Get plugin servlets map. 
	 * All plugin servlets mapped to url /plugin/PLUGIN_NAME/PATH
	 * Map key is PATH element.
	 * @return
	 */
	Map<String, HttpServlet> getServlets();
	
	List<PluginCronJob> getJobs();
}
