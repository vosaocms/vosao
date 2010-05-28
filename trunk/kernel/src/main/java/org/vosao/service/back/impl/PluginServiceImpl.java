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
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.entity.PluginEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PluginService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class PluginServiceImpl extends AbstractServiceImpl 
		implements PluginService {

	@Override
	public List<PluginEntity> select() {
		return getDao().getPluginDao().select();
	}

	@Override
	public ServiceResponse remove(Long id) {
		try {
			PluginEntity plugin = getDao().getPluginDao().getById(id);
			if (plugin != null) {
				getBusiness().getPluginBusiness().uninstall(plugin);
				return ServiceResponse.createSuccessResponse(
						Messages.get("plugin.success_uninstall"));
			}
			else {
				return ServiceResponse.createErrorResponse(
						Messages.get("plugin.not_found"));
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}

	@Override
	public List<PluginPropertyVO> getProperties(Long pluginId) {
		PluginEntity plugin = getDao().getPluginDao().getById(pluginId);
		if (plugin == null) {
			return Collections.EMPTY_LIST;
		}
		return getBusiness().getPluginBusiness().getProperties(plugin);	
	}

	@Override
	public PluginEntity getById(Long pluginId) {
		return getDao().getPluginDao().getById(pluginId);
	}

	@Override
	public PluginEntity getByName(String pluginName) {
		return getDao().getPluginDao().getByName(pluginName);
	}

	@Override
	public ServiceResponse savePluginConfig(Long pluginId, String xml) {
		PluginEntity plugin = getById(pluginId);
		if (plugin != null) {
			plugin.setConfigData(xml);
			getDao().getPluginDao().save(plugin);
			return ServiceResponse.createSuccessResponse(
					Messages.get("config.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("plugin.not_found"));
		}
	}

}
