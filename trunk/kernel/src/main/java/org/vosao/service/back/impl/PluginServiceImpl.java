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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.entity.PluginEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.PluginService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.PluginPropertyVO;

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

	@Override
	public List<PluginPropertyVO> getProperties(String pluginId) {
		PluginEntity plugin = getDao().getPluginDao().getById(pluginId);
		if (plugin == null) {
			return Collections.EMPTY_LIST;
		}
		List<PluginPropertyVO> result = new ArrayList<PluginPropertyVO>();
		try {
			Document doc = DocumentHelper.parseText(plugin.getConfigStructure());
			for (Element e : (List<Element>)doc.getRootElement().elements()) {
				if (e.getName().equals("param")) {
					PluginPropertyVO p = new PluginPropertyVO();
					if (e.attributeValue("name") == null) {
						logger.error("There must be name attribute for param tag.");
						continue;
					}
					if (e.attributeValue("type") == null) {
						logger.error("There must be type attribute for param tag.");
						continue;
					}
					p.setName(e.attributeValue("name"));
					p.setType(e.attributeValue("type"));
					p.setDefaultValue(e.attributeValue("value"));
					if (e.attributeValue("title") == null) {
						p.setTitle(p.getName());
					}
					else {
						p.setTitle(e.attributeValue("title"));
					}
					result.add(p);
				}
			}
			return result;
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public PluginEntity getById(String pluginId) {
		return getDao().getPluginDao().getById(pluginId);
	}

}
