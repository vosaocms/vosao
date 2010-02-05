package org.vosao.business.plugin;

public interface PluginClassLoaderFactory {

	ClassLoader getClassLoader(String pluginName);

	void resetPlugin(String pluginName);
	
}
