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
import org.vosao.service.FrontService;
import org.vosao.service.front.FormService;
import org.vosao.service.front.LoginService;

public class FrontServiceImpl implements FrontService, Serializable {

	private static final Log log = LogFactory.getLog(FrontServiceImpl.class);

	private LoginService loginService;
	private FormService formService;
	
	public void init() {
	}

	@Override
	public void register(JSONRPCBridge bridge) {
		bridge.registerObject("loginFrontService", loginService);
		bridge.registerObject("formFrontService", formService);
	}
	
	@Override
	public void unregister(JSONRPCBridge bridge) {
		bridge.unregisterObject("loginFrontService");
		bridge.unregisterObject("formFrontService");
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
}
