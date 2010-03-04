package org.vosao.plugins.register;

import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.service.plugin.PluginServiceManager;

public class RegisterEntryPoint extends AbstractPluginEntryPoint {

	private RegisterDao registerDao;
	private RegisterBackServiceManager registerBackSerivice;
	
	@Override
	public void init() {
		getServlets().put("test", new ConfirmServlet());
		getJobs().add(new CleanupConfirmationsJob());
	}
	
	@Override
	public PluginServiceManager getPluginBackService() {
		if (registerBackSerivice == null) {
			registerBackSerivice = new RegisterBackServiceManager(getBusiness(),
					getRegisterDao());
		}
		return registerBackSerivice;
	}

	private RegisterDao getRegisterDao() {
		if (registerDao == null) {
			registerDao = new RegisterDao(getDao());
		}
		return registerDao;
	}
	
	
	
	
}
