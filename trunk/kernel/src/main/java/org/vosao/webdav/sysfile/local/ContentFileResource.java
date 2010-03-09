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

package org.vosao.webdav.sysfile.local;

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
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.DateUtil;
import org.vosao.webdav.AbstractFileResource;

import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;

public class ContentFileResource extends AbstractFileResource {

	private List<PageEntity> pages;
	
	public ContentFileResource(Business aBusiness, List<PageEntity> aPages) {
		super(aBusiness, "_content.xml", new Date());
		setContentType("text/xml");
		setData(new byte[0]);
		pages = aPages;
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
		Element e = doc.addElement("page");
		PageEntity page = pages.get(0);
		createPageDetailsXML(page, e);
		createPageVersionXML(page, e);
		setData(doc.asXML().getBytes("UTF-8"));
	}

	private void createPageVersionXML(PageEntity page, Element pageElement) {
		List<PageEntity> versions = getDao().getPageDao().selectByUrl(
				page.getFriendlyURL());
		for (PageEntity pageVersion : versions) {
			if (!pageVersion.getId().equals(page.getId())) {
				createPageDetailsXML(pageVersion, pageElement.addElement(
						"page-version"));
			}
		}
	}

	private static String packTitle(PageEntity page) {
		StringBuffer b = new StringBuffer("<title>");
		b.append(page.getTitleValue()).append("</title>");
		return b.toString();
	}

	private static String unpackTitle(String xml) {
		if (!xml.startsWith("<title>")) {
			return "en" + xml;
		}
		return xml.replace("<title>", "").replace("</title>", "");
	}
	
	private void createPageDetailsXML(PageEntity page, Element pageElement) {
		pageElement.addAttribute("url", page.getFriendlyURL());
		pageElement.addAttribute("title", packTitle(page));
		pageElement.addAttribute("commentsEnabled", String.valueOf(
				page.isCommentsEnabled()));
		if (page.getPublishDate() != null) {
			pageElement.addAttribute("publishDate", 
				DateUtil.toString(page.getPublishDate()));
		}
		TemplateEntity template = getDao().getTemplateDao().getById(
				page.getTemplate());
		if (template != null) {
			pageElement.addAttribute("theme", template.getUrl());
		}
		pageElement.addElement("version").setText(page.getVersion().toString());
		pageElement.addElement("versionTitle").setText(page.getVersionTitle());
		pageElement.addElement("state").setText(page.getState().name());
		pageElement.addElement("createUserEmail").setText(
				page.getCreateUserEmail());
		pageElement.addElement("modUserId").setText(
				page.getModUserEmail());
		if (page.getCreateDate() != null) {
			pageElement.addElement("createDate").setText(
					DateUtil.dateTimeToString(page.getCreateDate()));
		}
		if (page.getModDate() != null) {
			pageElement.addElement("modDate").setText(
					DateUtil.dateTimeToString(page.getModDate()));
		}
		StructureEntity structure = getDao().getStructureDao().getById(
				page.getStructureId());
		pageElement.addElement("structure").setText(
				structure == null ? "" : structure.getTitle());
		StructureTemplateEntity structureTemplate = getDao()
				.getStructureTemplateDao().getById(page.getStructureTemplateId());
		pageElement.addElement("structureTemplate").setText(
				structureTemplate == null ? ""	: structureTemplate.getTitle());
		pageElement.addElement("pageType").setText(page.getPageType().name());
		pageElement.addElement("sortIndex").setText(
				page.getSortIndex() == null ? "0" : page.getSortIndex().toString());
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				page.getId()); 
		for (ContentEntity content : contents) {
			Element contentElement = pageElement.addElement("content");
			contentElement.addAttribute("language", content.getLanguageCode());
			contentElement.addText(content.getContent());
		}
	}

}
