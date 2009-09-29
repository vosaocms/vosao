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

package org.vosao.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.UserEntity;


public class ProfileBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String password1;
	private String password2;
	private UserEntity user;
	
	public void init() {
		user = getBusiness().getUserPreferences().getUser(); 
	}
	
	private List<String> validate() {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isEmpty(user.getEmail())) {
			errors.add("Email is empty");
		}
		if (!StringUtils.isEmpty(password1) 
			|| !StringUtils.isEmpty(password2)) {
			if (!password1.equals(password2)) {
				errors.add("Passwords don't match");
			}
			else {
				user.setPassword(password1);
			}
		}
		return errors;
	}
	
	public void save() {
		List<String> errors = validate();
		if (errors.isEmpty()) {
			getDao().getUserDao().save(user);
			getBusiness().getUserPreferences().setUser(user);
		}
		else {
			JSFUtil.addErrorMessages(errors);
		}
	}
	
	public String getPassword1() {
		return password1;
	}
	
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
