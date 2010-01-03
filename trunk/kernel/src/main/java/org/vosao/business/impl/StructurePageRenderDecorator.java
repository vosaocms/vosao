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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.PageBusiness;
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
	
	public PageEntity getPage() {
		return page;
	}
	
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	public String getId() {
		return page.getId();
	}

	public String getContent() {
		return content;
	}
	
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
			content = getPageBusiness().getPageContent(page, languageCode)
					.getContent();
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
	
	public String getTitle() {
		return page.getTitle();
	}

	public String getFriendlyUrl() {
		return page.getFriendlyURL();
	}

	public String getParentUrl() {
		return page.getParentUrl();
	}
	
	public String getTemplate() {
		return page.getTemplate();
	}
	
	public Date getPublishDate() {
		return page.getPublishDate();
	}
	
	public String getPublishDateString() {
		return DateUtil.toString(page.getPublishDate());
	}

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
	
	public Integer getVersion() {
		return page.getVersion();
	}

	public String getVersionTitle() {
		return page.getVersionTitle();
	}

	public String getState() {
		return page.getState().name();
	}

	public String getCreateUserEmail() {
		return page.getCreateUserEmail();
	}
	
	public String getCreateDate() {
		return DateUtil.dateTimeToString(page.getCreateDate());
	}

	public String getModUserEmail() {
		return page.getModUserEmail();
	}
	
	public String getModDate() {
		return DateUtil.dateTimeToString(page.getModDate());
	}
	
}
