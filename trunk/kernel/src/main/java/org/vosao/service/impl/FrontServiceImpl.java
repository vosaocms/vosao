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

package org.vosao.service.impl;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jabsorb.JSONRPCBridge;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.service.FrontService;
import org.vosao.service.front.CommentService;
import org.vosao.service.front.FormService;
import org.vosao.service.front.LoginService;
import org.vosao.service.plugin.PluginServiceManager;

public class FrontServiceImpl implements FrontService, Serializable {

	private static final Log log = LogFactory.getLog(FrontServiceImpl.class);

	private Dao dao;
	private Business business;
	
	private LoginService loginService;
	private FormService formService;
	private CommentService commentService;
	
	@Override
	public void register(JSONRPCBridge bridge) {
		bridge.registerObject("loginFrontService", loginService);
		bridge.registerObject("formFrontService", formService);
		bridge.registerObject("commentFrontService", commentService);
		registerPluginServices(bridge);
	}
	
	@Override
	public void unregister(JSONRPCBridge bridge) {
		bridge.unregisterObject("loginFrontService");
		bridge.unregisterObject("formFrontService");
		bridge.unregisterObject("commentFrontService");
		unregisterPluginServices(bridge);
	}

	@Override
	public FormService getFormService() {
		return formService;
	}

	@Override
	public void setFormService(FormService bean) {
		formService = bean;
	}

	@Override
	public LoginService getLoginService() {
		return loginService;
	}

	@Override
	public void setLoginService(LoginService bean) {
		loginService = bean;
	}

	@Override
	public CommentService getCommentService() {
		return commentService;
	}

	@Override
	public void setCommentService(CommentService bean) {
		commentService = bean;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}
	
	private void registerPluginServices(JSONRPCBridge bridge) {
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			try {
				if (plugin.isFrontServicePlugin()) {
					PluginServiceManager manager = getBusiness()
						.getPluginBusiness().getFrontServices(plugin);
					manager.setFrontService(this);
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
		for (PluginEntity plugin : getDao().getPluginDao().select()) {
			try {
				if (plugin.isFrontServicePlugin()) {
					PluginServiceManager manager = getBusiness()
						.getPluginBusiness().getFrontServices(plugin);
					manager.setFrontService(this);
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

	
}
