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

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.Dao;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.DateUtil;

public class PageExporter extends AbstractExporter {

	private static final Log logger = LogFactory.getLog(PageExporter.class);

	private ResourceExporter resourceExporter;
	private ConfigExporter configExporter;
	private FormExporter formExporter;
	
	public PageExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
		resourceExporter = new ResourceExporter(aDao, aBusiness);
		configExporter = new ConfigExporter(aDao, aBusiness);
		formExporter = new FormExporter(aDao, aBusiness);
	}
	
	private void createPageXML(TreeItemDecorator<PageEntity> page,
			Element root) {
		Element pageElement = root.addElement("page"); 
		pageElement.addAttribute("url", page.getEntity().getFriendlyURL());
		pageElement.addAttribute("title", page.getEntity().getTitle());
		pageElement.addAttribute("commentsEnabled", String.valueOf(
				page.getEntity().isCommentsEnabled()));
		if (page.getEntity().getPublishDate() != null) {
			pageElement.addAttribute("publishDate", 
				DateUtil.toString(page.getEntity().getPublishDate()));
		}
		TemplateEntity template = getDao().getTemplateDao().getById(
				page.getEntity().getTemplate());
		if (template != null) {
			pageElement.addAttribute("theme", template.getUrl());
		}
		List<ContentEntity> contents = getDao().getPageDao().getContents(
				page.getEntity().getId()); 
		for (ContentEntity content : contents) {
			Element contentElement = pageElement.addElement("content");
			contentElement.addAttribute("language", content.getLanguageCode());
			contentElement.addText(content.getContent());
		}
		createCommentsXML(page, pageElement);
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			createPageXML(child, pageElement);
		}
	}
	
	private void createCommentsXML(TreeItemDecorator<PageEntity> page, 
			Element pageElement) {
		Element commentsElement = pageElement.addElement("comments");
		List<CommentEntity> comments = getDao().getCommentDao().getByPage(
				page.getEntity().getFriendlyURL());
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

	public void exportContent(final ZipOutputStream out) throws IOException {
		String contentName = "content.xml";
		out.putNextEntry(new ZipEntry(contentName));
		out.write(createContentExportXML().getBytes("UTF-8"));
		out.closeEntry();
		addContentResources(out);
	}

	private String createContentExportXML() {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("site");
		Element config = root.addElement("config");
		configExporter.createConfigXML(config);
		Element pages = root.addElement("pages");
		TreeItemDecorator<PageEntity> pageRoot = getBusiness()
				.getPageBusiness().getTree();
		createPageXML(pageRoot, pages);
		Element forms = root.addElement("forms");
		formExporter.createFormsXML(forms);
		return doc.asXML();
	}
	
	private void addContentResources(final ZipOutputStream out)
			throws IOException {
		TreeItemDecorator<FolderEntity> root = getBusiness()
				.getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> folder = getBusiness()
				.getFolderBusiness().findFolderByPath(root, "/page");
		if (folder == null) {
			return;
		}
		resourceExporter.addResourcesFromFolder(out, folder, "page/");
	}

	public void readPages(Element pages) {
		for (Iterator<Element> i = pages.elementIterator(); i.hasNext(); ) {
			Element pageElement = i.next();
			readPage(pageElement, null);
		}
	}

	private void readPage(Element pageElement, PageEntity parentPage) {
		String title = pageElement.attributeValue("title");
		String url = pageElement.attributeValue("url");
		String themeUrl = pageElement.attributeValue("theme");
		String commentsEnabled = pageElement.attributeValue("commentsEnabled");
		Date publishDate = new Date();
		if (pageElement.attributeValue("publishDate") != null) {
			try {
				publishDate = DateUtil.toDate(pageElement
						.attributeValue("publishDate"));
			} catch (ParseException e) {
				logger.error("Wrong date format "
						+ pageElement.attributeValue("publishDate") + " "
						+ title);
			}
		}
		TemplateEntity template = getDao().getTemplateDao().getByUrl(themeUrl);
		String templateId = null;
		if (template != null) {
			templateId = template.getId();
		}
		PageEntity newPage = new PageEntity(title, url,	templateId, publishDate);
		if (commentsEnabled != null) {
			newPage.setCommentsEnabled(Boolean.valueOf(commentsEnabled));
		}
		PageEntity page = getDao().getPageDao().getByUrl(url);
		if (page != null) {
			page.copy(newPage);
		} else {
			page = newPage;
		}
		getDao().getPageDao().save(page);
		readContents(pageElement, page);
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("page")) {
				readPage(element, page);
			}
			if (element.getName().equals("comments")) {
				readComments(element, page);
			}
		}
	}

	private void readContents(Element pageElement, PageEntity page) {
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("content")) {
				String content = element.getText();
				String language = element.attributeValue("language");
				if (language == null) {
					language = LanguageEntity.ENGLISH_CODE;
				}
				getDao().getPageDao().setContent(page.getId(), language, 
						content);
			}
		}
	}
	
	private void readComments(Element commentsElement, PageEntity page) {
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
						publishDate, page.getId(), disabled);
				getDao().getCommentDao().save(comment);
			}
		}
	}
	
}
