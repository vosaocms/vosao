package org.vosao.business.plugin;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public class AbstractPluginEntryPoint implements PluginEntryPoint {

	private Dao dao;
	private Business business;
	private FrontService frontService;
	private BackService backService;
	
	@Override
	public BackService getBackService() {
		return backService;
	}

	@Override
	public Business getBusiness() {
		return business;
	}

	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public FrontService getFrontService() {
		return frontService;
	}

	@Override
	public PluginServiceManager getPluginBackService() {
		return null;
	}

	@Override
	public PluginServiceManager getPluginFrontService() {
		return null;
	}

	@Override
	public Object getPluginVelocityService() {
		return null;
	}

	@Override
	public void setBackService(BackService bean) {
		backService = bean;
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;
	}

	@Override
	public void setDao(Dao bean) {
		dao = bean;
	}

	@Override
	public void setFrontService(FrontService bean) {
		frontService = bean;
	}

}
