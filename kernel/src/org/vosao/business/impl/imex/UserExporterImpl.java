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

import static org.vosao.utils.XmlUtil.notNull;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.vosao.business.imex.UserExporter;
import org.vosao.common.BCrypt;
import org.vosao.dao.DaoTaskException;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;
import org.vosao.utils.ParamUtil;

/**
 * @author Alexander Oleynik
 */
public class UserExporterImpl extends AbstractExporter 
		implements UserExporter {

	public UserExporterImpl(ExporterFactoryImpl factory) {
		super(factory);
	}
	
	@Override
	public String createUsersXML() {
		Document doc = DocumentHelper.createDocument();
		Element usersElement = doc.addElement("users");
		createUsersXML(usersElement);
		return doc.asXML();
	}

	private void createUsersXML(Element usersElement) {
		List<UserEntity> list = getDao().getUserDao().select();
		for (UserEntity user : list) {
			createUserXML(usersElement, user);
		}
	}

	private void createUserXML(Element usersElement, final UserEntity user) {
		Element userElement = usersElement.addElement("user");
		userElement.addElement("name").setText(user.getName());
		userElement.addElement("email").setText(user.getEmail());
		userElement.addElement("password").setText(notNull(user.getPassword()));
		userElement.addElement("role").setText(user.getRole().name());
		userElement.addElement("disabled").setText(
				String.valueOf(user.isDisabled()));
	}
	
	public void readUsers(Element usersElement) throws DaoTaskException {
		for (Iterator<Element> i = usersElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("user")) {
            	String email = element.elementText("email");
            	String name = element.elementText("name");
            	boolean disabled = ParamUtil.getBoolean(
            			element.elementText("disabled"), false);
            	String password = element.elementText("password");
            	try {
            		BCrypt.checkpw("test", password);
            	}
            	catch (Exception e) {
            		password = BCrypt.hashpw(password, BCrypt.gensalt());
            	}
            	UserRole role = UserRole.valueOf(element.elementText("role"));
            	UserEntity user = getDao().getUserDao().getByEmail(email);
            	if (user == null) {
            		user = new UserEntity(name, password, email, role);
            	}
            	user.setName(name);
            	user.setPassword(password);
            	user.setRole(role);
            	user.setDisabled(disabled);
            	getDaoTaskAdapter().userSave(user);
            }
		}		
	}
	
	/**
	 * Read and import data from _users.xml.
	 * @param xml - _users.xml content.
	 * @throws DocumentException 
	 * @throws DaoTaskException 
	 */
	public void readUsersFile(String xml) throws DocumentException, 
			DaoTaskException {
		Document doc = DocumentHelper.parseText(xml);
		readUsers(doc.getRootElement());
	}
}
