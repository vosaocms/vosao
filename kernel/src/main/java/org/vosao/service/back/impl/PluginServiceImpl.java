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

package org.vosao.service.back.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.PluginEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PluginService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class PluginServiceImpl extends AbstractServiceImpl 
		implements PluginService {

	private static final Log logger = LogFactory.getLog(PluginServiceImpl.class);

	@Override
	public List<PluginEntity> select() {
		return getDao().getPluginDao().select();
	}

	@Override
	public ServiceResponse remove(String id) {
		try {
			PluginEntity plugin = getDao().getPluginDao().getById(id);
			if (plugin != null) {
				getBusiness().getPluginBusiness().uninstall(plugin);
				return ServiceResponse.createSuccessResponse(
						"Plugin was successfully uninstalled");
			}
			else {
				return ServiceResponse.createErrorResponse("Plugin was not found");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}

}
