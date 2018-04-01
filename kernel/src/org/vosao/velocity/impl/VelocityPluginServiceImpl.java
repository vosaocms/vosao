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

package org.vosao.velocity.impl;

import java.util.HashMap;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;
import org.vosao.velocity.VelocityPluginService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class VelocityPluginServiceImpl implements VelocityPluginService {

	private FormVelocityService form;
	private Business business;
	
	public VelocityPluginServiceImpl(Business aBusiness) {
		business = aBusiness;
		form = new FormVelocityServiceImpl(getBusiness());
	}
	
	@Override
	public Map<String, Object> getPlugins() {
		Map<String, Object> services = new HashMap<String, Object>();
		services.put("form", form);
		for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
			try {
				Object velocityPlugin = getBusiness().getPluginBusiness()
					.getVelocityPlugin(plugin);
				if (velocityPlugin != null) {
					services.put(plugin.getName(), velocityPlugin);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return services;
	}

	public Business getBusiness() {
		return business;
	}

	public Dao getDao() {
		return getBusiness().getDao();
	}
	
	public SystemService getSystemService() {
		return getBusiness().getSystemService();
	}
	
}
