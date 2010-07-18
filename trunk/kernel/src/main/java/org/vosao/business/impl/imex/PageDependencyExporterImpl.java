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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.PageDependencyExporter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.PageDependencyEntity;

/**
 * @author Alexander Oleynik
 */
public class PageDependencyExporterImpl extends AbstractExporter 
		implements PageDependencyExporter {

	public PageDependencyExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createXML() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("dependencies");
		List<PageDependencyEntity> list = getDao().getPageDependencyDao().select();
		for (PageDependencyEntity entity : list) {
			Element itemElement = root.addElement("item");
			itemElement.addElement("page").setText(entity.getPage());
			itemElement.addElement("dependency").setText(entity.getDependency());
		}
		return doc.asXML();
	}

	public void read(Element rootElement) throws DaoTaskException {
		for (Iterator<Element> i = rootElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("item")) {
            	String page = element.elementText("page");
            	String dependency = element.elementText("dependency");
            	PageDependencyEntity entity = 
            		getDao().getPageDependencyDao().getByPageAndDependency(
            				page, dependency);
            	
            	if (entity == null) {
            		entity = new PageDependencyEntity(dependency, page);
            	}
            	getDaoTaskAdapter().pageDependencySave(entity);
            }
		}		
	}
	
	/**
	 * Read and import data from _messages.xml file.
	 * @param xml - _messages.xml file content.
	 * @throws DocumentException
	 * @throws DaoTaskException
	 */
	public void readFile(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		read(doc.getRootElement());
	}
}
