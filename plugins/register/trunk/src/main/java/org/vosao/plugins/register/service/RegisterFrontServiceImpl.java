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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.vosao.business.Business;
import org.vosao.common.BCrypt;
import org.vosao.common.PluginException;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.UserEntity;
import org.vosao.i18n.Messages;
import org.vosao.plugins.register.dao.RegisterDao;
import org.vosao.plugins.register.entity.RegisterConfigEntity;
import org.vosao.plugins.register.entity.RegistrationEntity;
import org.vosao.service.ServiceResponse;
import org.vosao.utils.EmailUtil;
import org.vosao.utils.ParamUtil;
import org.vosao.utils.RecaptchaUtil;
import org.vosao.utils.StrUtil;

public class RegisterFrontServiceImpl extends AbstractRegisterService 
		implements RegisterFrontService {

	public RegisterFrontServiceImpl(Business business, RegisterDao aRegisterDao) {
		super(business, aRegisterDao);
	}

	@Override
	public ServiceResponse register(Map<String, String> vo, 
			String challenge, String response, HttpServletRequest request) {
		RegisterConfigEntity registerConfig = getRegisterDao().getRegisterConfigDao()
				.getConfig();
		if (registerConfig.isCaptcha()) {
			ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
			ReCaptchaResponse recaptchaResponse = RecaptchaUtil.check(
				config.getRecaptchaPublicKey(), 
				config.getRecaptchaPrivateKey(), 
				challenge, response, request); 
			if (!recaptchaResponse.isValid()) {
				return ServiceResponse.createErrorResponse(
						Messages.get("incorrect_captcha"));
			}
		}
		RegistrationEntity reg = getRegisterDao().getRegistrationDao()
				.getByEmail(vo.get("email")); 
		if (reg == null) {	
			reg = new RegistrationEntity();
		}
		reg.setCreatedDate(new Date());
		reg.setEmail(ParamUtil.filterXSS(vo.get("email")));
		reg.setName(ParamUtil.filterXSS(vo.get("name")));
		reg.setPassword(vo.get("password1"));
		reg.createSessionKey();
		UserEntity user = getDao().getUserDao().getByEmail(reg.getEmail());
		if (user != null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"register.user_exists", reg.getEmail()));
		}
		getRegisterDao().getRegistrationDao().save(reg);
		try {
			sendConfirmLetter(reg);
			return ServiceResponse.createSuccessResponse(
					Messages.get("success"));
		}
		catch (PluginException e) {
			e.printStackTrace();
			return ServiceResponse.createErrorResponse(e.getMessage());
		}
	}

	private void sendConfirmLetter(RegistrationEntity reg) 
			throws PluginException {
		RegisterConfigEntity registerConfig = getRegisterDao().getRegisterConfigDao()
				.getConfig();
		ConfigEntity config = getDao().getConfigDao().getConfig();
		VelocityContext context = new VelocityContext();
		context.put("config", config);
		context.put("registerConfig", registerConfig);
		context.put("registration", reg);
		if (registerConfig.isSendConfirmAdmin()) {
			List<String> emails = StrUtil.fromCSV(registerConfig.getAdminEmail());
			for (String email : emails) {
				sendConfirmEmail(registerConfig.getConfirmAdminTemplate(), 
					context, config.getSiteEmail(), StringUtils.strip(email));
			}
		}
		if (registerConfig.isSendConfirmUser()) {
			sendConfirmEmail(registerConfig.getConfirmUserTemplate(), 
					context, config.getSiteEmail(), reg.getEmail());
		}
	}
	
	private void sendConfirmEmail(String template, VelocityContext context,
			String fromAddress, String toAddress) throws PluginException {
		String letter = getBusiness().getSystemService().render(template, context);
		letter = letter.replace("\n", "<br/>");
		String error = EmailUtil.sendEmail(letter, "Confirm registration", 
				fromAddress, "", toAddress);
		if (error != null) {
			throw new PluginException(error);
		}
	}

	@Override
	public ServiceResponse changePassword(Long userId, String oldPassword,
			String newPassword) {
		UserEntity user = getDao().getUserDao().getById(userId);
		if (user == null) {
			return ServiceResponse.createErrorResponse(Messages.get("user.user_not_found"));
		}
		if (user.getPassword().equals(BCrypt.hashpw(oldPassword, BCrypt.gensalt()))) {
			user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
			getDao().getUserDao().save(user);
			return ServiceResponse.createSuccessResponse(Messages.get("success"));
		}
		else {
			return ServiceResponse.createSuccessResponse(Messages.get("password_incorrect"));
		}
	}
	
	
}
