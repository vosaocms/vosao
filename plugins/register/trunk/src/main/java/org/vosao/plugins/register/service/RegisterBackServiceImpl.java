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

package org.vosao.plugins.register.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.entity.PluginEntity;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegisterConfigEntity;
import org.vosao.plugins.register.entity.RegistrationEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.service.plugin.AbstractServicePlugin;

public class RegisterBackServiceImpl extends AbstractServicePlugin 
		implements RegisterBackService {

	private static final Log logger = LogFactory.getLog(RegisterBackServiceImpl.class);
	
	private RegisterDao registerDao;
	
	public RegisterBackServiceImpl(Business business, RegisterDao aRegisterDao) {
		setBusiness(business);
		setRegisterDao(aRegisterDao);
	}

	private RegisterDao getRegisterDao() {
		return registerDao;
	}

	private void setRegisterDao(RegisterDao registerDao) {
		this.registerDao = registerDao;
	}
	
	@Override
	public List<RegistrationEntity> getRegistrations() {
		try {
			return getRegisterDao().getRegistrationDao().select();
		}
		catch(Exception e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	@Override
	public RegisterConfigEntity getConfig() {
		return getRegisterDao().getRegisterConfigDao().getConfig();
	}

	@Override
	public ServiceResponse saveConfig(Map<String, String> vo) {
		RegisterConfigEntity config = getRegisterDao().getRegisterConfigDao()
				.getConfig();
		config.setAdminEmail(vo.get("adminEmail"));
		config.setSendConfirmAdmin(Boolean.valueOf(vo.get("sendConfirmAdmin")));
		config.setSendConfirmUser(Boolean.valueOf(vo.get("sendConfirmUser")));
		config.setClearDays(Integer.valueOf(vo.get("clearDays")));
		config.setRegisterFormTemplate(vo.get("registerFormTemplate"));
		config.setConfirmUserTemplate(vo.get("confirmUserTemplate"));
		config.setConfirmAdminTemplate(vo.get("confirmAdminTemplate"));
		getRegisterDao().getRegisterConfigDao().save(config);
		return ServiceResponse.createSuccessResponse("Saved.");
	}
	
	
}
