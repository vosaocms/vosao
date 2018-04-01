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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.StructureExporter;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.enums.StructureTemplateType;
import org.vosao.utils.XmlUtil;

/**
 * @author Alexander Oleynik
 */
public class StructureExporterImpl extends AbstractExporter 
		implements StructureExporter {

	public StructureExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createStructuresXML() {
		Document doc = DocumentHelper.createDocument();
		Element structuresElement = doc.addElement("structures");
		createStructuresXML(structuresElement);
		return doc.asXML();
	}

	private void createStructuresXML(Element structuresElement) {
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
		templateElement.addElement("name").setText(template.getName());
		templateElement.addElement("type").setText(template.getTypeString());
		templateElement.addElement("content").setText(template.getContent());
		templateElement.addElement("headContent").setText(XmlUtil.notNull(template.getHeadContent()));
	}	
	
	@Override
	public void readStructures(Element structuresElement) 
			throws DaoTaskException {
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
            	getDaoTaskAdapter().structureSave(structure);
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
            	String name = element.elementText("name");
            	name = name == null ? title : name;
            	String content = element.elementText("content");
            	StructureTemplateType type = StructureTemplateType.valueOf(
            			element.elementText("type"));
            	StructureTemplateEntity template = getDao()
            			.getStructureTemplateDao().getByName(name);
            	if (template == null) {
            		template = new StructureTemplateEntity(name,
            			title, structure.getId(), type, content);
            	}
            	template.setHeadContent(element.elementText("headContent"));
            	getDao().getStructureTemplateDao().save(template);
            }
		}
	}
	
	/**
	 * Read and import data from _structures.xml file.
	 * @param xml - _structures.xml content.
	 * @throws DocumentException
	 * @throws DaoTaskException
	 */
	@Override
	public void readStructuresFile(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		readStructures(doc.getRootElement());
	}

	@Override
	public void exportStructures(ZipOutStreamTaskAdapter out,
			List<StructureEntity> structures) throws IOException,
			TaskTimeoutException {
		saveFile(out, "_structures.xml", createStructuresXML());
	}
}
