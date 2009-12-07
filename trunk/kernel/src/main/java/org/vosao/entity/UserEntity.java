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

/**
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 2L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String password;

	@Persistent
	private String email;

	@Persistent
	private UserRole role;
	
	public UserEntity() {
	}
	
	public UserEntity(String aName, String aPassword,
			String anEmail, UserRole aRole) {
		this();
		name = aName;
		password = aPassword;
		email = anEmail;
		role = aRole;
	}
	
	public void copy(final UserEntity entity) {
		setName(entity.getName());
		setPassword(entity.getPassword());
		setEmail(entity.getEmail());
		setRole(entity.getRole());		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

	public boolean equals(Object object) {
		if (object instanceof UserEntity) {
			UserEntity entity = (UserEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
	
}
