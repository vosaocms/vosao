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

import java.util.Iterator;

import org.dom4j.Element;
import org.vosao.business.imex.FolderExporter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.enums.FolderPermissionType;

/**
 * @author Alexander Oleynik
 */
public class FolderExporterImpl extends AbstractExporter 
		implements FolderExporter {

	public FolderExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	public void readFolders(Element foldersElement) throws DaoTaskException {
		for (Iterator<Element> i = foldersElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("folder")) {
            	FolderEntity root = getDao().getFolderDao().getByPath("/");
            	readFolder(element, root);
            }
		}
	}
	
	private void readFolder(Element element, final FolderEntity parent) 
			throws DaoTaskException {
		String name = element.elementText("name");
		String title = element.elementText("title");
		FolderEntity folder;
		if (name.equals("/") && parent.isRoot()) {
			folder = getDao().getFolderDao().getByPath("/");
		}
		else {
			folder = getDao().getFolderDao().getByParentName(
					parent.getId(), name);
			if (folder == null) {
				folder = new FolderEntity(title, name, parent.getId());
			}
		}
		folder.setTitle(title);
		getDaoTaskAdapter().folderSave(folder);
		readFolderPermissions(element.element("permissions"), folder);
		for (Iterator<Element> i = element.elementIterator(); i
				.hasNext();) {
			Element e = i.next();
			if (e.getName().equals("folder")) {
				readFolder(e, folder);
			}
		}
	}
	
	private void readFolderPermissions(Element permissionsElement,
			final FolderEntity folder) {
		for (Iterator<Element> i = permissionsElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("permission")) {
            	String groupName = element.elementText("group");
            	GroupEntity group = getDao().getGroupDao().getByName(groupName);
            	if (group == null) {
            		logger.error("Group " + groupName + " was not found.");
            		continue;
            	}
            	FolderPermissionType permType = FolderPermissionType.valueOf(
            			element.elementText("permissionType"));
            	getBusiness().getFolderPermissionBusiness().setPermission(
            			folder, group, permType);
            }
		}		
	}
}
