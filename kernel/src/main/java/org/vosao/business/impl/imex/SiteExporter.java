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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.imex.task.TaskTimeoutException;
import org.vosao.business.imex.task.ZipOutStreamTaskAdapter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FolderEntity;
import org.vosao.utils.FolderUtil;

public class SiteExporter extends AbstractExporter {

	public SiteExporter(ExporterFactory factory) {
		super(factory);
	}

	public boolean isSiteContent(final ZipEntry entry)
			throws UnsupportedEncodingException {
		String[] chain = FolderUtil.getPathChain(entry);
		if (chain.length != 1 || !chain[0].equals("content.xml")) {
			return false;
		}
		return true;
	}

	public void exportSite(final ZipOutStreamTaskAdapter out) 
			throws IOException, TaskTimeoutException {
		if (!out.isSkip("_users.xml")) {
			saveFile(out, "_users.xml", getUserExporter().createUsersXML());
		}
		if (!out.isSkip("_groups.xml")) {
			saveFile(out, "_groups.xml", getGroupExporter().createGroupsXML());
		}
		if (!out.isSkip("_config.xml")) {
			saveFile(out, "_config.xml", getConfigExporter().createConfigXML());
		}
		if (!out.isSkip("_structures.xml")) {
			saveFile(out, "_structures.xml", getStructureExporter()
				.createStructuresXML());
		}
		if (!out.isSkip("_forms.xml")) {
			saveFile(out, "_forms.xml", getFormExporter().createFormsXML());
		}
		if (!out.isSkip("_messages.xml")) {
			saveFile(out, "_messages.xml", getMessagesExporter()
				.createMessagesXML());
		}
		if (!out.isSkip("_plugins.xml")) {
			saveFile(out, "_plugins.xml", getPluginExporter().createPluginsXML());
		}
		TreeItemDecorator<FolderEntity> page = getBusiness().getFolderBusiness()
				.findFolderByPath(getBusiness().getFolderBusiness().getTree(), 
						"/page");
		if (page != null) {
			getResourceExporter().addResourcesFromFolder(out, page, "page/");
		}
	}

	private void saveFile(final ZipOutStreamTaskAdapter out, String name, 
			String content) throws IOException, TaskTimeoutException {
		out.putNextEntry(new ZipEntry(name));
		out.write(content.getBytes("UTF-8"));
		out.closeEntry();
	}
	
	public void readSiteContent(final ZipEntry entry, final String xml)
			throws DocumentException, DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		Element root = doc.getRootElement();
		for (Iterator<Element> i = root.elementIterator(); i.hasNext();) {
			Element element = i.next();
			if (element.getName().equals("config")) {
				getConfigExporter().readConfigs(element);
			}
			if (element.getName().equals("pages")) {
				getPageExporter().readPages(element);
			}
			if (element.getName().equals("forms")) {
				getFormExporter().readForms(element);
			}
			if (element.getName().equals("users")) {
				getUserExporter().readUsers(element);
			}
			if (element.getName().equals("groups")) {
				getGroupExporter().readGroups(element);
			}
			if (element.getName().equals("folders")) {
				getFolderExporter().readFolders(element);
			}
			if (element.getName().equals("messages")) {
				getMessagesExporter().readMessages(element);
			}
			if (element.getName().equals("structures")) {
				getStructureExporter().readStructures(element);
			}
			if (element.getName().equals("plugins")) {
				getPluginExporter().readPlugins(element);
			}
		}
	}

	private PluginExporter getPluginExporter() {
		return getExporterFactory().getPluginExporter();
	}

	private MessagesExporter getMessagesExporter() {
		return getExporterFactory().getMessagesExporter();
	}

	private StructureExporter getStructureExporter() {
		return getExporterFactory().getStructureExporter();
	}

	private FolderExporter getFolderExporter() {
		return getExporterFactory().getFolderExporter();
	}

	private UserExporter getUserExporter() {
		return getExporterFactory().getUserExporter();
	}

	private GroupExporter getGroupExporter() {
		return getExporterFactory().getGroupExporter();
	}

	private ConfigExporter getConfigExporter() {
		return getExporterFactory().getConfigExporter();
	}
	
	private PageExporter getPageExporter() {
		return getExporterFactory().getPageExporter();
	}

	private FormExporter getFormExporter() {
		return getExporterFactory().getFormExporter();
	}

	private ResourceExporter getResourceExporter() {
		return getExporterFactory().getResourceExporter();
	}

	private ThemeExporter getThemeExporter() {
		return getExporterFactory().getThemeExporter();
	}

	private String toXML(ByteArrayOutputStream data) 
			throws UnsupportedEncodingException {
		return data.toString("UTF-8");
	}
	
	public boolean importSystemFile(ZipEntry entry, ByteArrayOutputStream data) 
			throws DocumentException, DaoTaskException, 
			UnsupportedEncodingException {
		if (entry.getName().equals("_users.xml")) {
			getUserExporter().readUsersFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_groups.xml")) {
			getGroupExporter().readGroupsFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_config.xml")) {
			getConfigExporter().readConfigFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_structures.xml")) {
			getStructureExporter().readStructuresFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_forms.xml")) {
			getFormExporter().readFormsFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_messages.xml")) {
			getMessagesExporter().readMessagesFile(toXML(data));
			return true;
		}
		if (entry.getName().equals("_plugins.xml")) {
			getPluginExporter().readPluginsFile(toXML(data));
			return true;
		}
		
		if (entry.getName().endsWith("_folder.xml")) {
			String folderPath = FolderUtil.getFilePath("/" + entry.getName());
			getResourceExporter().readFolderFile(folderPath, toXML(data));
			return true;
		}
		if (entry.getName().endsWith("_template.xml")) {
			return getThemeExporter().readTemplateFile("/" + entry.getName(), 
					toXML(data));
		}
		if (entry.getName().endsWith("_content.xml")) {
			String folderPath = FolderUtil.getFilePath("/" + entry.getName());
			return getPageExporter().readContentFile(folderPath, toXML(data));
		}
		if (entry.getName().endsWith("_comments.xml")) {
			String folderPath = FolderUtil.getFilePath("/" + entry.getName());
			return getPageExporter().readCommentsFile(folderPath, toXML(data));
		}
		if (entry.getName().endsWith("_permissions.xml")) {
			String folderPath = FolderUtil.getFilePath("/" + entry.getName());
			return getPageExporter().readPermissionsFile(folderPath, toXML(
					data));
		}
		return false;
	}
	
	
}
