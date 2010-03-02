package org.vosao.plugins.disqus;

import org.vosao.business.plugin.AbstractPluginEntryPoint;

public class DisqusEntryPoint extends AbstractPluginEntryPoint {

	private DisqusVelocityPlugin velocityPlugin;
	
	@Override
	public Object getPluginVelocityService() {
		if (velocityPlugin == null) {
			velocityPlugin = new DisqusVelocityPlugin(getBusiness());
		}
		return velocityPlugin;
	}

}
