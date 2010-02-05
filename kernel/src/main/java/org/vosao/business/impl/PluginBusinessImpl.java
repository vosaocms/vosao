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
import org.vosao.business.impl.plugin.PluginClassLoaderFactoryImpl;
import org.vosao.business.impl.plugin.PluginLoader;
import org.vosao.business.impl.plugin.PluginResourceCacheImpl;
import org.vosao.business.plugin.PluginClassLoaderFactory;
import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.velocity.plugin.VelocityPlugin;

public class PluginBusinessImpl extends AbstractBusinessImpl 
	implements PluginBusiness {

	private static final Log logger = LogFactory.getLog(PluginBusinessImpl.class);

	private Business business;
	private PluginLoader pluginLoader;
	private PluginClassLoaderFactory pluginClassLoaderFactory;
	private Map<String, VelocityPlugin> velocityPlugins;
	private PluginResourceCache cache;
	
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
	public VelocityPlugin getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException {
		if (!velocityPlugins.containsKey(plugin.getName())) {
			ClassLoader pluginClassLoader = getPluginClassLoaderFactory()
				.getClassLoader(plugin.getName());
			Class velocityPluginClass = pluginClassLoader
				.loadClass(plugin.getVelocityPluginClass());
			logger.info("Creating velocityPlugin");
			velocityPlugins.put(plugin.getName(), 
					(VelocityPlugin)velocityPluginClass.newInstance());
		}
		return velocityPlugins.get(plugin.getName());
	}

	@Override
	public void resetPlugin(PluginEntity plugin) {
		velocityPlugins.remove(plugin.getName());
		getPluginClassLoaderFactory().resetPlugin(plugin.getName());
		cache.reset(plugin.getName());
	}

	public PluginClassLoaderFactory getPluginClassLoaderFactory() {
		return pluginClassLoaderFactory;
	}

	public void setPluginClassLoaderFactory(PluginClassLoaderFactory bean) {
		this.pluginClassLoaderFactory = bean;
	}

	@Override
	public void uninstall(PluginEntity plugin) {
		getPluginLoader().uninstall(plugin);
		resetPlugin(plugin);
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

	public PluginResourceCache getCache() {
		return cache;
	}

	public void setCache(PluginResourceCache cache) {
		this.cache = cache;
	}

}
