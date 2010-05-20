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

import org.vosao.enums.UserRole;
import static org.vosao.utils.EntityUtil.*;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class UserEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 4L;

	private String name;
	private String password;
	private String email;
	private UserRole role;
	private String forgotPasswordKey;
	private boolean disabled;
	
	public UserEntity() {
		role = UserRole.USER;
		disabled = false;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		name = getStringProperty(entity, "name");
		password = getStringProperty(entity, "password");
		email = getStringProperty(entity, "email");
		role = UserRole.valueOf(getStringProperty(entity, "role"));
		forgotPasswordKey = getStringProperty(entity, "forgotPasswordKey");
		disabled = getBooleanProperty(entity, "disabled", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "name", name, false);
		setProperty(entity, "password", password, false);
		setProperty(entity, "email", email, true);
		setProperty(entity, "role", role.name(), true);
		setProperty(entity, "forgotPasswordKey", forgotPasswordKey, true);
		setProperty(entity, "disabled", disabled, false);
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

	public String getForgotPasswordKey() {
		return forgotPasswordKey;
	}

	public void setForgotPasswordKey(String forgotPasswordKey) {
		this.forgotPasswordKey = forgotPasswordKey;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
}
