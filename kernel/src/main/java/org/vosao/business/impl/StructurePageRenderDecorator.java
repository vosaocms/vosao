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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.vosao.business.PageBusiness;
import org.vosao.business.vo.StructureFieldVO;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.global.SystemService;

public class StructurePageRenderDecorator extends AbstractPageRenderDecorator {

	private StructureEntity structure;
	private StructureTemplateEntity structureTemplate;
	private Map<String, String> contentMap;

	public StructurePageRenderDecorator(PageEntity page,	String languageCode, 
			Dao dao,
			PageBusiness pageBusiness, 
			SystemService systemService) {
		super();
		setPage(page);
		setLanguageCode(languageCode);
		setDao(dao);
		setPageBusiness(pageBusiness);
		setSystemService(systemService);
		initStructure();
		prepareContent();
	}

	private void initStructure() {
		structure = getDao().getStructureDao().getById(getPage()
				.getStructureId());
		structureTemplate = getDao().getStructureTemplateDao().getById(
				getPage().getStructureTemplateId());
	}
	
	private void prepareContent() {
		try {
			contentMap = createContentMap();
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
			setContent(e.getMessage());
			return;
		}
		prepareVelocityContent(contentMap);
		if (structureTemplate.isVelocity()) {
			VelocityContext context = getPageBusiness().createContext(
					getLanguageCode()); 
			context.put("content", contentMap);
			context.put("page", getPage());
			setContent(getSystemService().render(structureTemplate.getContent(), 
					context));
		}
		if (structureTemplate.isXSLT()) {
			String xml = createContentXML(contentMap);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				InputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				Transformer transformer = getSystemService().getTransformer(
						structureTemplate.getContent());
				if (transformer != null) {
					transformer.transform(new StreamSource(input), 
							new StreamResult(output));
					setContent(output.toString().replaceAll("\\<\\?.*?\\?>", ""));
				}
				else {
					setContent("There is a problem during creating XSLT transfomer.");
				}					
			} catch (TransformerException e) {
				logger.error(e.getMessage());
				setContent(e.getMessage());
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
				setContent(e.getMessage());
			}
		}
	}
	
	private void prepareVelocityContent(Map<String, String> contentMap) {
		if (isVelocityProcessing()) {
			VelocityContext context = getPageBusiness().createContext(
				getLanguageCode());
			context.put("page", getPage());
			for (String key : contentMap.keySet()) {
				contentMap.put(key, getSystemService().render(contentMap.get(key), 
					context));
			}
		}
	}

	private Map<String, String> createContentMap() throws DocumentException {
		Map<String, String> result = new HashMap<String, String>();
		List<StructureFieldVO> fields = structure.getFields();
		String xml = getPageBusiness().getPageContent(getPage(), 
				getLanguageCode()).getContent();
		Document doc = DocumentHelper.parseText(xml);
		for (StructureFieldVO field : fields) {
				String fieldContent = StringEscapeUtils.unescapeHtml(
						doc.getRootElement().elementText(field.getName()));
				result.put(field.getName(), fieldContent);
		}
		return result;
	}
	
	private String createContentXML(Map<String, String> contentMap) {
		StringBuffer xml = new StringBuffer("<content>");
		List<StructureFieldVO> fields = structure.getFields();
		for (StructureFieldVO field : fields) {
			String fieldContent = contentMap.get(field.getName());
			xml.append("<").append(field.getName()).append(">")
					.append(StringEscapeUtils.escapeHtml(fieldContent))
					.append("</").append(field.getName()).append(">");
		}
		return xml + "</content>";
	}
	
	public Map<String, String> getContentMap() {
		return contentMap;
	}
}
