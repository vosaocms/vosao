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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.vosao.business.PageBusiness;
import org.vosao.business.PageRenderDecorator;
import org.vosao.business.vo.StructureFieldVO;
import org.vosao.dao.Dao;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.global.SystemService;
import org.vosao.utils.DateUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class StructurePageRenderDecorator implements PageRenderDecorator {

	private Log logger = LogFactory.getLog(SimplePageRenderDecorator.class);
	
	private PageEntity page;
	private String languageCode;
	private String content;
	private Dao dao;
	private PageBusiness pageBusiness;
	private SystemService systemService;
	private StructureEntity structure;
	private StructureTemplateEntity structureTemplate;
	
	public StructurePageRenderDecorator(PageEntity page,	String languageCode, 
			Dao dao,
			PageBusiness pageBusiness, 
			SystemService systemService) {
		super();
		this.page = page;
		this.languageCode = languageCode;
		this.dao = dao;
		this.pageBusiness = pageBusiness;
		this.systemService = systemService;
		initStructure();
		prepareContent();
	}

	private void initStructure() {
		structure = dao.getStructureDao().getById(page.getStructureId());
		structureTemplate = dao.getStructureTemplateDao().getById(
				page.getStructureTemplateId());
	}
	
	@Override
	public PageEntity getPage() {
		return page;
	}
	
	@Override
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	@Override
	public String getId() {
		return page.getId();
	}

	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public String getComments() {
		if (isCommentsEnabled()) {
			String commentsTemplate = getDao().getConfigDao().getConfig()
				.getCommentsTemplate();
			if (StringUtil.isEmpty(commentsTemplate)) {
				logger.error("comments template is empty");
				return "comments template is empty";
			}
			VelocityContext context = getPageBusiness().createContext(
					languageCode);
			context.put("page", page);
			return getSystemService().render(commentsTemplate, context);
		}
		return "";
	}
	
	private void prepareContent() {
		Map<String, String> contentMap;
		try {
			contentMap = getContentMap();
		}
		catch (DocumentException e) {
			logger.error(e.getMessage());
			content = e.getMessage();
			return;
		}
		prepareVelocityContent(contentMap);
		if (structureTemplate.isVelocity()) {
			VelocityContext context = getPageBusiness().createContext(languageCode); 
			context.put("content", contentMap);
			content = getSystemService().render(structureTemplate.getContent(), 
					context);
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
					content = output.toString();				
					content = content.replaceAll("\\<\\?.*?\\?>", "");
				}
				else {
					content = "There is a problem during creating XSLT transfomer.";
				}					
			} catch (TransformerException e) {
				logger.error(e.getMessage());
				content = e.getMessage();
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage());
				content = e.getMessage();
			}
		}
	}
	
	private void prepareVelocityContent(Map<String, String>contentMap) {
		VelocityContext context = getPageBusiness().createContext(languageCode); 
		for (String key : contentMap.keySet()) {
			contentMap.put(key, getSystemService().render(contentMap.get(key), 
					context));
		}
	}

	private Map<String, String> getContentMap() throws DocumentException {
		Map<String, String> result = new HashMap<String, String>();
		List<StructureFieldVO> fields = structure.getFields();
		String xml = getPageBusiness().getPageContent(page, languageCode)
				.getContent();
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
	
	@Override
	public String getTitle() {
		return page.getTitle();
	}

	@Override
	public String getFriendlyURL() {
		return page.getFriendlyURL();
	}

	@Override
	public String getParentUrl() {
		return page.getParentUrl();
	}
	
	@Override
	public String getTemplate() {
		return page.getTemplate();
	}
	
	@Override
	public Date getPublishDate() {
		return page.getPublishDate();
	}
	
	@Override
	public String getPublishDateString() {
		return DateUtil.toString(page.getPublishDate());
	}

	@Override
	public boolean isCommentsEnabled() {
		return page.isCommentsEnabled();
	}

	private Dao getDao() {
		return dao;
	}

	private PageBusiness getPageBusiness() {
		return pageBusiness;
	}

	private SystemService getSystemService() {
		return systemService;
	}
	
	@Override
	public Integer getVersion() {
		return page.getVersion();
	}

	@Override
	public String getVersionTitle() {
		return page.getVersionTitle();
	}

	@Override
	public String getState() {
		return page.getState().name();
	}

	@Override
	public String getCreateUserEmail() {
		return page.getCreateUserEmail();
	}
	
	@Override
	public String getCreateDate() {
		return DateUtil.dateTimeToString(page.getCreateDate());
	}

	@Override
	public String getModUserEmail() {
		return page.getModUserEmail();
	}
	
	@Override
	public String getModDate() {
		return DateUtil.dateTimeToString(page.getModDate());
	}
	
}
