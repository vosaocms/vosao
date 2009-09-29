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

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;


public class LoginBean extends AbstractJSFBean implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String email;
	private String password;
	
	public void login() throws IOException {
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			JSFUtil.addErrorMessage("User was not found.");
			return;
		}
		if (!user.getPassword().equals(password)) {
			JSFUtil.addErrorMessage("Password incorrect.");
			return;
		}
		getBusiness().getUserPreferences().setUser(user);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		String originalView = (String) request.getSession().getAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		request.getSession().removeAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		if (originalView.equals("/login")) {
			originalView = "/cms";
		}
		response.sendRedirect(originalView);
	}

	public void logout() throws IOException {
		getBusiness().getUserPreferences().setUser(null);
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();
		response.sendRedirect("/");
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserEntity getCurrentUser() {
		return getBusiness().getUserPreferences().getUser();
	}
	
	
}
