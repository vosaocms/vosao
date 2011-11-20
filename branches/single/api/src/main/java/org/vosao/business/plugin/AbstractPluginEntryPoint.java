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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;
import org.vosao.service.plugin.PluginServiceManager;

public abstract class AbstractPluginEntryPoint implements PluginEntryPoint {

	private Business business;
	private FrontService frontService;
	private BackService backService;
	private Map<String, HttpServlet> servlets;
	private List<PluginCronJob> jobs;
	private boolean headInclude;
	
	public AbstractPluginEntryPoint() {
		servlets = new HashMap<String, HttpServlet>();
		jobs = new ArrayList<PluginCronJob>();
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public void uninstall() {
	}

	@Override
	public PluginServiceManager getPluginBackService() {
		return null;
	}

	@Override
	public PluginServiceManager getPluginFrontService() {
		return null;
	}

	@Override
	public Object getPluginVelocityService() {
		return null;
	}

	@Override
	public BackService getBackService() {
		return backService;
	}

	@Override
	public Business getBusiness() {
		return business;
	}

	protected Dao getDao() {
		return getBusiness().getDao();
	}

	@Override
	public FrontService getFrontService() {
		return frontService;
	}

	@Override
	public void setBackService(BackService bean) {
		backService = bean;
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;
	}

	@Override
	public void setFrontService(FrontService bean) {
		frontService = bean;
	}

	@Override
	public Map<String, HttpServlet> getServlets() {
		return servlets;
	}

	@Override
	public List<PluginCronJob> getJobs() {
		return jobs;
	}
	
	@Override
	public String getHeadBeginInclude() {
		return "";
	}
	
	@Override
	public String getBundleName() {
		return null;
	}

	@Override
	public boolean isHeadInclude() {
		return headInclude;
	}
	
	@Override
	public void setHeadInclude(boolean value) {
		headInclude = value;
	}

	@Override
	public Map<String,String> getRewriteRules() {
		return Collections.EMPTY_MAP;
	}
}
