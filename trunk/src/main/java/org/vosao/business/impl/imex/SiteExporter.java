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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.servlet.FolderUtil;

public class SiteExporter extends AbstractExporter {

	private ConfigExporter configExporter;
	private PageExporter pageExporter;
	private FormExporter formExporter;
	private UserExporter userExporter;
	private GroupExporter groupExporter;
	private FolderExporter folderExporter;

	public SiteExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
		configExporter = new ConfigExporter(aDao, aBusiness);
		pageExporter = new PageExporter(aDao, aBusiness);
		formExporter = new FormExporter(aDao, aBusiness);
		userExporter = new UserExporter(aDao, aBusiness);
		groupExporter = new GroupExporter(aDao, aBusiness);
		folderExporter = new FolderExporter(aDao, aBusiness);
	}

	public boolean isSiteContent(final ZipEntry entry)
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length != 1 || !chain[0].equals("content.xml")) {
			return false;
		}
		return true;
	}

	public void exportSite(final ZipOutputStream out) throws IOException {
		String contentName = "content.xml";
		out.putNextEntry(new ZipEntry(contentName));
		out.write(createSiteExportXML().getBytes("UTF-8"));
		out.closeEntry();
		pageExporter.addContentResources(out);
	}

	private String createSiteExportXML() {
		Document doc = DocumentHelper.createDocument();
		Element siteElement = doc.addElement("site");
		userExporter.createUsersXML(siteElement);
		groupExporter.createGroupsXML(siteElement);
		configExporter.createConfigXML(siteElement);
		pageExporter.createPagesXML(siteElement);
		formExporter.createFormsXML(siteElement);
		folderExporter.createFoldersXML(siteElement);
		return doc.asXML();
	}
	
	
	public void readSiteContent(final ZipEntry entry, final String xml)
			throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("config")) {
				configExporter.readConfigs(element);
			}
			if (element.getName().equals("pages")) {
				pageExporter.readPages(element);
			}
			if (element.getName().equals("forms")) {
				formExporter.readForms(element);
			}
			if (element.getName().equals("users")) {
				userExporter.readUsers(element);
			}
			if (element.getName().equals("groups")) {
				groupExporter.readGroups(element);
			}
			if (element.getName().equals("folders")) {
				folderExporter.readFolders(element);
			}
		}
	}


}
