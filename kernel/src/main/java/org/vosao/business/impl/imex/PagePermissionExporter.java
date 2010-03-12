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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.enums.ContentPermissionType;

/**
 * @author Alexander Oleynik
 */
public class PagePermissionExporter extends AbstractExporter {

	private static final Log logger = LogFactory.getLog(
			PagePermissionExporter.class);

	public PagePermissionExporter(ExporterFactory factory) {
		super(factory);
	}
	
	public void createPagePermissionsXML(final Element pageElement, 
			final String friendlyUrl) {
		Element permissionsElement = pageElement.addElement("permissions");
		List<ContentPermissionEntity> list = getDao().getContentPermissionDao()
				.selectByUrl(friendlyUrl);
		for (ContentPermissionEntity permission : list) {
			createPagePermissionXML(permissionsElement, permission);
		}
	}

	private void createPagePermissionXML(Element permissionsElement, 
			final ContentPermissionEntity permission) {
		GroupEntity group = getDao().getGroupDao().getById(permission.getGroupId());
		Element permissionElement = permissionsElement.addElement("permission");
		permissionElement.addElement("group").setText(group.getName());
		permissionElement.addElement("permissionType").setText(
				permission.getPermission().name());
		permissionElement.addElement("allLanguages").setText(
				String.valueOf(permission.isAllLanguages()));
		permissionElement.addElement("languages").setText(notNull(
				permission.getLanguages()));
	}
	
	public void readPagePermissions(Element permissionsElement,
			final String url) {
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
            	ContentPermissionType permType = ContentPermissionType.valueOf(
            			element.elementText("permissionType"));
            	boolean allLanguages = Boolean.valueOf(element.elementText(
            			"allLanguages"));
            	String languages = element.elementText("languages");
            	getBusiness().getContentPermissionBusiness().setPermission(
            			url, group, permType, languages);
            }
		}		
	}
}
