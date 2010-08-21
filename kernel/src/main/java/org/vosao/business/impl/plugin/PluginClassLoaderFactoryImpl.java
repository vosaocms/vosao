/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl.plugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.plugin.PluginClassLoaderFactory;
import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.global.SystemService;

public class PluginClassLoaderFactoryImpl implements PluginClassLoaderFactory {

	private static final Log logger = LogFactory.getLog(PluginClassLoader.class);
	
	private Map<String, ClassLoader> classLoaders;
	private PluginResourceCache cache;
	
	public ClassLoader getClassLoader(String pluginName) {
		if (!getClassLoaders().containsKey(pluginName)) {
			logger.info("creating class loader " + pluginName);
			PluginClassLoader classLoader = new PluginClassLoader(
					getSystemService(), getDao(), getCache(), pluginName);
			getClassLoaders().put(pluginName, classLoader);			
		}
		return getClassLoaders().get(pluginName);
	}

	public void resetPlugin(String pluginName) {
		getClassLoaders().remove(pluginName);
	}
	
	private Map<String, ClassLoader> getClassLoaders() {
		if (classLoaders == null) {
			classLoaders = new HashMap<String, ClassLoader>();
		}
		return classLoaders;
	}
	
	public SystemService getSystemService() {
		return VosaoContext.getInstance().getBusiness().getSystemService();
	}

	public Dao getDao() {
		return VosaoContext.getInstance().getBusiness().getDao();
	}

	public PluginResourceCache getCache() {
		if (cache == null) {
			cache = new PluginResourceCacheImpl();
		}
		return cache;
	}

	public void setCache(PluginResourceCache cache) {
		this.cache = cache;
	}
	
}
