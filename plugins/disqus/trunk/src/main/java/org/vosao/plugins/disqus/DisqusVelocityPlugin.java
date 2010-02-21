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

package org.vosao.plugins.disqus;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.entity.PluginEntity;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class DisqusVelocityPlugin extends AbstractVelocityPlugin {

	private static final Log logger = LogFactory.getLog(
			DisqusVelocityPlugin.class);

	public String render() {
		try {
			PluginEntity plugin = getDao().getPluginDao().getByName("disqus");
			Map<String, String> config = getConfig(plugin);
			logger.info(config.get("embedCode") + "\n\n" + config.get("countCode"));
			return config.get("embedCode") + "\n\n" + config.get("countCode"); 
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private Map<String, String> getConfig(PluginEntity plugin) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("embedCode", "Disqus not configured");
		result.put("countCode", "");
		if (!StringUtils.isEmpty(plugin.getConfigData())) {
			try {
				Document doc = DocumentHelper.parseText(plugin.getConfigData());
				Element root = doc.getRootElement();
				result.put("embedCode", root.elementText("embedCode"));
				result.put("countCode", root.elementText("countCode"));
			}
			catch (DocumentException e) {
				logger.error("Sitemap plugin config DocumentException" + e.getMessage());
			}
		}
		return result;
	}
	
}