package org.vosao.business.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.FolderBusiness;
import org.vosao.business.ImportExportBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FormEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;
import org.vosao.servlet.FolderUtil;
import org.vosao.servlet.MimeType;

public class ImportExportBusinessImpl extends AbstractBusinessImpl 
	implements ImportExportBusiness {

	private static final String THEME_FOLDER = "theme/";
	
	private static final Log logger = LogFactory.getLog(ImportExportBusinessImpl.class);
	
	private FolderBusiness folderBusiness;
	private PageBusiness pageBusiness;
	private ConfigBusiness configBusiness;
	
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}
	
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
	}
	
	public void setPageBusiness(PageBusiness bean) {
		pageBusiness = bean;
	}
	
	public PageBusiness getPageBusiness() {
		return pageBusiness;
	}

	@Override
	public byte[] createExportFile(final List<TemplateEntity> list) 
			throws IOException {
		
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(THEME_FOLDER));
		for (TemplateEntity theme : list) {
			exportTheme(out, theme);
		}
		out.close();
		return outData.toByteArray();
	}
	
	private void exportTheme(final ZipOutputStream out, 
			final TemplateEntity theme) throws IOException {

		String themeFolder = getThemePath(theme);
		addThemeResources(out, theme);		
		String descriptionName = themeFolder + "description.xml";
		out.putNextEntry(new ZipEntry(descriptionName));
		out.write(createThemeExportXML(theme).getBytes("UTF-8"));
		out.closeEntry();
		String contentName = themeFolder + "content.html";
		out.putNextEntry(new ZipEntry(contentName));
		out.write(theme.getContent().getBytes("UTF-8"));
		out.closeEntry();
	}

	private static String getThemePath(final TemplateEntity theme) {
		return THEME_FOLDER + theme.getUrl() + "/";
	}
	
	private String createThemeExportXML(final TemplateEntity theme) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("theme");
		root.addElement("title").addText(theme.getTitle());
		root.addElement("url").addText(theme.getUrl());
		return doc.asXML();
	}
	
	private void addThemeResources(final ZipOutputStream out, 
			final TemplateEntity theme) throws IOException {
		
		TreeItemDecorator<FolderEntity> root = getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> themeFolder = getFolderBusiness()
				.findFolderByPath(root, getThemePath(theme));
		if (themeFolder == null) {
			return;
		}
		addResourcesFromFolder(out, themeFolder, getThemePath(theme)); 
	}

	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param path - folder path under which resources will be placed to zip. 
	 * 	             Should end with / symbol.
	 * @throws IOException
	 */
	private void addResourcesFromFolder(final ZipOutputStream out, 
			final TreeItemDecorator<FolderEntity> folder, final String path) 
		throws IOException {
		
		out.putNextEntry(new ZipEntry(path));
		out.closeEntry();
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			addResourcesFromFolder(out, child, 
					path + child.getEntity().getName() + "/");
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(
				folder.getEntity().getId());
		for (FileEntity file : files) {
			String filePath = path + file.getFilename();
			out.putNextEntry(new ZipEntry(filePath));
			out.write(getDao().getFileDao().getFileContent(file));
			out.closeEntry();
		}
	}

	public List<String> importZip(ZipInputStream in) throws IOException, 
			DocumentException {
		List<String> result = new ArrayList<String>();
		ZipEntry entry;
		byte[] buffer = new byte[4096];
		while((entry = in.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				getFolderBusiness().createFolder("/" + entry.getName());
			}
			else {
				ByteArrayOutputStream data = new ByteArrayOutputStream();
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					data.write(buffer, 0, len);
				}
				in.closeEntry(); 
				if (isSiteContent(entry)) {
					createSiteContent(entry, data.toString("UTF-8"));
				}
				if (isThemeDescription(entry)) {
					createThemeByDescription(entry, data.toString("UTF-8"));
				}
				else if (isThemeContent(entry)) {
					createThemeByContent(entry, data.toString("UTF-8"));
				}
				else {
					result.add(importResourceFile(entry, data.toByteArray()));
				}
			}
		}
		return result;
	}
	
	private boolean isThemeDescription(final ZipEntry entry) 
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
		for (Iterator<Element> i = root.elementIterator(); i.hasNext(); ) {
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
	
	private void createThemeByDescription(final ZipEntry entry, String xml) 
			throws UnsupportedEncodingException, DocumentException {
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
		getDao().getTemplateDao().save(theme);
	}

	private boolean isThemeContent(final ZipEntry entry) 
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length < 3 || !chain[0].equals("theme")
				|| !chain[2].equals("content.html")) {
			return false;
		}
		return true;
	}

	private void createThemeByContent(final ZipEntry entry, 
			final String content) 
			throws UnsupportedEncodingException, DocumentException {
		String[] chain = FolderUtil.getPathChain(entry);
		String themeName = chain[1];
		TemplateEntity theme = getDao().getTemplateDao().getByUrl(themeName);
		if (theme == null) {
			theme = new TemplateEntity();
			theme.setTitle(themeName);
			theme.setUrl(themeName);
		}
		theme.setContent(content);
		getDao().getTemplateDao().save(theme);
	}
	
	private String importResourceFile(final ZipEntry entry, byte[] data) 
			throws UnsupportedEncodingException {
		
		String[] chain = FolderUtil.getPathChain(entry);
		String folderPath = FolderUtil.getFilePath(entry);
		String fileName = chain[chain.length - 1];
		logger.debug("importResourceFile: " + folderPath + " " + fileName);
		getFolderBusiness().createFolder(folderPath);
		TreeItemDecorator<FolderEntity> root = getFolderBusiness().getTree();
		FolderEntity folderEntity = getFolderBusiness().findFolderByPath(root, 
				folderPath).getEntity(); 
		String contentType = MimeType.getContentTypeByExt(
				FolderUtil.getFileExt(fileName));
		FileEntity fileEntity = getDao().getFileDao().getByName(
				folderEntity.getId(), fileName);
		if (fileEntity != null) {
			fileEntity.setMdtime(new Date());
			fileEntity.setSize(data.length);
			getDao().getFileDao().save(fileEntity);
			getDao().getFileDao().saveFileContent(fileEntity, data);
		}
		else {
			fileEntity = new FileEntity(fileName, fileName, folderEntity.getId(),
					contentType, new Date(), data.length);
			getDao().getFileDao().save(fileEntity);
			
		}
		return "/" + entry.getName();
	}

	@Override
	public byte[] createExportFile() throws IOException {
		ByteArrayOutputStream outData = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(outData);
		out.putNextEntry(new ZipEntry(THEME_FOLDER));
		List<TemplateEntity> list = getDao().getTemplateDao().select();
		for (TemplateEntity theme : list) {
			exportTheme(out, theme);
		}
		exportContent(out);
		out.close();
		return outData.toByteArray();
	}
	
	private void exportContent(final ZipOutputStream out) throws IOException {
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
		createConfigXML(config);
		Element pages = root.addElement("pages");
		TreeItemDecorator<PageEntity> pageRoot = getPageBusiness().getTree();
		createPageXML(pageRoot, pages);
		Element forms = root.addElement("forms");
		createFormsXML(forms);
		return doc.asXML();
	}
	
	private void createConfigXML(Element config) {
		Element googleAnalytics = config.addElement("google-analytics");
		googleAnalytics.setText(getConfigBusiness().getGoogleAnalyticsId());
		Element siteEmail = config.addElement("email");
		siteEmail.setText(getConfigBusiness().getSiteEmail());
		Element siteDomain = config.addElement("domain");
		siteDomain.setText(getConfigBusiness().getSiteDomain());
	}

	private void createPageXML(TreeItemDecorator<PageEntity> page,
			Element root) {
		Element pageElement = root.addElement("page"); 
		pageElement.addAttribute("url", page.getEntity().getFriendlyURL());
		pageElement.addAttribute("title", page.getEntity().getTitle());
		if (page.getEntity().getPublishDate() != null) {
			pageElement.addAttribute("publishDate", 
				page.getEntity().getPublishDate().toString());
		}
		TemplateEntity template = getDao().getTemplateDao().getById(
				page.getEntity().getTemplate());
		if (template != null) {
			pageElement.addAttribute("theme", template.getUrl());
		}
		Element contentElement = pageElement.addElement("content");
		String contentEncoded = page.getEntity().getContent();
		contentElement.addText(contentEncoded);
		for (TreeItemDecorator<PageEntity> child : page.getChildren()) {
			createPageXML(child, pageElement);
		}
	}

	private void createFormsXML(Element formsElement) {
		List<FormEntity> list = getDao().getFormDao().select();
		for (FormEntity form : list) {
			createFormXML(formsElement, form);
		}
	}

	private void createFormXML(Element formsElement, final FormEntity form) {
		Element formElement = formsElement.addElement("form");
		formElement.addAttribute("name", form.getName());
		formElement.addAttribute("title", form.getTitle());
		formElement.addAttribute("email", form.getEmail());
		formElement.addAttribute("letterSubject", form.getLetterSubject());
	}
	
	private void addContentResources(final ZipOutputStream out) 
			throws IOException {
		
		TreeItemDecorator<FolderEntity> root = getFolderBusiness().getTree();
		TreeItemDecorator<FolderEntity> folder = getFolderBusiness()
				.findFolderByPath(root, "/page");
		if (folder == null) {
			return;
		}
		addResourcesFromFolder(out, folder, "/page/"); 
	}

	private boolean isSiteContent(final ZipEntry entry) 
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length != 1 || !chain[0].equals("content.xml")) {
			return false;
		}
		return true;
	}
	
	private void createSiteContent(final ZipEntry entry, final String xml) 
			throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		for (Iterator<Element> i = root.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("pages")) {
            	createPages(element);
            }
            if (element.getName().equals("config")) {
            	createConfigs(element);
            }
            if (element.getName().equals("forms")) {
            	createForms(element);
            }
        }
	}
	
	private void createPages(Element pages) {
		for (Iterator<Element> i = pages.elementIterator(); i.hasNext(); ) {
            Element pageElement = i.next();
            createPage(pageElement, null);
		}
	}
		
	private void createPage(Element pageElement, PageEntity parentPage) {
		String parentId = null;
		if (parentPage != null) {
			parentId = parentPage.getId();
		}
		String title = pageElement.attributeValue("title");
		String url = pageElement.attributeValue("url");
		String themeUrl = pageElement.attributeValue("theme");
		Date publishDate = new Date();
		if (pageElement.attributeValue("theme") != null) {
			try {
				publishDate = DateFormat.getInstance().parse(
					pageElement.attributeValue("theme"));
			} catch (ParseException e) {
				logger.error("Wrong date format" + pageElement.attributeValue("theme"));
			}
		}
		TemplateEntity template = getDao().getTemplateDao().getByUrl(themeUrl);
		String templateId = null;
		if (template != null) {
			templateId = template.getId();
		}
		String content = "";
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("content")) {
            	content = element.getText();
            	break;
            }
		}
		PageEntity newPage = new PageEntity(title, content, url, parentId, 
				templateId, publishDate);
		PageEntity page = getDao().getPageDao().getByUrl(url);
		if (page != null) {
			page.copy(newPage);
		}
		else {
			page = newPage;
		}
		getDao().getPageDao().save(page);
		for (Iterator<Element> i = pageElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("page")) {
            	createPage(element, page);
            }
		}
	}

	private void createConfigs(Element configElement) {
		for (Iterator<Element> i = configElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("google-analytics")) {
            	getConfigBusiness().setGoogleAnalyticsId(element.getText());
            }
            if (element.getName().equals("email")) {
            	getConfigBusiness().setSiteEmail(element.getText());
            }
            if (element.getName().equals("domain")) {
            	getConfigBusiness().setSiteDomain(element.getText());
            }
		}
	}

	private void createForms(Element formsElement) {
		for (Iterator<Element> i = formsElement.elementIterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("form")) {
            	String name = element.attributeValue("name");
            	String title = element.attributeValue("title");
            	String email = element.attributeValue("email");
            	String letterSubject = element.attributeValue("letterSubject");
            	FormEntity form = new FormEntity(name, email, title, 
            			letterSubject);
            	getDao().getFormDao().save(form);
            }
		}		
	}

	@Override
	public ConfigBusiness getConfigBusiness() {
		return configBusiness;
	}

	@Override
	public void setConfigBusiness(ConfigBusiness bean) {
		configBusiness = bean;
	}
	
}
