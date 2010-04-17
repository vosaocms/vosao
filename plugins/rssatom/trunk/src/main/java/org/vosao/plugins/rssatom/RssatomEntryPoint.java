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

package org.vosao.plugins.rssatom;

import java.io.IOException;

import org.datanucleus.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.plugin.AbstractPluginEntryPoint;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.StreamUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class RssatomEntryPoint extends AbstractPluginEntryPoint {

	@Override
	public void init() {
		checkConfig();
		getServlets().put("feed", new RssatomServlet(getBusiness()));
	}
	
	private void checkConfig() {
		PluginEntity plugin = getBusiness().getDao().getPluginDao().getByName(
				"rssatom");
		if (StringUtils.isEmpty(plugin.getConfigData())) {
			RssatomConfig rssatomConfig = new RssatomConfig(plugin);
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("plugin-config");
			root.addElement("items").setText(String.valueOf(
					rssatomConfig.getItems()));
			root.addElement("pages").setText(rssatomConfig.getPages());
			try {
				root.addElement("rssTemplate").setText(
					StreamUtil.getTextResource(
							"org/vosao/plugins/rssatom/rss-template.vm"));
			}
			catch (IOException e) {
				root.addElement("rssTemplate").setText(e.getMessage());
			}
			try {
				root.addElement("atomTemplate").setText(
					StreamUtil.getTextResource(
							"org/vosao/plugins/rssatom/atom-template.vm"));
			}
			catch (IOException e) {
				root.addElement("atomTemplate").setText(e.getMessage());
			}
			plugin.setConfigData(doc.asXML());
			getDao().getPluginDao().save(plugin);
		}
	}
	
}
