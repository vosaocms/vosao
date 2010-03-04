package org.vosao.business.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public abstract class AbstractPluginEntryPoint implements PluginEntryPoint {

	private Business business;
	private FrontService frontService;
	private BackService backService;
	private Map<String, HttpServlet> servlets;
	
	public AbstractPluginEntryPoint() {
		servlets = new HashMap<String, HttpServlet>();
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
	public BackService getBackService() {
		return backService;
	}

	@Override
	public Business getBusiness() {
		return business;
	}

	protected Dao getDao() {
		return getBusiness().getDao();
	}

	@Override
	public FrontService getFrontService() {
		return frontService;
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
	public void setFrontService(FrontService bean) {
		frontService = bean;
	}

	@Override
	public Map<String, HttpServlet> getServlets() {
		return servlets;
	}

	
	
}
