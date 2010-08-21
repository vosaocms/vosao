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

package org.vosao.business.plugin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.vosao.business.Business;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public interface PluginEntryPoint {

	/**
	 * Plugin initialization. Called after setting dao, business and services.
	 */
	public void init();
	
	/**
	 * Plugin uninstall callback. Called before uninstall process.
	 */
	public void uninstall();
	
	Business getBusiness();
	void setBusiness(Business bean);

	BackService getBackService();
	void setBackService(BackService bean);
	
	FrontService getFrontService();
	void setFrontService(FrontService bean);

	PluginServiceManager getPluginBackService();

	PluginServiceManager getPluginFrontService();
	
	Object getPluginVelocityService();
	
	/**
	 * Get plugin servlets map. 
	 * All plugin servlets mapped to url /plugin/PLUGIN_NAME/PATH
	 * Map key is PATH element.
	 * @return
	 */
	Map<String, HttpServlet> getServlets();
	
	List<PluginCronJob> getJobs();
	
	/**
	 * Get html head tag include fragment. All pages will include this fragment in 
	 * head tag.
	 * @return head tag fragment.
	 */
	String getHeadBeginInclude();
	
	boolean isHeadInclude();
	
	void setHeadInclude(boolean value);
	
	/**
	 * Get message bundle class path. For example: org.vosao.resources.messages
	 * @return bundle name.
	 */
	String getBundleName();
}
