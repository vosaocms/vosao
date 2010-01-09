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

import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.enums.StructureTemplateType;

/**
 * @author Alexander Oleynik
 */
public class StructureExporter extends AbstractExporter {

	public StructureExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
	}
	
	public void createStructuresXML(Element siteElement) {
		Element structuresElement = siteElement.addElement("structures");
		List<StructureEntity> list = getDao().getStructureDao().select();
		for (StructureEntity structure : list) {
			createStructureXML(structuresElement, structure);
		}
	}

	private void createStructureXML(Element structuresElement, 
			final StructureEntity structure) {
		Element structureElement = structuresElement.addElement("structure");
		structureElement.addElement("title").setText(structure.getTitle());
		structureElement.addElement("content").setText(structure.getContent());
		createTemplatesXML(structureElement,structure);
	}
	
	private void createTemplatesXML(Element structureElement, 
			StructureEntity structure) {
		Element templatesElement = structureElement.addElement("templates");
		List<StructureTemplateEntity> list = getDao().getStructureTemplateDao()
				.selectByStructure(structure.getId());
		for (StructureTemplateEntity template : list) {
			createTemplateXML(templatesElement, template);
		}
	}

	private void createTemplateXML(Element templatesElement,
			StructureTemplateEntity template) {
		Element templateElement = templatesElement.addElement("template");
		templateElement.addElement("title").setText(template.getTitle());
		templateElement.addElement("type").setText(template.getTypeString());
		templateElement.addElement("content").setText(template.getContent());
	}	
	
	public void readStructures(Element structuresElement) {
		for (Iterator<Element> i = structuresElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("structure")) {
            	String title = element.elementText("title");
            	String content = element.elementText("content");
            	StructureEntity structure = getDao().getStructureDao().getByTitle(
            			title);
            	if (structure == null) {
            		structure = new StructureEntity(title, content);
            	}
            	structure.setContent(content);
            	getDao().getStructureDao().save(structure);
            	readTemplates(element.element("templates"), structure);
            }
		}		
	}

	private void readTemplates(Element templatesElement, 
			StructureEntity structure) {
		for (Iterator<Element> i = templatesElement.elementIterator(); 
				i.hasNext(); ) {
			Element element = i.next();
            if (element.getName().equals("template")) {
            	String title = element.elementText("title");
            	String content = element.elementText("content");
            	StructureTemplateType type = StructureTemplateType.valueOf(
            			element.elementText("type"));
            	StructureTemplateEntity template = new StructureTemplateEntity(
            			title, structure.getId(), type, content);
            	getDao().getStructureTemplateDao().save(template);
            }
		}
	}
}
