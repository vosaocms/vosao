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

package org.vosao.business.impl.imex;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.PluginExporter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.PluginEntity;

/**
 * @author Alexander Oleynik
 */
public class PluginExporterImpl extends AbstractExporter 
		implements PluginExporter {

	public PluginExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createPluginsXML() {
		Document doc = DocumentHelper.createDocument();
		Element pluginsElement = doc.addElement("plugins");
		createPluginsXML(pluginsElement);
		return doc.asXML();
	}

	private void createPluginsXML(Element pluginsElement) {
		List<PluginEntity> list = getDao().getPluginDao().select();
		for (PluginEntity plugin : list) {
			createPluginXML(pluginsElement, plugin);
		}
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
	
	/**
	 * Read and import data from _plugins.xml file.
	 * @param xml - _plugins.xml file content.
	 * @throws DocumentException
	 * @throws DaoTaskException
	 */
	public void readPluginsFile(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		readPlugins(doc.getRootElement());
	}
}
