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

package org.vosao.business.impl.imex;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.business.impl.imex.dao.DaoTaskAdapter;
import org.vosao.dao.Dao;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * @author Alexander Oleynik
 */
public class PluginExporter extends AbstractExporter {

	public PluginExporter(ExporterFactory factory) {
		super(factory);
	}
	
	public String createPluginsXML() {
		Document doc = DocumentHelper.createDocument();
		Element PluginsElement = doc.addElement("plugins");
		List<PluginEntity> list = getDao().getPluginDao().select();
		for (PluginEntity plugin : list) {
			createPluginXML(PluginsElement, plugin);
		}
		return doc.asXML();
	}

	private void createPluginXML(Element pluginsElement, 
			final PluginEntity plugin) {
		Element pluginElement = pluginsElement.addElement("plugin");
		pluginElement.addElement("name").setText(plugin.getName());
		pluginElement.addElement("configData").setText(plugin.getConfigData());
	}
	
	public void readPlugins(Element PluginsElement) throws DaoTaskException {
		for (Iterator<Element> i = PluginsElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("plugin")) {
            	String name = element.elementText("name");
            	PluginEntity plugin = getDao().getPluginDao().getByName(name);
            	if (plugin == null) {
            		continue;
            	}
            	plugin.setName(name);
            	plugin.setConfigData(element.elementText("configData"));
            	getDaoTaskAdapter().pluginSave(plugin);
            }
		}		
	}
}
