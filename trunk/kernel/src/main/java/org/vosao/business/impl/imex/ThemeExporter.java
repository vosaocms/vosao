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
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.utils.FolderUtil;

public class ThemeExporter extends AbstractExporter {

	public static final String THEME_FOLDER = "theme/";

	public ThemeExporter(ExporterFactory factory) {
		super(factory);
	}
	
	public void exportThemes(final ZipOutputStream out, 
			final List<TemplateEntity> themes) throws IOException {
		FolderEntity themeFolder = getDao().getFolderDao().getByPath("/theme");
		if (themeFolder == null) {
			logger.error("Folder not found: /theme");
			return;
		}
		getResourceExporter().addFolder(out, themeFolder, "theme/");
		for (TemplateEntity theme : themes) {
			exportTheme(out, theme);
		}
	}
	
	public void exportTheme(final ZipOutputStream out, 
			final TemplateEntity theme) throws IOException {

		String themeFolder = getThemeZipPath(theme);
		addThemeResources(out, theme);		
		String descriptionName = themeFolder + "_template.xml";
		out.putNextEntry(new ZipEntry(descriptionName));
		out.write(createThemeExportXML(theme).getBytes("UTF-8"));
		out.closeEntry();
	}

	private static String getThemeZipPath(final TemplateEntity theme) {
		return THEME_FOLDER + theme.getUrl() + "/";
	}
	
	private static String getThemePath(final TemplateEntity theme) {
		return "/" + THEME_FOLDER + theme.getUrl();
	}

	private String createThemeExportXML(final TemplateEntity theme) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("template");
		root.addElement("title").addText(theme.getTitle());
		root.addElement("url").addText(theme.getUrl());
		root.addElement("content").addText(theme.getContent());
		return doc.asXML();
	}
	
	private void addThemeResources(final ZipOutputStream out, 
			final TemplateEntity theme) throws IOException {
		
		TreeItemDecorator<FolderEntity> root = getBusiness()
				.getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> themeFolder = getBusiness()
				.getFolderBusiness().findFolderByPath(root, getThemePath(theme));
		if (themeFolder == null) {
			return;
		}
		getResourceExporter().addResourcesFromFolder(out, themeFolder, 
				getThemeZipPath(theme)); 
	}
	
	public boolean isThemeDescription(final ZipEntry entry)
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length < 3 || !chain[0].equals("theme")
				|| !chain[2].equals("description.xml")) {
			return false;
		}
		return true;
	}	
	
	private TemplateEntity readThemeImportXML(final String xml)
			throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		TemplateEntity template = new TemplateEntity();
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("title")) {
				template.setTitle(element.getStringValue());
			}
			if (element.getName().equals("url")) {
				template.setUrl(element.getStringValue());
			}
		}
		return template;
	}	

	public void createThemeByDescription(final ZipEntry entry, String xml)
			throws UnsupportedEncodingException, DocumentException, 
			DaoTaskException {
		String[] chain = FolderUtil.getPathChain(entry);
		String themeName = chain[1];
		TemplateEntity theme = getDao().getTemplateDao().getByUrl(themeName);
		if (theme == null) {
			theme = new TemplateEntity();
			theme.setContent("");
		}
		String content = theme.getContent();
		TemplateEntity parsedEntity = readThemeImportXML(xml);
		theme.copy(parsedEntity);
		theme.setContent(content);
		getDaoTaskAdapter().templateSave(theme);
	}	
	
	public boolean isThemeContent(final ZipEntry entry)
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length < 3 || !chain[0].equals("theme")
			|| !chain[2].equals("content.html")) {
			return false;
		}
		return true;
	}
	
	
	public void createThemeByContent(final ZipEntry entry, 
			final String content) 
			throws UnsupportedEncodingException, DocumentException, 
			DaoTaskException {
		String[] chain = FolderUtil.getPathChain(entry);
		String themeName = chain[1];
		TemplateEntity theme = getDao().getTemplateDao().getByUrl(themeName);
		if (theme == null) {
			theme = new TemplateEntity();
			theme.setTitle(themeName);
			theme.setUrl(themeName);
		}
		theme.setContent(content);
		getDaoTaskAdapter().templateSave(theme);
	}

	public ResourceExporter getResourceExporter() {
		return getExporterFactory().getResourceExporter();
	}
	
	/**
	 * Read and import data from _template.xml file.
	 * @param path - path to _temlate.xml file.
	 * @param xml - _template.xml content.
	 * @return true if successfully imported.
	 * @throws DocumentException
	 * @throws DaoTaskException 
	 */
	public boolean readTemplateFile(String path, String xml) 
			throws DocumentException, DaoTaskException {
		String folderPath = FolderUtil.getFilePath(path);
		String templateName = getTemplateName(folderPath);
		if (templateName == null) {
			return false;
		}
		TemplateEntity template = getDao().getTemplateDao().getByUrl(
				templateName);
		if (template == null) {
			template = new TemplateEntity();
			template.setUrl(templateName);
		}
		Element root = DocumentHelper.parseText(xml).getRootElement();
		String title = root.elementText("title");
		if (!StringUtils.isEmpty(title)) {
			template.setTitle(title);
		}
		String content = root.elementText("content");
		if (!StringUtils.isEmpty(content)) {
			template.setContent(content);
		}
		getDaoTaskAdapter().templateSave(template);
		return true;
	}
	
	private String getTemplateName(String folderPath) {
		try {
			String[] chain = FolderUtil.getPathChain(folderPath);
			if (chain.length == 2 && chain[0].equals("theme")) {
				return chain[1];
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
