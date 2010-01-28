/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.vosao.business.Business;
import org.vosao.business.PluginBusiness;
import org.vosao.business.impl.plugin.PluginClassLoader;
import org.vosao.business.impl.plugin.PluginLoader;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.velocity.plugin.VelocityPlugin;

public class PluginBusinessImpl extends AbstractBusinessImpl 
	implements PluginBusiness {

	private static final Log logger = LogFactory.getLog(PluginBusinessImpl.class);

	private Business business;
	private PluginLoader pluginLoader;
	private PluginClassLoader pluginClassLoader;
	private Map<String, VelocityPlugin> velocityPlugins;
	
	public void init() {
		velocityPlugins = new HashMap<String, VelocityPlugin>();
	}
	
	/**
	 * Plugin installation:
	 * - PluginEntity created
	 * - All resources are placed to /plugins/PLUGIN_NAME/
	 * - all classes are placed to PluginResourceEntity
	 */
	@Override
	public void install(String filename, byte[] data) throws IOException, 
			PluginException, DocumentException {
		getPluginLoader().install(filename, data);
	}
	

	@Override
	public synchronized VelocityPlugin getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException {
		if (velocityPlugins.containsKey(plugin.getName())) {
			return velocityPlugins.get(plugin.getName());
		}
		Class velocityPluginClass = pluginClassLoader
				.findClass(plugin.getVelocityPluginClass());
		return (VelocityPlugin) velocityPluginClass.newInstance();
	}

	@Override
	public void refreshPlugin(PluginEntity plugin) {
		velocityPlugins.remove(plugin.getName());
	}

	public PluginClassLoader getPluginClassLoader() {
		return pluginClassLoader;
	}

	public void setPluginClassLoader(PluginClassLoader pluginClassLoader) {
		this.pluginClassLoader = pluginClassLoader;
	}

	@Override
	public void uninstall(PluginEntity plugin) {
		getPluginLoader().uninstall(plugin);
	}
	
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public PluginLoader getPluginLoader() {
		if (pluginLoader == null) {
			pluginLoader = new PluginLoader(getDao(), getBusiness());
		}
		return pluginLoader;
	}

}
