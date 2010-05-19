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

package org.vosao.service.front.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.vosao.common.BCrypt;
import org.vosao.common.Messages;
import org.vosao.common.VosaoContext;
import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;
import org.vosao.service.ServiceResponse;
import org.vosao.service.front.LoginService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class LoginServiceImpl extends AbstractServiceImpl 
		implements LoginService {

	@Override
	public ServiceResponse login(String email, String password) {
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"user_not_found"));
		}
		ServiceResponse passwordIncorrect = ServiceResponse.createErrorResponse(
				Messages.get("password_incorrect"));
		if (user.getPassword() == null) {
			if (!StringUtils.isEmpty(password)) {
				return passwordIncorrect;
			}
		}
		else {		
			try {
				if (!BCrypt.checkpw(password, user.getPassword())) {
					return passwordIncorrect;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return passwordIncorrect;
			}
		}
		HttpSession session = VosaoContext.getInstance().getRequest()
				.getSession(true);
		session.setAttribute(AuthenticationFilter.USER_SESSION_ATTR, 
				user.getEmail());
		String originalView = (String) session.getAttribute(
				AuthenticationFilter.ORIGINAL_VIEW_KEY);
		if (originalView != null) {
			session.removeAttribute(AuthenticationFilter.ORIGINAL_VIEW_KEY);
			if (originalView.equals("/login.jsp")) {
				originalView = "/cms/index.jsp";
			}
		}
		else {
			originalView = "/cms/index.jsp";
		}
		return ServiceResponse.createSuccessResponse(originalView);
	}

	@Override
	public ServiceResponse logout() {
		HttpSession session = VosaoContext.getInstance().getRequest()
				.getSession(true);
		session.setAttribute(AuthenticationFilter.USER_SESSION_ATTR, null);
		return ServiceResponse.createSuccessResponse(Messages.get(
				"success_logout"));
	}

	@Override
	public ServiceResponse forgotPassword(String email) {
		try {
			getBusiness().getUserBusiness().forgotPassword(email);
			return ServiceResponse.createSuccessResponse(Messages.get("success"));
		}
		catch (Exception e) {
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}
	

}
