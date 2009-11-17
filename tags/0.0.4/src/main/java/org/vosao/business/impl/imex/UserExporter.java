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

import org.dom4j.Element;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

/**
 * @author Alexander Oleynik
 */
public class UserExporter extends AbstractExporter {

	public UserExporter(Dao aDao, Business aBusiness) {
		super(aDao, aBusiness);
	}
	
	public void createUsersXML(Element siteElement) {
		Element usersElement = siteElement.addElement("users");
		List<UserEntity> list = getDao().getUserDao().select();
		for (UserEntity user : list) {
			createUserXML(usersElement, user);
		}
	}

	private void createUserXML(Element usersElement, final UserEntity user) {
		Element userElement = usersElement.addElement("user");
		userElement.addElement("name").setText(user.getName());
		userElement.addElement("email").setText(user.getEmail());
		userElement.addElement("password").setText(user.getPassword());
		userElement.addElement("role").setText(user.getRole().name());
	}
	
	public void readUsers(Element usersElement) {
		for (Iterator<Element> i = usersElement.elementIterator(); 
				i.hasNext(); ) {
            Element element = i.next();
            if (element.getName().equals("user")) {
            	String email = element.elementText("email");
            	String name = element.elementText("name");
            	String password = element.elementText("password");
            	UserRole role = UserRole.valueOf(element.elementText("role"));
            	UserEntity user = getDao().getUserDao().getByEmail(email);
            	if (user == null) {
            		user = new UserEntity(name, password, email, role);
                	getDao().getUserDao().save(user);
            	}
            }
		}		
	}
}
