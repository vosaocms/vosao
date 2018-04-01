/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.business.impl.imex;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.GroupExporter;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * @author Alexander Oleynik
 */
public class GroupExporterImpl extends AbstractExporter 
		implements GroupExporter {

	public GroupExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createGroupsXML() {
		Document doc = DocumentHelper.createDocument();
		Element groupsElement = doc.addElement("groups");
		createGroupsXML(groupsElement);
		return doc.asXML();
	}

	private void createGroupsXML(Element groupsElement) {
		List<GroupEntity> list = getDao().getGroupDao().select();
		for (GroupEntity Group : list) {
			createGroupXML(groupsElement, Group);
		}
	}

	private void createGroupXML(Element groupsElement, final GroupEntity group) {
		Element groupElement = groupsElement.addElement("group");
		groupElement.addElement("name").setText(group.getName());
		List<UserGroupEntity> userGroups = getDao().getUserGroupDao()
				.selectByGroup(group.getId());
		Element users = groupElement.addElement("users");
		Map<Long, UserEntity> usersMap = UserHelper.createIdMap(getDao()
						.getUserDao().select()); 
		for (UserGroupEntity userGroup : userGroups) {
			UserEntity user = usersMap.get(userGroup.getUserId());
			if (user != null) {
				users.addElement("user").setText(user.getEmail());
			}
		}
	}
	
	public void readGroups(Element groupsElement) throws DaoTaskException {
		Map<Long, UserEntity> usersMap = UserHelper.createIdMap(getDao()
				.getUserDao().select()); 
		for (Iterator<Element> i = groupsElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("group")) {
            	String name = element.elementText("name");
            	GroupEntity group = getDao().getGroupDao().getByName(name);
            	if (group == null) {
            		group = new GroupEntity(name);
            	}
            	else {
            		group.setName(name);
            	}
            	getDaoTaskAdapter().groupSave(group);
            	for (Iterator<Element> j = element.element("users").elementIterator();
        				j.hasNext(); ) {
                    Element userElement = j.next();
                    UserEntity user = getDao().getUserDao().getByEmail(
                    		userElement.getText());
                    if (user != null) {
                    	UserGroupEntity userGroup = getDao().getUserGroupDao()
                    			.getByUserGroup(group.getId(), user.getId());
            			if (userGroup == null) {
            				userGroup = new UserGroupEntity(group.getId(), 
            						user.getId());
            			}
                    	getDaoTaskAdapter().userGroupSave(userGroup);
                    }
        		}
            }
		}		
	}
	
	/**
	 * Read and import data from _groups/xml file.
	 * @param xml - _groups.xml file content.
	 * @throws DaoTaskException
	 * @throws DocumentException
	 */
	public void readGroupsFile(String xml) throws DaoTaskException, 
			DocumentException {
		Document doc = DocumentHelper.parseText(xml);
		readGroups(doc.getRootElement());
	}
	
}
