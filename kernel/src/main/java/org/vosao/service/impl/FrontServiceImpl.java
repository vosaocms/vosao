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

package org.vosao.service.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.service.FrontService;
import org.vosao.service.front.ChannelApiService;
import org.vosao.service.front.CommentService;
import org.vosao.service.front.FormService;
import org.vosao.service.front.LoginService;
import org.vosao.service.front.SearchService;
import org.vosao.service.front.impl.ChannelApiServiceImpl;
import org.vosao.service.front.impl.CommentServiceImpl;
import org.vosao.service.front.impl.FormServiceImpl;
import org.vosao.service.front.impl.LoginServiceImpl;
import org.vosao.service.front.impl.SearchServiceImpl;
import org.vosao.service.plugin.PluginServiceManager;

public class FrontServiceImpl implements FrontService, Serializable {

	private static final Log log = LogFactory.getLog(FrontServiceImpl.class);

	private LoginService loginService;
	private FormService formService;
	private CommentService commentService;
	private SearchService searchService;
	private ChannelApiService channelApiService;
	
	@Override
	public void register(JSONRPCBridge bridge) {
		bridge.registerObject("loginFrontService", getLoginService());
		bridge.registerObject("formFrontService", getFormService());
		bridge.registerObject("commentFrontService", getCommentService());
		bridge.registerObject("searchFrontService", getSearchService());
		bridge.registerObject("channelApiFrontService", getChannelApiService());
		registerPluginServices(bridge);
	}
	
	@Override
	public void unregister(JSONRPCBridge bridge) {
		bridge.unregisterObject("loginFrontService");
		bridge.unregisterObject("formFrontService");
		bridge.unregisterObject("commentFrontService");
		bridge.unregisterObject("searchFrontService");
		bridge.unregisterObject("channelApiFrontService");
		unregisterPluginServices(bridge);
	}

	@Override
	public FormService getFormService() {
		if (formService == null) {
			formService = new FormServiceImpl();
		}
		return formService;
	}

	@Override
	public void setFormService(FormService bean) {
		formService = bean;
	}

	@Override
	public LoginService getLoginService() {
		if (loginService == null) {
			loginService = new LoginServiceImpl();
		}
		return loginService;
	}

	@Override
	public void setLoginService(LoginService bean) {
		loginService = bean;
	}

	@Override
	public CommentService getCommentService() {
		if (commentService == null) {
			commentService = new CommentServiceImpl();
		}
		return commentService;
	}

	@Override
	public void setCommentService(CommentService bean) {
		commentService = bean;
	}

	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}

	private Dao getDao() {
		return getBusiness().getDao();
	}
	
	private void registerPluginServices(JSONRPCBridge bridge) {
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			try {
				PluginServiceManager manager = getBusiness()
					.getPluginBusiness().getFrontServices(plugin);
				if (manager != null) {
					manager.register(bridge);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void unregisterPluginServices(JSONRPCBridge bridge) {
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			try {
				PluginServiceManager manager = getBusiness()
					.getPluginBusiness().getFrontServices(plugin);
				if (manager != null) {
					manager.unregister(bridge);
				}				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public SearchService getSearchService() {
		if (searchService == null) {
			searchService = new SearchServiceImpl();
		}
		return searchService;
	}

	@Override
	public void setSearchService(SearchService bean) {
		searchService = bean;
	}

	@Override
	public ChannelApiService getChannelApiService() {
		if (channelApiService == null) {
			channelApiService = new ChannelApiServiceImpl();
		}
		return channelApiService;
	}

	@Override
	public void setChannelApiService(ChannelApiService bean) {
		channelApiService = bean;
	}

}
