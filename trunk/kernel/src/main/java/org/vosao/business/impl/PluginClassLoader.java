package org.vosao.business.impl;

import org.vosao.business.PluginResourceBusiness;

public class PluginClassLoader extends ClassLoader {

	private PluginResourceBusiness pluginResourceBusiness;

	public PluginClassLoader(PluginResourceBusiness pluginResourceBusiness) {
		super();
		this.pluginResourceBusiness = pluginResourceBusiness;
	}
	
	@Override
	public Class findClass(String name) throws ClassNotFoundException {
		byte[] b = pluginResourceBusiness.findResource(name);
		if (b == null) {
			throw new ClassNotFoundException(name);
		}
		return defineClass(name, b, 0, b.length);
	}
	
}
