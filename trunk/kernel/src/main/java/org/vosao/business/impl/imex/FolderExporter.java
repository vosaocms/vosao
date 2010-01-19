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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.imex.dao.DaoTaskAdapter;
import org.vosao.business.impl.imex.dao.DaoTaskException;
import org.vosao.dao.Dao;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.enums.FolderPermissionType;

/**
 * @author Alexander Oleynik
 */
public class FolderExporter extends AbstractExporter {

	private static final Log logger = LogFactory.getLog(
			FolderExporter.class);

	public FolderExporter(Dao aDao, Business aBusiness,
			DaoTaskAdapter daoTaskAdapter) {
		super(aDao, aBusiness, daoTaskAdapter);
	}
	
	public void createFoldersXML(final Element siteElement) {
		Element foldersElement = siteElement.addElement("folders");
		TreeItemDecorator<FolderEntity> root = getBusiness().getFolderBusiness()
				.getTree();
		createFolderXML(foldersElement, root);		
	}

	private void createFolderXML(final Element foldersElement, 
			final TreeItemDecorator<FolderEntity> folder) {
		Element folderElement = foldersElement.addElement("folder");
		folderElement.addElement("name").setText(folder.getEntity().getName());
		folderElement.addElement("title").setText(folder.getEntity().getTitle());
		createFolderPermissionsXML(folderElement, folder.getEntity());
		for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
			createFolderXML(folderElement, child);
		}
	}
	
	private void createFolderPermissionsXML(final Element folderElement, 
			final FolderEntity folder) {
		Element permissionsElement = folderElement.addElement("permissions");
		List<FolderPermissionEntity> list = getDao().getFolderPermissionDao()
				.selectByFolder(folder.getId());
		for (FolderPermissionEntity permission : list) {
			createFolderPermissionXML(permissionsElement, permission);
		}
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
