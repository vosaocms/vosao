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

package org.vosao.plugins.register;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegistrationEntity;
import org.vosao.service.plugin.AbstractServicePlugin;

public class RegisterBackService extends AbstractServicePlugin {

	private static final Log logger = LogFactory.getLog(RegisterBackService.class);
	
	private RegisterDao registerDao;
	
	public RegisterBackService(Business business, RegisterDao aRegisterDao) {
		setBusiness(business);
		setRegisterDao(aRegisterDao);
	}

	public List<RegistrationEntity> getRegistrations() {
		try {
			return getRegisterDao().getRegistrationDao().select();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	public RegisterDao getRegisterDao() {
		return registerDao;
	}

	public void setRegisterDao(RegisterDao registerDao) {
		this.registerDao = registerDao;
	}
	
}
