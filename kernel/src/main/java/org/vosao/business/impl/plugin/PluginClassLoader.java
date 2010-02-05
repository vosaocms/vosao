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

package org.vosao.business.impl.plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginResourceEntity;
import org.vosao.global.SystemService;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

public class PluginClassLoader extends ClassLoader {

	private static final Log logger = LogFactory.getLog(PluginClassLoader.class);
	
	private SystemService systemService;
	private Dao dao;
	private PluginResourceCache cache;
	private String pluginName;
	
	public PluginClassLoader() {
		super(PluginClassLoader.class.getClassLoader());
	}
	
	public PluginClassLoader(SystemService systemService, Dao dao,
			PluginResourceCache cache, String pluginName) {
		super(PluginClassLoader.class.getClassLoader());
		this.systemService = systemService;
		this.dao = dao;
		this.cache = cache;
		this.pluginName = pluginName;
	}

	@Override
	public Class findClass(String name) throws ClassNotFoundException {
		Class cls = findLoadedClass(name);
		if (cls != null) {
			return cls;
		}
		try {
			byte[] b = findPluginResource(name);
			if (b == null) {
				throw new ClassNotFoundException(name);
			}
			return defineClass(name, b, 0, b.length);
		}
		catch (SecurityException e) {
			return super.loadClass(name);
		}
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		byte[] b = findPluginResource(name);
		if (b == null) {
			throw new ResourceNotFoundException(name);
		}
		return new ByteArrayInputStream(b);
	}
	
	private byte[] findPluginResource(String name) {
		if (!getCache().contains(pluginName, name)) {
			byte[] data = loadPluginResource(name);
			if (data != null) {
				getCache().put(pluginName, name, data);
			}
			else {
				return null;
			}
		}
		return getCache().get(pluginName, name);
	}

	private byte[] loadPluginResourceNative(String name) {
		Query query = new Query("PluginResourceEntity");
		query.addFilter("url", Query.FilterOperator.EQUAL, name);
		Entity e = getSystemService().getDatastore().prepare(query)
			.asSingleEntity();
		if (e != null) {
			return ((Blob)e.getProperty("data")).getBytes();
		}
		return null;
	}
	
	private byte[] loadPluginResource(String name) {
		PluginResourceEntity resource = getDao().getPluginResourceDao()
				.getByUrl(name);
		if (resource != null) {
			return resource.getContent(); 
		}
		return null;
	}
	
	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public PluginResourceCache getCache() {
		return cache;
	}

	public void setCache(PluginResourceCache bean) {
		cache = bean;
	}
	
}
