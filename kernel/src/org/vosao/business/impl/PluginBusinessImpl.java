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

package org.vosao.business.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.PluginBusiness;
import org.vosao.business.impl.plugin.PluginClassLoaderFactoryImpl;
import org.vosao.business.impl.plugin.PluginLoader;
import org.vosao.business.plugin.PluginClassLoaderFactory;
import org.vosao.business.plugin.PluginCronJob;
import org.vosao.business.plugin.PluginEntryPoint;
import org.vosao.business.plugin.PluginResourceCache;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.common.PluginException;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PluginEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PluginBusinessImpl extends AbstractBusinessImpl 
	implements PluginBusiness {

	private PluginLoader pluginLoader;
	private PluginClassLoaderFactory pluginClassLoaderFactory;
	private Map<String, PluginEntryPoint> plugins;
	private Map<String, PluginEntity> pluginTimestamps;
	
	public PluginBusinessImpl() {
		plugins = new HashMap<String, PluginEntryPoint>();
		pluginTimestamps = new HashMap<String, PluginEntity>();
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
		Messages.resetCache();
	}
	
	@Override
	public Object getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException {
		PluginEntryPoint entryPoint = getEntryPoint(plugin);
		return entryPoint == null ? null : entryPoint.getPluginVelocityService();
	}

	@Override
	public void resetPlugin(PluginEntity plugin) {
		plugins.remove(plugin.getName());
		pluginTimestamps.remove(plugin.getName());
		getPluginClassLoaderFactory().resetPlugin(plugin.getName());
		getCache().reset(plugin.getName());
	}

	public PluginClassLoaderFactory getPluginClassLoaderFactory() {
		if (pluginClassLoaderFactory == null) {
			pluginClassLoaderFactory = new PluginClassLoaderFactoryImpl();
		}
		return pluginClassLoaderFactory;
	}

	public void setPluginClassLoaderFactory(PluginClassLoaderFactory bean) {
		this.pluginClassLoaderFactory = bean;
	}

	@Override
	public void uninstall(PluginEntity plugin) {
		getBusiness().getRewriteUrlBusiness().removeRules(
				getEntryPoint(plugin).getRewriteRules());
		
		getEntryPoint(plugin).uninstall();
		getPluginLoader().uninstall(plugin);
		resetPlugin(plugin);
	}
	
	public PluginLoader getPluginLoader() {
		if (pluginLoader == null) {
			pluginLoader = new PluginLoader(getDao(), getBusiness());
		}
		return pluginLoader;
	}

	public PluginResourceCache getCache() {
		return pluginClassLoaderFactory.getCache();
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
		PluginEntryPoint entryPoint = getEntryPoint(plugin);
		return entryPoint== null ? null : entryPoint.getPluginBackService();
	}

	@Override
	public PluginServiceManager getFrontServices(PluginEntity plugin)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		PluginEntryPoint entryPoint = getEntryPoint(plugin);
		return entryPoint == null ? null : entryPoint.getPluginFrontService();
	}

	private boolean isNeedRefresh(PluginEntity plugin) {
		if (pluginTimestamps.containsKey(plugin.getName())) {
			return !pluginTimestamps.get(plugin.getName()).getModDate()
					.equals(plugin.getModDate());
		}
		return true;
	}
	
	@Override
	public PluginEntryPoint getEntryPoint(PluginEntity plugin) {
		if (!plugins.containsKey(plugin.getName()) 
			|| isNeedRefresh(plugin)) {
			try {
				resetPlugin(plugin);
				ClassLoader pluginClassLoader = getPluginClassLoaderFactory()
					.getClassLoader(plugin.getName());
				Class entryPointClass = pluginClassLoader
					.loadClass(plugin.getEntryPointClass());
				PluginEntryPoint entryPoint = (PluginEntryPoint)entryPointClass
					.newInstance();
				entryPoint.setBusiness(getBusiness());
				entryPoint.setFrontService(getFrontService());
				entryPoint.setBackService(getBackService());
				entryPoint.init();
				plugins.put(plugin.getName(), entryPoint);
				pluginTimestamps.put(plugin.getName(), plugin);
				getBusiness().getRewriteUrlBusiness().addRules(
						entryPoint.getRewriteRules());
			}
			catch (ClassNotFoundException e) {
				logger.error("Class not found " + e.getMessage());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return plugins.get(plugin.getName());
	}

	public FrontService getFrontService() {
		return VosaoContext.getInstance().getFrontService();
	}

	public BackService getBackService() {
		return VosaoContext.getInstance().getBackService();
	}

	@Override
	public HttpServlet getPluginServlet(HttpServletRequest request) {
		String url = request.getServletPath();
		if (!url.startsWith("/_ah/plugin")) {
			return null;
		}
		String[] tokens = request.getRequestURI().toString().split("/");
		if (tokens.length < 4) {
			return null;
		}
		String pluginName = tokens[3];
		String servlet = tokens[4];
		PluginEntity plugin = getDao().getPluginDao().getByName(pluginName);
		if (plugin == null || plugin.isDisabled()) {
			return null;
		}
		try {
			PluginEntryPoint entryPoint = getEntryPoint(plugin);
			return entryPoint == null ? null : 
				entryPoint.getServlets().get(servlet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void cronSchedule(Date date) {
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			PluginEntryPoint entry = getBusiness().getPluginBusiness()
					.getEntryPoint(plugin);
			if (entry == null) {
				return;
			}
			for (PluginCronJob job : entry.getJobs()) {
				try {
					if (job.isShowTime(date)) {
						job.run();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public ClassLoader getClassLoader(PluginEntity plugin) {
		return getPluginClassLoaderFactory().getClassLoader(plugin.getName());
	}

	@Override
	public Class loadClass(String name) {
		List<PluginEntity> plugins = getDao().getPluginDao().selectEnabled();
		for (PluginEntity plugin : plugins) {
			ClassLoader classLoader = getClassLoader(plugin);
			try {
				return classLoader.loadClass(name);
			}
			catch (ClassNotFoundException e) {
			}
		}
		return null;
	}

}
