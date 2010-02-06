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

package org.vosao.plugins.superfish;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.plugin.AbstractServicePlugin;

public class SuperfishBackService extends AbstractServicePlugin {

	private static final Log logger = LogFactory.getLog(SuperfishBackService.class);
	
	public SuperfishBackService(Dao dao, Business business) {
		setDao(dao);
		setBusiness(business);
	}
	
	public ServiceResponse enablePage(String page, String enabledStr) {
		try {
		
		boolean enabled = Boolean.valueOf(enabledStr);	
		PluginEntity plugin = getDao().getPluginDao().getByName("superfish");
		SuperfishConfig config = new SuperfishConfig(plugin.getConfigData());
		if (enabled) {
			if (!config.getEnabledPages().contains(page)) {
				config.getEnabledPages().add(page);
			}
		}
		else {
			if (config.getEnabledPages().contains(page)) {
				config.getEnabledPages().remove(page);
			}
		}
		plugin.setConfigData(config.toXML());
		getDao().getPluginDao().save(plugin);
		return ServiceResponse.createSuccessResponse("Changes successfully saved.");
		
		}
		catch (Exception e) {
			e.printStackTrace();			
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}
	
	
}