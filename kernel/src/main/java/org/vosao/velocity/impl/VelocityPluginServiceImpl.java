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

package org.vosao.velocity.impl;

import java.util.HashMap;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;
import org.vosao.velocity.VelocityPluginService;
import org.vosao.velocity.plugin.VelocityPlugin;

public class VelocityPluginServiceImpl implements VelocityPluginService {

	private Dao dao;
	private SystemService systemService;
	private FormVelocityService form;
	private Business business;
	
	public VelocityPluginServiceImpl(Dao aDao, SystemService aSystemService,
			Business aBusiness) {
		dao = aDao;
		systemService= aSystemService;
		form = new FormVelocityServiceImpl(dao, systemService);
		business = aBusiness;
	}
	
	@Override
	public Map<String, Object> getPlugins() {
		Map<String, Object> services = new HashMap<String, Object>();
		services.put("form", form);
		for (PluginEntity plugin : dao.getPluginDao().select()) {
			try {
				Object velocityPlugin = business.getPluginBusiness()
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
}
