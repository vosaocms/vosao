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

package org.vosao.business;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.jabsorb.JSONRPCBridge;
import org.vosao.business.plugin.PluginEntryPoint;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.service.plugin.PluginServiceManager;
import org.vosao.velocity.plugin.VelocityPlugin;

/**
 * @author Alexander Oleynik
 */
public interface PluginBusiness {

	/**
	 * Installs war file as Vosao plugin.
	 * @param filename
	 * @param data
	 * @throws IOException 
	 * @throws PluginException 
	 * @throws DocumentException 
	 */
	void install(String filename, byte[] data) 
			throws IOException, PluginException, DocumentException;
	
	/**
	 * Remove plugin from system.
	 * @param plugin - plugin for remove.
	 */
	void uninstall(PluginEntity plugin);
	
	/**
	 * Get plugin entry point.
	 * @param plugin - plugin.
	 * @return - Plugin entry point service.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	PluginEntryPoint getEntryPoint(PluginEntity plugin);

	/**
	 * Get plugin Velocity service for page rendering.
	 * @param plugin - plugin.
	 * @return - Velocity service.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	Object getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, 
			IllegalAccessException;
	
	/**
	 * Reload all plugins resources and cache reset.
	 * @param plugin - plugin for reset.
	 */
	void resetPlugin(PluginEntity plugin);
	
	/**
	 * Get plugin config XML structure. Config values are stored as XML in 
	 * plugin.getConfigData()
	 * @param plugin - plugin for structure.
	 * @return - list of config structure properties.
	 */
	List<PluginPropertyVO> getProperties(PluginEntity plugin);

	/**
	 * Get plugin config XML structure. Config values are stored as XML in 
	 * plugin.getConfigData()
	 * @param plugin - plugin for structure.
	 * @return - map of config structure properties. Property name as key.
	 */
	Map<String, PluginPropertyVO> getPropertiesMap(PluginEntity plugin);
	
	PluginServiceManager getBackServices(PluginEntity plugin)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException;

	PluginServiceManager getFrontServices(PluginEntity plugin)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException;
	
	HttpServlet getPluginServlet(HttpServletRequest request);
	
	/**
	 * Schedule and run if necessary plugins cron jobs. 
	 * @param date - run datetime.
	 */
	void cronSchedule(Date date);

}
