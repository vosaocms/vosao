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

package org.vosao.plugins.register.dao;

import org.vosao.business.Business;
import org.vosao.dao.Dao;

public class RegisterDao {

	private RegistrationDao registrationDao;
	private RegisterConfigDao registerConfigDao;
	private Business business;

	public RegisterDao(Business aBusiness) {
		setBusiness(aBusiness);
	}
	
	public Dao getDao() {
		return getBusiness().getDao();
	}

	public RegistrationDao getRegistrationDao() {
		if (registrationDao == null) {
			registrationDao = new RegistrationDaoImpl();
		}
		return registrationDao;
	}
	
	public RegisterConfigDao getRegisterConfigDao() {
		if (registerConfigDao == null) {
			registerConfigDao = new RegisterConfigDaoImpl();
		}
		return registerConfigDao;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

}
