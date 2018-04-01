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

package org.vosao.entity.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.entity.PluginEntity;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PluginHelper {

	protected static final Log logger = LogFactory.getLog(PluginHelper.class);

	public static Map<String, PluginParameter> parseParameters(
			PluginEntity plugin) {
		Map<String, PluginParameter> result = 
			new HashMap<String, PluginParameter>();
		Document dataDoc = null;
		try {
			dataDoc = DocumentHelper.parseText(plugin.getConfigData());
		}
		catch (DocumentException e) {
		}
		try {
			Document configDoc = DocumentHelper.parseText(
					plugin.getConfigStructure());
			for (Element element : (List<Element>)configDoc.getRootElement()
					.elements()) {
				PluginParameter param = new PluginParameter(element);
				try {
					param.setValue(dataDoc.getRootElement().elementText(
						param.getName()));
				}
				catch (Exception e) {
					logger.error(e.getMessage());
				}
				result.put(param.getName(), param);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
}
