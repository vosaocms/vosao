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

package org.vosao.plugins.superfish;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SuperfishConfig {

	private static final Log logger = LogFactory.getLog(SuperfishConfig.class);
	
	private Map<String, Integer> enabledPages;
	private boolean showHomepage;

	public SuperfishConfig() {
		enabledPages = new HashMap<String, Integer>();
		showHomepage = true;
	}
	
	public static SuperfishConfig parse(String xml) throws DocumentException {
		SuperfishConfig config = new SuperfishConfig();
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		config.setShowHomepage(Boolean.valueOf(root.elementText("showHomepage")));
		for (Element e : (List<Element>)root.element("pages").elements()) {
			String url = e.getText();
			int index = Integer.valueOf(e.attributeValue("index"));
			config.getEnabledPages().put(url, index);
		}
		return config;
	}
	
	public String toXML() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("config");
		root.addElement("showHomepage").setText(String.valueOf(isShowHomepage()));
		Element pages = root.addElement("pages");
		for (String url : enabledPages.keySet()) {
			Element page = pages.addElement("page");
			page.setText(url);
			page.addAttribute("index", String.valueOf(enabledPages.get(url)));
		}
		return doc.asXML();
	}
	
	public Map<String, Integer> getEnabledPages() {
		return enabledPages;
	}

	public void setEnabledPages(Map<String, Integer> enabledPages) {
		this.enabledPages = enabledPages;
	}

	public boolean isShowHomepage() {
		return showHomepage;
	}

	public void setShowHomepage(boolean showHomepage) {
		this.showHomepage = showHomepage;
	}
	
}
