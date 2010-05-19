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

package org.vosao.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class ForgotPasswordServlet extends BaseSpringServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter("key");
		UserEntity user = getDao().getUserDao().getByKey(key);
		if (user == null) {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/forgotPasswordFail.jsp");
			dispatcher.forward(request,response);
		}
		else {
			user.setForgotPasswordKey(null);
			getDao().getUserDao().save(user);
			HttpSession session = request.getSession(true);
			session.setAttribute(AuthenticationFilter.USER_SESSION_ATTR, 
					user.getEmail());
			response.sendRedirect("/cms/profile.jsp");
		}
	}
	
}