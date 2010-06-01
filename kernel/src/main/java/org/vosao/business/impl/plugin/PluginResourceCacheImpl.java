package org.vosao.business.impl.plugin;

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.common.VosaoContext;
import org.vosao.global.SystemService;

public class PluginResourceCacheImpl implements PluginResourceCache {

	private static final String PLUGIN_RESOURCE = "pluginResourceList"; 

	private SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}
	
	private String getPluginResourcesListKey(String pluginName) {
		return pluginName + PLUGIN_RESOURCE;
	}
	
	private List<String> getPluginResourcesList(String pluginName) {
		String key = getPluginResourcesListKey(pluginName);
		if (!getSystemService().getCache().containsKey(key)) {
			getSystemService().getCache().put(key, new ArrayList<String>());
		}
		return (List<String>)getSystemService().getCache().get(key);
	}
	
	@Override
	public boolean contains(String pluginName, String key) {
		return getPluginResourcesList(pluginName).contains(key) 
			&& getSystemService().getCache().containsKey(key);
	}
	
	@Override
	public byte[] get(String pluginName, String key) {
		if (contains(pluginName, key)) {
			return (byte[])getSystemService().getCache().get(key);
		}
		return null;
	}
	
	@Override
	public void put(String pluginName, String key, byte[] data) {
		List<String> list = getPluginResourcesList(pluginName);
		list.add(key);
		getSystemService().getCache().put(getPluginResourcesListKey(pluginName), 
				list);
		getSystemService().getCache().put(key, data);
	}
	
	@Override
	public void reset(String pluginName) {
		getSystemService().getCache().remove(getPluginResourcesListKey(pluginName));
	}

}
