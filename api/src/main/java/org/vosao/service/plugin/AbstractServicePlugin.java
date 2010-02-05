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

package org.vosao.service.plugin;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;

abstract public class AbstractServicePlugin implements ServicePlugin {

	private Dao dao;
	private Business business;
	private FrontService frontService;
	private BackService backService;
	
	@Override
	public Business getBusiness() {
		return business;
	}

	@Override
	public Dao getDao() {
		return dao;
	}

	@Override
	public void setBusiness(Business bean) {
		business = bean;		
	}

	@Override
	public void setDao(Dao bean) {
		dao = bean;
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
