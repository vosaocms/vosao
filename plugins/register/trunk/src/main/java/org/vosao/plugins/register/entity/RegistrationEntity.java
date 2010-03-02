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

package org.vosao.plugins.register.entity;

import java.util.Date;

import org.vosao.entity.BaseEntityImpl;

import com.google.appengine.api.datastore.Entity;

public class RegistrationEntity extends BaseEntityImpl {

	private String name;
	private String email;
	private String password;
	private Date createdDate;

	public RegistrationEntity() {
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		name = getStringProperty(entity, "name");
		email = getStringProperty(entity, "email");
		password = getStringProperty(entity, "password");
		createdDate = getDateProperty(entity, "createdDate");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("name", name);
		entity.setProperty("email", email);
		entity.setProperty("password", password);
		entity.setProperty("createdDate", createdDate);
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
