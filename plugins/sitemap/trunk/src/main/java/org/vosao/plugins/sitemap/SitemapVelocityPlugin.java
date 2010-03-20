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

package org.vosao.plugins.sitemap;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.datanucleus.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.vo.PluginPropertyVO;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.ParamUtil;
import org.vosao.utils.StreamUtil;
import org.vosao.velocity.plugin.AbstractVelocityPlugin;

public class SitemapVelocityPlugin extends AbstractVelocityPlugin {

	private static final Log logger = LogFactory.getLog(
			SitemapVelocityPlugin.class);

	public SitemapVelocityPlugin(Business business) {
		setBusiness(business);
	}
	
	public String render() {
		try {
			PluginEntity plugin = getDao().getPluginDao().getByName("sitemap");
			TreeItemDecorator<PageEntity> root = getBusiness().getPageBusiness()
				.getTree();
			String template = StreamUtil.getTextResource(
				SitemapVelocityPlugin.class.getClassLoader(), 
				"org/vosao/plugins/sitemap/sitemap.vm");
			VelocityContext context = new VelocityContext();
			context.put("root", root);
			context.put("config", getConfig(plugin));
			context.put("languageCode", getBusiness().getLanguage());
			return getBusiness().getSystemService().render(template, context);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private Map<String, Object> getConfig(PluginEntity plugin) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			Map<String, PluginPropertyVO> map = getBusiness().getPluginBusiness()
					.getPropertiesMap(plugin);
			PluginPropertyVO level = map.get("level");
			if (level != null) {
				result.put("level", ParamUtil.getInteger(
						level.getDefaultValue(), 2));
			}
			return result;
		}
		try {
			Document doc = DocumentHelper.parseText(plugin.getConfigData());
			Element root = doc.getRootElement();
			result.put("level", ParamUtil.getInteger(root.elementText("level"), 
					2));
		}
		catch (DocumentException e) {
			logger.error("Sitemap plugin config DocumentException" + e.getMessage());
		}
		return result;
	}
	
}
