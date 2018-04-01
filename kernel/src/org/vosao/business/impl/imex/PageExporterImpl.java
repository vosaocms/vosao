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

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;
import org.vosao.business.imex.PageExporter;
import org.vosao.business.imex.PagePermissionExporter;
import org.vosao.business.imex.ResourceExporter;
import org.vosao.common.VosaoContext;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PageTagEntity;
import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.entity.TagEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.enums.PageState;
import org.vosao.enums.PageType;
import org.vosao.utils.DateUtil;
import org.vosao.utils.StrUtil;
import org.vosao.utils.XmlUtil;

public class PageExporterImpl extends AbstractExporter 
		implements PageExporter {

	public PageExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	public String createPageContentXML(PageEntity page) {
		Document doc = DocumentHelper.createDocument(); 
		Element pageElement = doc.addElement("page"); 
		createPageDetailsXML(page, pageElement);
		createPageVersionXML(page, pageElement);
		return doc.asXML();
	}
	
	public String createPageCommentsXML(String pageURL) {
		Document doc = DocumentHelper.createDocument(); 
		Element rootElement = doc.addElement("comments"); 
		createCommentsXML(pageURL, rootElement);
		return doc.asXML();
	}

	public String createPagePermissionsXML(String pageURL) {
		Document doc = DocumentHelper.createDocument(); 
		Element e = doc.addElement("permissions"); 
		getPagePermissionExporter().createPagePermissionsXML(e, 
				pageURL);
		return doc.asXML();
	}

	public String createPageTagXML(String pageURL) {
		Document doc = DocumentHelper.createDocument(); 
		Element e = doc.addElement("tags"); 
		PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
		if (pageTag != null) {
			List<TagEntity> tags = getDao().getTagDao().getById(
					pageTag.getTags());
			for (TagEntity tag : tags) {
				Element tagElement = e.addElement("tag");
				tagElement.addElement("name").setText(
						getBusiness().getTagBusiness().getPath(tag));
			}
		}
		return doc.asXML();
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
		return page.getTitleValue();
	}

	private static String unpackTitle(String data) {
		if (data.startsWith("{")) {
			return data;
		}
		if (data.startsWith("<title>")) {
			String old = data.replace("<title>", "").replace("</title>", "");
			return (new JSONObject(StrUtil.unpack06Title(old))).toString();
		}
		return "{en:'" + data + "'}";
	}
	
	private void createPageDetailsXML(PageEntity page, Element pageElement) {
		pageElement.addAttribute("url", page.getFriendlyURL());
		pageElement.addAttribute("title", packTitle(page));
		pageElement.addAttribute("commentsEnabled", String.valueOf(
				page.isCommentsEnabled()));
		if (page.getPublishDate() != null) {
			pageElement.addAttribute("publishDate", 
				DateUtil.dateTimeToString(page.getPublishDate()));
		}
		if (page.getEndPublishDate() != null) {
			pageElement.addAttribute("endPublishDate", 
				DateUtil.dateTimeToString(page.getEndPublishDate()));
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
		pageElement.addElement("modUserEmail").setText(
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
				structureTemplate == null ? ""	: structureTemplate.getName());
		pageElement.addElement("pageType").setText(page.getPageType().name());
		pageElement.addElement("sortIndex").setText(
				page.getSortIndex() == null ? "0" : page.getSortIndex().toString());
		pageElement.addElement("keywords").setText(XmlUtil.notNull(
				page.getKeywords()));
		pageElement.addElement("description").setText(XmlUtil.notNull(
				page.getDescription()));
		pageElement.addElement("headHtml").setText(XmlUtil.notNull(
				page.getHeadHtml()));
		pageElement.addElement("searchable").setText(String.valueOf(
				page.isSearchable()));
		pageElement.addElement("velocityProcessing").setText(String.valueOf(
				page.isVelocityProcessing()));
		pageElement.addElement("skipPostProcessing").setText(String.valueOf(
				page.isSkipPostProcessing()));
		pageElement.addElement("cached").setText(String.valueOf(
				page.isCached()));
		pageElement.addElement("contentType").setText(XmlUtil.notNull(
				page.getContentType()));
		pageElement.addElement("wikiProcessing").setText(String.valueOf(
				page.isWikiProcessing()));
		pageElement.addElement("enableCkeditor").setText(String.valueOf(
				page.isEnableCkeditor()));
		pageElement.addElement("attributes").setText(XmlUtil.notNull(
				page.getAttributes()));
		pageElement.addElement("restful").setText(String.valueOf(
				page.isRestful()));
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				page.getId()); 
		for (ContentEntity content : contents) {
			Element contentElement = pageElement.addElement("content");
			contentElement.addAttribute("language", content.getLanguageCode());
			contentElement.addText(content.getContent());
		}
	}
	
	private void createCommentsXML(String pageURL, Element commentsElement) {
		List<CommentEntity> comments = getDao().getCommentDao().getByPage(
				pageURL);
		for (CommentEntity comment : comments) {
			Element commentElement = commentsElement.addElement("comment");
			commentElement.addAttribute("name", comment.getName());
			commentElement.addAttribute("disabled", String.valueOf(
					comment.isDisabled()));
			commentElement.addAttribute("publishDate", 
				DateUtil.dateTimeToString(comment.getPublishDate()));
			commentElement.setText(comment.getContent());
		}
	}

	public void readPages(Element pages) throws DaoTaskException {
		for (Iterator<Element> i = pages.elementIterator(); i.hasNext(); ) {
			Element pageElement = i.next();
			readPage(pageElement);
		}
	}

	private void readPage(Element pageElement) 
			throws DaoTaskException {
		PageEntity page = readPageVersion(pageElement);
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("page")) {
				readPage(element);
			}
			if (element.getName().equals("comments")) {
				readComments(element, page.getFriendlyURL());
			}
			if (element.getName().equals("page-version")) {
				readPageVersion(element);
			}
			if (element.getName().equals("permissions")) {
				getPagePermissionExporter().readPagePermissions(element, 
						page.getFriendlyURL());
			}
		}
	}

	private PageEntity readPageVersion(Element pageElement) 
			throws DaoTaskException {
		String title = unpackTitle(pageElement.attributeValue("title"));
		String url = pageElement.attributeValue("url");
		String themeUrl = pageElement.attributeValue("theme");
		String commentsEnabled = pageElement.attributeValue("commentsEnabled");
		Date publishDate = new Date();
		Date endPublishDate = null;
		if (pageElement.attributeValue("publishDate") != null) {
			try {
				publishDate = DateUtil.dateTimeToDate(pageElement
						.attributeValue("publishDate"));
			} catch (ParseException e) {
				try {
					publishDate = DateUtil.toDate(pageElement
						.attributeValue("publishDate"));
				}
				catch (ParseException e2) {
					logger.error("Wrong date format "
							+ pageElement.attributeValue("publishDate") + " "
							+ title);
				}
			}
		}
		if (pageElement.attributeValue("endPublishDate") != null) {
			try {
				endPublishDate = DateUtil.dateTimeToDate(pageElement
						.attributeValue("endPublishDate"));
			} catch (ParseException e) {
				logger.error("Wrong date format "
						+ pageElement.attributeValue("endPublishDate") + " "
						+ title);
			}
		}
		TemplateEntity template = getDao().getTemplateDao().getByUrl(themeUrl);
		Long templateId = null;
		if (template != null) {
			templateId = template.getId();
		}
		PageEntity newPage = new PageEntity();
		newPage.setTitleValue(title);
		newPage.setFriendlyURL(url);
		newPage.setTemplate(templateId);
		newPage.setPublishDate(publishDate);
		newPage.setEndPublishDate(endPublishDate);
		if (commentsEnabled != null) {
			newPage.setCommentsEnabled(Boolean.valueOf(commentsEnabled));
		}
		newPage.setState(PageState.APPROVED);
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("version")) {
				newPage.setVersion(XmlUtil.readIntegerText(element, 1));
			}
			if (element.getName().equals("versionTitle")) {
				newPage.setVersionTitle(element.getText());
			}
			if (element.getName().equals("state")) {
				newPage.setState(PageState.valueOf(element.getText()));
			}
			if (element.getName().equals("createUserEmail")) {
				newPage.setCreateUserEmail(element.getText());
			}
			if (element.getName().equals("modUserEmail")) {
				newPage.setModUserEmail(element.getText());
			}
			if (element.getName().equals("pageType")) {
				newPage.setPageType(PageType.valueOf(element.getText()));
			}
			if (element.getName().equals("sortIndex")) {
				newPage.setSortIndex(XmlUtil.readIntegerText(element, 0));
			}
			if (element.getName().equals("structure")) {
				StructureEntity structure = getDao().getStructureDao().getByTitle(
						element.getText());
				newPage.setStructureId(structure == null ? null : structure.getId());
			}
			if (element.getName().equals("structureTemplate")) {
				StructureTemplateEntity structureTemplate = getDao()
						.getStructureTemplateDao().getByName(element.getText());
				if (structureTemplate == null) {
					structureTemplate = getDao()
						.getStructureTemplateDao().getByTitle(element.getText());
				}
				newPage.setStructureTemplateId(structureTemplate == null ? null : 
						structureTemplate.getId());
			}
			if (element.getName().equals("createDate")) {
				try {
					newPage.setCreateDate(DateUtil.dateTimeToDate(
							element.getText()));
				} catch (ParseException e) {
					logger.error("Wrong date format for createDate " 
							+ element.getText());
				}
			}
			if (element.getName().equals("modDate")) {
				try {
					newPage.setModDate(DateUtil.dateTimeToDate(
							element.getText()));
				} catch (ParseException e) {
					logger.error("Wrong date format for createDate " 
							+ element.getText());
				}
			}
			if (element.getName().equals("keywords")) {
				newPage.setKeywords(element.getText());
			}
			if (element.getName().equals("description")) {
				newPage.setDescription(element.getText());
			}
			if (element.getName().equals("searchable")) {
				newPage.setSearchable(XmlUtil.readBooleanText(element, true));
			}
			if (element.getName().equals("velocityProcessing")) {
				newPage.setVelocityProcessing(XmlUtil.readBooleanText(element, 
						false));
			}
			if (element.getName().equals("headHtml")) {
				newPage.setHeadHtml(element.getText());
			}
			if (element.getName().equals("skipPostProcessing")) {
				newPage.setSkipPostProcessing(XmlUtil.readBooleanText(element, 
						false));
			}
			if (element.getName().equals("cached")) {
				newPage.setCached(XmlUtil.readBooleanText(element, true));
			}
			if (element.getName().equals("contentType")) {
				newPage.setContentType(element.getText());
			}
			if (element.getName().equals("wikiProcessing")) {
				newPage.setWikiProcessing(XmlUtil.readBooleanText(element, 
						false));
			}
			if (element.getName().equals("enableCkeditor")) {
				newPage.setEnableCkeditor(XmlUtil.readBooleanText(element, 
						true));
			}
			if (element.getName().equals("attributes")) {
				newPage.setAttributes(element.getText());
			}
			if (element.getName().equals("restful")) {
				newPage.setRestful(XmlUtil.readBooleanText(element, 
						true));
			}
		}
		PageEntity page = getDao().getPageDao().getByUrlVersion(url, 
				newPage.getVersion());
		if (page != null) {
			page.copy(newPage);
		} else {
			page = newPage;
		}
		getDaoTaskAdapter().pageSave(page);
		readContents(pageElement, page);
		return page;
	}
	
	private void readContents(Element pageElement, PageEntity page) 
			throws DaoTaskException {
		ConfigEntity config = VosaoContext.getInstance().getConfig();
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("content")) {
				String content = element.getText();
				String language = element.attributeValue("language");
				if (language == null) {
					language = config.getDefaultLanguage();
				}
				getDaoTaskAdapter().setPageContent(page.getId(), language, 
						content);
			}
		}
	}
	
	private void readComments(Element commentsElement, String url) 
			throws DaoTaskException {
		for (Iterator<Element> i = commentsElement.elementIterator(); i
				.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("comment")) {
				String name = element.attributeValue("name");
				Date publishDate = new Date();
				try {
					publishDate = DateUtil.dateTimeToDate(element
							.attributeValue("publishDate"));
				} catch (ParseException e) {
					logger.error("Error parsing comment publish date "
							+ element.attributeValue("publishDate"));
				}
				boolean disabled = Boolean.valueOf(element
						.attributeValue("disabled"));
				String content = element.getText();
				CommentEntity comment = new CommentEntity(name, content,
						publishDate, url, disabled);
				getDaoTaskAdapter().commentSave(comment);
			}
		}
	}

	private ResourceExporter getResourceExporter() {
		return getExporterFactory().getResourceExporter();
	}

	private PagePermissionExporter getPagePermissionExporter() {
		return getExporterFactory().getPagePermissionExporter();
	}
	
	/**
	 * Read and import data from _content.xml file.
	 * @param folderPath - _content.xml file path.
	 * @param xml - _content.xml file content.
	 * @return
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	public boolean readContentFile(String folderPath, String xml) 
			throws DocumentException, DaoTaskException {
		String pageURL = getPageURL(folderPath);
		if (pageURL == null) {
			return false;
		}
		Document doc = DocumentHelper.parseText(xml);
		readPage(doc.getRootElement());
		return true;
	}
	
	/**
	 * Read and import data from _comments.xml file.
	 * @param folderPath - _comments.xml file path.
	 * @param xml - _comments.xml file content.
	 * @return
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	public boolean readCommentsFile(String folderPath, String xml) 
			throws DocumentException, DaoTaskException {
		String pageURL = getPageURL(folderPath);
		if (pageURL == null) {
			return false;
		}
		Document doc = DocumentHelper.parseText(xml);
		readComments(doc.getRootElement(), pageURL);
		return true;
	}
	
	/**
	 * Read and import data from _permissions.xml file.
	 * @param folderPath - _permissions.xml file path.
	 * @param xml - _permissions.xml file content.
	 * @return
	 * @throws DocumentException 
	 */
	public boolean readPermissionsFile(String folderPath, String xml) 
			throws DocumentException {
		String pageURL = getPageURL(folderPath);
		if (pageURL == null) {
			return false;
		}
		Document doc = DocumentHelper.parseText(xml);
		getPagePermissionExporter().readPagePermissions(doc.getRootElement(), 
				pageURL);
		return true;
	}
	
	private String getPageURL(String folderPath) {
		if (folderPath.equals("/page")) {
			return "/";
		}
		if (folderPath.startsWith("/page")) {
			return folderPath.replace("/page", "");
		}
		return null;
	}

	@Override
	public boolean readPageTagFile(String folderPath, String xml)
			throws DocumentException, DaoTaskException {
		String pageURL = getPageURL(folderPath);
		if (pageURL == null) {
			return false;
		}
		Document doc = DocumentHelper.parseText(xml);
		readTags(doc.getRootElement(), pageURL);
		return true;
	}
	
	private void readTags(Element tagsElement, String pageURL) 
			throws DaoTaskException {
		for (Iterator<Element> i = tagsElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("tag")) {
				String path = element.elementText("name");
				TagEntity tag = getBusiness().getTagBusiness().getByPath(path);
				if(tag != null) {
					PageTagEntity pageTag = getDao().getPageTagDao().getByURL(pageURL);
					if (pageTag == null) {
						pageTag = new PageTagEntity(pageURL);
					}
					if (!pageTag.getTags().contains(tag.getId())) {
						pageTag.getTags().add(tag.getId());
					}
					getDaoTaskAdapter().pageTagSave(pageTag);
					if (!tag.getPages().contains(pageURL)) {
						tag.getPages().add(pageURL);
						getDaoTaskAdapter().tagSave(tag);
					}
				}
				else {
					logger.error("Tag not found " + path);
				}			
			}
		}
	}
	
}
