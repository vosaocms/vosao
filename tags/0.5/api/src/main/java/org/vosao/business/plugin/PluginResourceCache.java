package org.vosao.business.plugin;


public interface PluginResourceCache {
	
	boolean contains(String pluginName, String key);
	
	byte[] get(String pluginName, String key);
	
	void put(String pluginName, String key, byte[] data);
	
	void reset(String pluginName);

}
