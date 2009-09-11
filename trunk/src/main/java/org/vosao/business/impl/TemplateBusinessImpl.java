package org.vosao.business.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.FolderBusiness;
import org.vosao.business.TemplateBusiness;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class TemplateBusinessImpl extends AbstractBusinessImpl 
	implements TemplateBusiness {

	private static final String THEME_FOLDER = "theme/";
	
	private FolderBusiness folderBusiness;
	
	public void setFolderBusiness(FolderBusiness bean) {
		folderBusiness = bean;
	}
	
	public FolderBusiness getFolderBusiness() {
		return folderBusiness;
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

		String themeFolder = getThemeFolder(theme);
		addThemeResources(out, theme);		
		String descriptionName = themeFolder + "description.xml";
		out.putNextEntry(new ZipEntry(descriptionName));
		out.write(createThemeExportXML(theme).getBytes());
		out.closeEntry();
		String contentName = themeFolder + "content.html";
		out.putNextEntry(new ZipEntry(contentName));
		out.write(theme.getContent().getBytes());
		out.closeEntry();
	}

	private static String getThemeFolder(final TemplateEntity theme) {
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
				.findFolderByPath(root, getThemeFolder(theme));
		if (themeFolder == null) {
			return;
		}
		addResourcesFromFolder(out, themeFolder, getThemeFolder(theme)); 
	}

	private void addResourcesFromFolder(final ZipOutputStream out, 
			final TreeItemDecorator<FolderEntity> folder, final String path) 
		throws IOException {
		
		out.putNextEntry(new ZipEntry(path));
		out.closeEntry();
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			addResourcesFromFolder(out, child, 
					path + child.getEntity().getName() + "/");
		}
		for (FileEntity file : folder.getEntity().getFiles()) {
			String filePath = path + file.getFile().getFilename();
			out.putNextEntry(new ZipEntry(filePath));
			out.write(file.getFile().getContent());
			out.closeEntry();
		}
	}

	public List<String> validateBeforeUpdate(final TemplateEntity template) {
		List<String> errors = new ArrayList<String>();
		if (template.getId() == null) {
			TemplateEntity myTemplate = getDao().getTemplateDao().getByUrl(
					template.getUrl());
			if (myTemplate != null) {
				errors.add("Template with such URL already exists");
			}
		}
		if (StringUtil.isEmpty(template.getUrl())) {
			errors.add("URL is empty");
		}
		if (StringUtil.isEmpty(template.getTitle())) {
			errors.add("Title is empty");
		}
		if (StringUtil.isEmpty(template.getContent())) {
			errors.add("Content is empty");
		}
		return errors;
	}

	
}
