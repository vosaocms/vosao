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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jabsorb.JSONRPCBridge;
import org.vosao.business.Business;
import org.vosao.business.PluginBusiness;
import org.vosao.business.impl.plugin.PluginLoader;
import org.vosao.business.plugin.PluginClassLoaderFactory;
import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.service.plugin.PluginServiceManager;
import org.vosao.velocity.plugin.VelocityPlugin;

public class PluginBusinessImpl extends AbstractBusinessImpl 
	implements PluginBusiness {

	private static final Log logger = LogFactory.getLog(PluginBusinessImpl.class);

	private Business business;
	private PluginLoader pluginLoader;
	private PluginClassLoaderFactory pluginClassLoaderFactory;
	private Map<String, VelocityPlugin> velocityPlugins;
	private Map<String, PluginServiceManager> frontServices;
	private Map<String, PluginServiceManager> backServices;
	private PluginResourceCache cache;
	
	public void init() {
		velocityPlugins = new HashMap<String, VelocityPlugin>();
		frontServices = new HashMap<String, PluginServiceManager>();
		backServices = new HashMap<String, PluginServiceManager>();
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
			velocityPlugins.put(plugin.getName(), 
					(VelocityPlugin)velocityPluginClass.newInstance());
		}
		return velocityPlugins.get(plugin.getName());
	}

	@Override
	public void resetPlugin(PluginEntity plugin) {
		velocityPlugins.remove(plugin.getName());
		frontServices.remove(plugin.getName());
		backServices.remove(plugin.getName());
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

	@Override
	public List<PluginPropertyVO> getProperties(PluginEntity plugin) {
		List<PluginPropertyVO> result = new ArrayList<PluginPropertyVO>();
		try {
			Document doc = DocumentHelper.parseText(plugin.getConfigStructure());
			for (Element e : (List<Element>)doc.getRootElement().elements()) {
				if (e.getName().equals("param")) {
					PluginPropertyVO p = new PluginPropertyVO();
					if (e.attributeValue("name") == null) {
						logger.error("There must be name attribute for param tag.");
						continue;
					}
					if (e.attributeValue("type") == null) {
						logger.error("There must be type attribute for param tag.");
						continue;
					}
					p.setName(e.attributeValue("name"));
					p.setType(e.attributeValue("type"));
					p.setDefaultValue(e.attributeValue("value"));
					if (e.attributeValue("title") == null) {
						p.setTitle(p.getName());
					}
					else {
						p.setTitle(e.attributeValue("title"));
					}
					result.add(p);
				}
			}
			return result;
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public Map<String, PluginPropertyVO> getPropertiesMap(PluginEntity plugin) {
		Map<String, PluginPropertyVO> map = 
				new HashMap<String, PluginPropertyVO>();
		List<PluginPropertyVO> list = getProperties(plugin);
		for (PluginPropertyVO p : list) {
			map.put(p.getName(), p);
		}
		return map;
	}

	@Override
	public PluginServiceManager getBackServices(PluginEntity plugin)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (!backServices.containsKey(plugin.getName())) {
			ClassLoader pluginClassLoader = getPluginClassLoaderFactory()
				.getClassLoader(plugin.getName());
			Class backServicesClass = pluginClassLoader
				.loadClass(plugin.getBackServiceClass());
			PluginServiceManager manager = (PluginServiceManager)
					backServicesClass.newInstance();
			manager.setDao(getDao());
			manager.setBusiness(getBusiness());
			backServices.put(plugin.getName(), manager);
		}
		return backServices.get(plugin.getName());
	}

	@Override
	public PluginServiceManager getFrontServices(PluginEntity plugin)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (!backServices.containsKey(plugin.getName())) {
			ClassLoader pluginClassLoader = getPluginClassLoaderFactory()
				.getClassLoader(plugin.getName());
			Class frontServicesClass = pluginClassLoader
				.loadClass(plugin.getBackServiceClass());
			PluginServiceManager manager = (PluginServiceManager)
					frontServicesClass.newInstance();
			manager.setDao(getDao());
			manager.setBusiness(getBusiness());
			frontServices.put(plugin.getName(), manager);
		}
		return frontServices.get(plugin.getName());
	}

}
