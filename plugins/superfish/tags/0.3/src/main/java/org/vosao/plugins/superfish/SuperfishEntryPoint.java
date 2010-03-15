package org.vosao.plugins.superfish;

import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.service.plugin.PluginServiceManager;

public class SuperfishEntryPoint extends AbstractPluginEntryPoint {

	SuperfishBackServiceManager backServiceManager;
	SuperfishVelocityPlugin velocityPlugin;
	
	@Override
	public PluginServiceManager getPluginBackService() {
		if (backServiceManager == null) {
			backServiceManager = new SuperfishBackServiceManager(
					getBusiness()); 
		}
		return backServiceManager;
	}

	@Override
	public Object getPluginVelocityService() {
		if (velocityPlugin == null) {
			velocityPlugin = new SuperfishVelocityPlugin(getBusiness());
		}
		return velocityPlugin;
	}

	
}
