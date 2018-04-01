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

package org.vosao.service.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;

abstract public class AbstractServicePlugin implements ServicePlugin {

	protected static final Log logger = LogFactory.getLog(
			AbstractServicePlugin.class);

	private Business business;
	private FrontService frontService;
	private BackService backService;
	
	@Override
	public Business getBusiness() {
		return business;
	}

	@Override
	public Dao getDao() {
		return business.getDao();
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;		
	}

	@Override
	public FrontService getFrontService() {
		return frontService;
	}

	@Override
	public void setFrontService(FrontService bean) {
		frontService = bean;
	}

	@Override
	public BackService getBackService() {
		return backService;
	}

	@Override
	public void setBackService(BackService bean) {
		backService = bean;
	}
	
}
