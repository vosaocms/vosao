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

package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.enums.UserRole;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class UserEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 3L;

	private String name;
	private String password;
	private String email;
	private UserRole role;
	
	public UserEntity() {
		role = UserRole.USER;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		name = getStringProperty(entity, "name");
		password = getStringProperty(entity, "password");
		email = getStringProperty(entity, "email");
		role = UserRole.valueOf(getStringProperty(entity, "role"));
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setUnindexedProperty("name", name);
		entity.setUnindexedProperty("password", password);
		entity.setProperty("email", email);
		entity.setProperty("role", role.name());
	}

	public UserEntity(String aName, String aPassword,
			String anEmail, UserRole aRole) {
		this();
		name = aName;
		password = aPassword;
		email = anEmail;
		role = aRole;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String aPassword) {
		password = aPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getRoleString() {
		if (role == null) {
			return "";
		}
		return role.name();
	}
	
	public boolean isAdmin() {
		if (role == null) {
			return false;
		}
		return role.equals(UserRole.ADMIN);
	}

	public boolean isSiteUser() {
		if (role == null) {
			return false;
		}
		return role.equals(UserRole.SITE_USER);
	}

	public boolean isUser() {
		if (role == null) {
			return false;
		}
		return role.equals(UserRole.USER);
	}
	
	public boolean isEditor() {
		return isAdmin() || isUser();
	}
	
}
