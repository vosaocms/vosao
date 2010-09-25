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

package org.vosao.plugins.register.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegistrationEntity;
import org.vosao.plugins.register.service.RegisterBackService;

public class ConfirmServlet extends HttpServlet {

	private RegisterDao registerDao;
	private RegisterBackService registerBackService;
	
	public ConfirmServlet(RegisterDao aRegisterDao,
			RegisterBackService aRegisterBackService) {
		setRegisterDao(aRegisterDao);
		setRegisterBackService(aRegisterBackService);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sessionKey = request.getParameter("id");
		RegistrationEntity reg = getRegisterDao().getRegistrationDao()
				.getBySessionKey(sessionKey);
		if (reg != null) {
			getRegisterBackService().confirmRegistration(reg.getId());
		}
		response.sendRedirect("/");		
	}

	public RegisterDao getRegisterDao() {
		return registerDao;
	}

	public void setRegisterDao(RegisterDao registerDao) {
		this.registerDao = registerDao;
	}

	public RegisterBackService getRegisterBackService() {
		return registerBackService;
	}

	public void setRegisterBackService(RegisterBackService registerBackService) {
		this.registerBackService = registerBackService;
	}
	
}
