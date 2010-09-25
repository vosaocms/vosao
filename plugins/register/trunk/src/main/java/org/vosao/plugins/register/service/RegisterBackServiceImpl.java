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

package org.vosao.plugins.register.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.vosao.business.Business;
import org.vosao.common.BCrypt;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;
import org.vosao.i18n.Messages;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegisterConfigEntity;
import org.vosao.plugins.register.entity.RegistrationEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.utils.StreamUtil;

public class RegisterBackServiceImpl extends AbstractRegisterService 
		implements RegisterBackService {

	private static final String REGISTER_FORM_TEMPLATE = 
		"org/vosao/plugins/register/resources/registerForm.html";
	
	private static final String CONFIRM_ADMIN_TEMPLATE = 
		"org/vosao/plugins/register/resources/adminConfirmLetter.html";
	
	private static final String CONFIRM_USER_TEMPLATE = 
		"org/vosao/plugins/register/resources/userConfirmLetter.html";

	public RegisterBackServiceImpl(Business business, RegisterDao aRegisterDao) {
		super(business, aRegisterDao);
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
		RegisterConfigEntity config = getRegisterDao().getRegisterConfigDao()
				.getConfig();
		if (config.isNew()) {
			config.setClearDays(10);
			config.setRegisterFormTemplate(getTextResource(REGISTER_FORM_TEMPLATE));
			config.setConfirmAdminTemplate(getTextResource(CONFIRM_ADMIN_TEMPLATE));
			config.setConfirmUserTemplate(getTextResource(CONFIRM_USER_TEMPLATE));
		}
		return config;
	}

	private String getTextResource(String path) {
		try {
			return StreamUtil.getTextResource(this.getClass().getClassLoader(), 
					path);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
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
		config.setCaptcha(Boolean.valueOf(vo.get("captcha")));
		getRegisterDao().getRegisterConfigDao().save(config);
		return ServiceResponse.createSuccessResponse(
				Messages.get("success"));
	}

	@Override
	public ServiceResponse restoreAdminConfirmLetter() {
		RegisterConfigEntity config = getConfig();
		config.setConfirmAdminTemplate(getTextResource(
				CONFIRM_ADMIN_TEMPLATE));
		getRegisterDao().getRegisterConfigDao().save(config);
		return ServiceResponse.createSuccessResponse(
				config.getConfirmAdminTemplate());
	}

	@Override
	public ServiceResponse restoreRegisterFormTemplate() {
		RegisterConfigEntity config = getConfig();
		config.setRegisterFormTemplate(getTextResource(
				REGISTER_FORM_TEMPLATE));
		getRegisterDao().getRegisterConfigDao().save(config);
		return ServiceResponse.createSuccessResponse(
				config.getRegisterFormTemplate());
	}

	@Override
	public ServiceResponse restoreUserConfirmLetter() {
		RegisterConfigEntity config = getConfig();
		config.setConfirmUserTemplate(getTextResource(
				CONFIRM_USER_TEMPLATE));
		getRegisterDao().getRegisterConfigDao().save(config);
		return ServiceResponse.createSuccessResponse(
				config.getConfirmUserTemplate());
	}

	@Override
	public ServiceResponse removeRegistration(Long id) {
		RegistrationEntity reg = getRegisterDao().getRegistrationDao().getById(id);
		if (reg != null) {
			getRegisterDao().getRegistrationDao().remove(id);
		}
		return ServiceResponse.createSuccessResponse(
				Messages.get("success"));
	}

	@Override
	public ServiceResponse confirmRegistration(Long id) {
		RegistrationEntity reg = getRegisterDao().getRegistrationDao().getById(id);
		if (reg != null) {
			UserEntity user = new UserEntity(reg.getName(), 
					BCrypt.hashpw(reg.getPassword(), BCrypt.gensalt()), 
					reg.getEmail(), UserRole.SITE_USER);
			getDao().getUserDao().save(user);
			getRegisterDao().getRegistrationDao().remove(id);
		}
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}
	
}
