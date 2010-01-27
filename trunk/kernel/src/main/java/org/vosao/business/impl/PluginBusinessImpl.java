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

package org.vosao.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.PluginBusiness;
import org.vosao.business.PluginResourceBusiness;
import org.vosao.common.PluginException;
import org.vosao.entity.PluginEntity;
import org.vosao.velocity.plugin.VelocityPlugin;

public class PluginBusinessImpl extends AbstractBusinessImpl 
	implements PluginBusiness {

	private static final Log logger = LogFactory.getLog(PluginBusinessImpl.class);

	private static final String VOSAO_PLUGIN = "WEB-INF/vosao-plugin.xml";
	
	private PluginResourceBusiness pluginResourceBusiness;
	
	private static class ZipItem {
		public String path;
		public ByteArrayOutputStream data;

		public ZipItem(String path, ByteArrayOutputStream data) {
			super();
			this.path = path;
			this.data = data;
		}
	}
	
	@Override
	public void install(String filename, byte[] data) throws IOException, 
			PluginException, DocumentException {
		Map<String, ZipItem> war = readWar(data);
		if (!war.containsKey(VOSAO_PLUGIN)) {
			throw new PluginException(VOSAO_PLUGIN + " not found");
		}
		PluginEntity plugin = readPluginConfig(war.get(VOSAO_PLUGIN));
		PluginEntity p = getDao().getPluginDao().getByName(plugin.getName());
		if (p != null) {
			throw new PluginException("Plugin " + plugin.getTitle() + " already installed.");
		}
		getDao().getPluginDao().save(plugin);
	}
	
	private Map<String, ZipItem> readWar(byte[] data) throws IOException {
		ByteArrayInputStream inputData = new ByteArrayInputStream(data);
		ZipInputStream in = new ZipInputStream(inputData);
		Map<String, ZipItem> map = new HashMap<String, ZipItem>();
		ZipEntry entry;
		byte[] buffer = new byte[4096];
		while((entry = in.getNextEntry()) != null) {
			if (entry.getName().startsWith("META-INF")) {
				continue;
			}
			if (entry.isDirectory()) {
			}
			else {
				ByteArrayOutputStream itemData = new ByteArrayOutputStream();
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					itemData.write(buffer, 0, len);
				}
				ZipItem item = new ZipItem(entry.getName(), itemData);
				map.put(entry.getName(), item);
				logger.info(entry.getName());
			}
		}
		in.close();
		return map;
	}

	private PluginEntity readPluginConfig(ZipItem zipItem) 
			throws UnsupportedEncodingException, DocumentException {
		PluginEntity result = new PluginEntity();
		Element root = DocumentHelper.parseText(zipItem.data.toString("UTF-8"))
				.getRootElement();
		result.setName(root.elementText("name"));	
		result.setTitle(root.elementText("title"));
		result.setDescription(root.elementText("description"));
		result.setWebsite(root.elementText("website"));
		if (root.element("velocity-plugin-class") != null) {
			result.setVelocityPluginClass(root.elementText("velocity-plugin-class"));
		}
		// TODO load config xml
		return result; 
	}

	@Override
	public VelocityPlugin getVelocityPlugin(PluginEntity plugin) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		PluginClassLoader loader = new PluginClassLoader(pluginResourceBusiness);
		Class velocityPluginClass = loader.findClass(plugin.getVelocityPluginClass());
		return (VelocityPlugin) velocityPluginClass.newInstance();
	}

	public PluginResourceBusiness getPluginResourceBusiness() {
		return pluginResourceBusiness;
	}

	public void setPluginResourceBusiness(
			PluginResourceBusiness pluginResourceBusiness) {
		this.pluginResourceBusiness = pluginResourceBusiness;
	}

}
