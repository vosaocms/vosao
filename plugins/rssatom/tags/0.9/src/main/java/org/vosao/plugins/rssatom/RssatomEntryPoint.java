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

package org.vosao.plugins.rssatom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.StreamUtil;
import org.vosao.utils.XmlUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class RssatomEntryPoint extends AbstractPluginEntryPoint {

	private static final String FEED_PLUGIN_URL = "/_ah/plugin/rssatom/feed";
	
	private Map<String, String> rules;
	
	@Override
	public void init() {
		checkConfig();
		getServlets().put("feed", new RssatomServlet(getBusiness()));
	}
	
	@Override
	public String getHeadBeginInclude() {
		RssatomConfig rssatomConfig = new RssatomConfig(getPlugin());
		return "<link href=\"/_ah/plugin/rssatom/feed?format=atom\" "
			+ "type=\"application/atom+xml\" rel=\"alternate\" title=\""
			+ rssatomConfig.getTitle() + "\" />";
	}

	private PluginEntity getPlugin() {
		return getBusiness().getDao().getPluginDao().getByName("rssatom");
	}
	
	private void checkConfig() {
		PluginEntity plugin = getPlugin();
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			RssatomConfig rssatomConfig = new RssatomConfig(plugin);
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("plugin-config");
			root.addElement("items").setText(String.valueOf(
					rssatomConfig.getItems()));
			root.addElement("itemSize").setText(String.valueOf(
					rssatomConfig.getItemSize()));
			root.addElement("pages").setText(XmlUtil.notNull(
					rssatomConfig.getPages()));
			root.addElement("title").setText(XmlUtil.notNull(
					rssatomConfig.getTitle()));
			try {
				root.addElement("rssTemplate").setText(
					StreamUtil.getTextResource(this.getClass().getClassLoader(),
							"org/vosao/plugins/rssatom/rss-template.vm"));
			}
			catch (IOException e) {
				root.addElement("rssTemplate").setText(e.getMessage());
			}
			try {
				root.addElement("atomTemplate").setText(
					StreamUtil.getTextResource(this.getClass().getClassLoader(),
							"org/vosao/plugins/rssatom/atom-template.vm"));
			}
			catch (IOException e) {
				root.addElement("atomTemplate").setText(e.getMessage());
			}
			plugin.setConfigData(doc.asXML());
			getDao().getPluginDao().save(plugin);
			rules = null;
		}
	}
	
	@Override
	public String getBundleName() {
		return "org.vosao.plugins.rssatom.messages";
	}
	
	@Override
	public Map<String,String> getRewriteRules() {
		if (rules == null) {
			rules = new HashMap<String, String>();
			RssatomConfig rssatomConfig = new RssatomConfig(getPlugin());
			if (StringUtils.isNotEmpty(rssatomConfig.getRewriteUrl())) {
				rules.put(rssatomConfig.getRewriteUrl(), FEED_PLUGIN_URL);
			}
		}
		return rules;
	}
}
