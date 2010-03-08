package org.vosao.webdav.sysfile.global;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class StructuresFileResource extends AbstractFileResource {

	public StructuresFileResource(Business aBusiness, String name) {
		super(aBusiness, name, new Date());
		setContentType("text/xml");
		setData(new byte[0]);
	}

	@Override
	public void sendContent(OutputStream out, Range range,
			Map<String, String> params, String aContentType) throws IOException,
			NotAuthorizedException, BadRequestException {
		createXML();
		super.sendContent(out, range, params, aContentType);
	}

	private void createXML() throws UnsupportedEncodingException {
		Document doc = DocumentHelper.createDocument();
		Element e = doc.addElement("structures");
		List<StructureEntity> list = getDao().getStructureDao().select();
		for (StructureEntity structure : list) {
			createStructureXML(e, structure);
		}
		setData(doc.asXML().getBytes("UTF-8"));
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

}
