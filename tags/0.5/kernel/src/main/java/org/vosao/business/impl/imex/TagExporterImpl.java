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
import org.vosao.business.imex.ExporterFactory;
import org.vosao.business.imex.TagExporter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.TagEntity;

/**
 * @author Alexander Oleynik
 */
public class TagExporterImpl extends AbstractExporter 
		implements TagExporter {

	public TagExporterImpl(ExporterFactory factory) {
		super(factory);
	}
	
	@Override
	public String createXML() {
		Document doc = DocumentHelper.createDocument();
		Element element = doc.addElement("tags");
		List<TagEntity> list = getDao().getTagDao().selectByParent(null);
		for (TagEntity tag : list) {
			createTagXML(element, tag);
		}
		return doc.asXML();
	}

	private void createTagXML(Element element, TagEntity tag) {
		Element tagElement = element.addElement("tag");
		tagElement.addElement("name").setText(tag.getName());
		List<TagEntity> list = getDao().getTagDao().selectByParent(tag.getId());
		for (TagEntity child : list) {
			createTagXML(tagElement, child);
		}
	}
	
	public void readTags(Element tagsElement, Long parent) throws DaoTaskException {
		for (Iterator<Element> i = tagsElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("tag")) {
            	String name = element.elementText("name");
            	TagEntity tag = getDao().getTagDao().getByName(parent, name);
            	if (tag == null) {
            		tag = new TagEntity(parent, name);
            	}
            	tag.setName(name);
            	getDaoTaskAdapter().tagSave(tag);
            	readTags(element, tag.getId());
            }
		}		
	}
	
	/**
	 * Read and import data from _messages.xml file.
	 * @param xml - _messages.xml file content.
	 * @throws DocumentException
	 * @throws DaoTaskException
	 */
	public void read(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		readTags(doc.getRootElement(), null);
	}
}
