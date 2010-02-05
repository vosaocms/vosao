package org.vosao.business.impl.plugin;

import java.util.ArrayList;
import java.util.List;

import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.global.SystemService;

public class PluginResourceCacheImpl implements PluginResourceCache {

	private static final String PLUGIN_RESOURCE = "pluginResourceList"; 
	
	private SystemService systemService;

	private String getPluginResourcesListKey(String pluginName) {
		return pluginName + PLUGIN_RESOURCE;
	}
	
	private List<String> getPluginResourcesList(String pluginName) {
		String key = getPluginResourcesListKey(pluginName);
		if (!systemService.getCache().containsKey(key)) {
			systemService.getCache().put(key, new ArrayList<String>());
		}
		return (List<String>)systemService.getCache().get(key);
	}
	
	@Override
	public boolean contains(String pluginName, String key) {
		return getPluginResourcesList(pluginName).contains(key) 
			&& systemService.getCache().containsKey(key);
	}
	
	@Override
	public byte[] get(String pluginName, String key) {
		if (contains(pluginName, key)) {
			return (byte[])systemService.getCache().get(key);
		}
		return null;
	}
	
	@Override
	public void put(String pluginName, String key, byte[] data) {
		List<String> list = getPluginResourcesList(pluginName);
		list.add(key);
		systemService.getCache().put(getPluginResourcesListKey(pluginName), 
				list);
		systemService.getCache().put(key, data);
	}
	
	@Override
	public void reset(String pluginName) {
		systemService.getCache().remove(getPluginResourcesListKey(pluginName));
	}

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	
}
