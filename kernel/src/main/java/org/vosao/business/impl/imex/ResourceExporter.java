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

import static org.vosao.utils.XmlUtil.notNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.MimeType;

public class ResourceExporter extends AbstractExporter {

	public ResourceExporter(ExporterFactory factory) {
		super(factory);
	}
	
	/**
	 * Add folder and _folder.xml file to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder 
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	public void addFolder(final ZipOutputStream out, 
			final FolderEntity folder, final String zipPath) 
			throws IOException {
		if (zipPath.length() != 0) {
			out.putNextEntry(new ZipEntry(zipPath));
			out.closeEntry();
		}
		out.putNextEntry(new ZipEntry(zipPath + "_folder.xml"));
		out.write(getFolderSystemFile(folder).getBytes("UTF-8"));
		out.closeEntry();
	}
	
	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	public void addResourcesFromFolder(final ZipOutputStream out, 
			final TreeItemDecorator<FolderEntity> folder, final String zipPath) 
			throws IOException {
		addFolder(out, folder.getEntity(), zipPath);
		List<String> childrenNames = new ArrayList<String>();
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			addResourcesFromFolder(out, child, 
					zipPath + child.getEntity().getName() + "/");
			childrenNames.add(child.getEntity().getName());
		}
		if (zipPath.startsWith("page/")) {
			String pageURL = zipPath.replace("page", "");
			if (!pageURL.equals("/")) {
				pageURL = pageURL.substring(0, pageURL.length() - 1);
			}
			List<PageEntity> children = getDao().getPageDao().getByParent(
					pageURL);
			for (PageEntity child : children) {
				if (!childrenNames.contains(child.getPageFriendlyURL())) {
					addResourcesFromPage(out, child.getFriendlyURL(), 
							zipPath + child.getPageFriendlyURL() + "/");
				}
			}
			List<PageEntity> pages = getDao().getPageDao().selectByUrl(
					pageURL);
			if (pages.size() > 0) {
				addPageFiles(out, pages.get(0), zipPath);
			}
		}
		List<FileEntity> files = getDao().getFileDao().getByFolder(
				folder.getEntity().getId());
		for (FileEntity file : files) {
			String filePath = zipPath + file.getFilename();
			out.putNextEntry(new ZipEntry(filePath));
			out.write(getDao().getFileDao().getFileContent(file));
			out.closeEntry();
		}
	}

	/**
	 * Add files from resource folder to zip archive.
	 * @param out - zip output stream
	 * @param folder - folder tree item
	 * @param zipPath - folder path under which resources will be placed to zip. 
	 * 	             Should not start with / symbol and should end with / symbol.
	 * @throws IOException
	 */
	public void addResourcesFromPage(final ZipOutputStream out, 
			final String pageURL, final String zipPath) 
			throws IOException {
		out.putNextEntry(new ZipEntry(zipPath));
		out.closeEntry();
		List<PageEntity> children = getDao().getPageDao().getByParent(pageURL);
		for (PageEntity child : children) {
			addResourcesFromPage(out, child.getFriendlyURL(), zipPath + 
					child.getPageFriendlyURL() + "/");
		}
		List<PageEntity> pages = getDao().getPageDao().selectByUrl(
				pageURL);
		if (pages.size() > 0) {
			addPageFiles(out, pages.get(0), zipPath);
		}
	}
	
	private void addPageFiles(final ZipOutputStream out, PageEntity page,
			String zipPath) throws IOException {
		saveFile(out, zipPath + "_content.xml", getPageExporter()
				.createPageContentXML(page));
		saveFile(out, zipPath + "_comments.xml", getPageExporter()
				.createPageCommentsXML(page.getFriendlyURL()));
		saveFile(out, zipPath + "_permissions.xml", 
				getPageExporter().createPagePermissionsXML(page.getFriendlyURL()));
	}
	
	private void saveFile(final ZipOutputStream out, String name, 
			String content) throws IOException {
		out.putNextEntry(new ZipEntry(name));
		out.write(content.getBytes("UTF-8"));
		out.closeEntry();
	}

	private String getFolderSystemFile(FolderEntity folder) {
		Document doc = DocumentHelper.createDocument();
		Element e = doc.addElement("folder");
		e.addElement("title").setText(notNull(folder.getTitle()));
		Element p = e.addElement("permissions");
		List<FolderPermissionEntity> list = getDao().getFolderPermissionDao()
				.selectByFolder(folder.getId());
		for (FolderPermissionEntity permission : list) {
			createFolderPermissionXML(p, permission);
		}
		return doc.asXML();
	}

	private void createFolderPermissionXML(Element permissionsElement, 
			final FolderPermissionEntity permission) {
		GroupEntity group = getDao().getGroupDao().getById(
				permission.getGroupId());
		Element permissionElement = permissionsElement.addElement("permission");
		permissionElement.addElement("group").setText(group.getName());
		permissionElement.addElement("permissionType").setText(
				permission.getPermission().name());
	}
	
	public String importResourceFile(final ZipEntry entry, byte[] data)
			throws UnsupportedEncodingException, DaoTaskException {

		String[] chain = FolderUtil.getPathChain(entry);
		String folderPath = FolderUtil.getFilePath(entry);
		String fileName = chain[chain.length - 1];
		//logger.debug("importResourceFile: " + folderPath + " " + fileName + " "
		//		+ data.length);
		FolderEntity folderEntity = getBusiness().getFolderBusiness()
				.createFolder(folderPath);
		//logger.debug("folderEntity: " + folderEntity);
		String contentType = MimeType.getContentTypeByExt(FolderUtil
				.getFileExt(fileName));
		FileEntity fileEntity = getDao().getFileDao().getByName(
				folderEntity.getId(), fileName);
		if (fileEntity != null) {
			fileEntity.setLastModifiedTime(new Date());
			fileEntity.setSize(data.length);
		} else {
			fileEntity = new FileEntity(fileName, fileName, folderEntity
					.getId(), contentType, new Date(), data.length);
		}
		getDaoTaskAdapter().fileSave(fileEntity, data);
		return "/" + entry.getName();
	}
	
	PageExporter getPageExporter() {
		return getExporterFactory().getPageExporter();
	}
	
}
