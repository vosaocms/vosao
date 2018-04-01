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

package org.vosao.service.front.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.SetupBean;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;
import org.vosao.common.BCrypt;
import org.vosao.common.VosaoContext;
import org.vosao.entity.UserEntity;
import org.vosao.filter.AuthenticationFilter;
import org.vosao.filter.LanguageFilter;
import org.vosao.i18n.Messages;
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
		if (user == null || user.isDisabled()) {
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
		VosaoContext ctx = VosaoContext.getInstance();
		ctx.getSession().set(AuthenticationFilter.USER_SESSION_ATTR, user.getEmail());
		String originalView = ctx.getSession().getString(AuthenticationFilter.ORIGINAL_VIEW_KEY);
		logger.info(originalView);
		if (originalView != null) {
			ctx.getSession().remove(AuthenticationFilter.ORIGINAL_VIEW_KEY);
		}
		else {
			originalView = "/";
		}
		getMessageQueue().publish(new SimpleMessage(Topic.LOGIN.name(), 
				user.getEmail()));
		
		return ServiceResponse.createSuccessResponse(originalView);
	}

	@Override
	public ServiceResponse logout() {
		VosaoContext.getInstance().getSession().set(
				AuthenticationFilter.USER_SESSION_ATTR, (String)null);
		
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
	
	@Override
	public Map<String, String> getSystemProperties() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("version", SetupBean.VERSION);
		result.put("fullVersion", SetupBean.FULLVERSION);
		result.put("loggedIn", new Boolean(VosaoContext.getInstance().getUser() != null).toString());
		return result;
	}

	@Override
	public ServiceResponse setLanguage(String language) {
		Locale locale = LanguageFilter.getLocale(language);
		logger.info("Locale " + locale.getDisplayName());
		VosaoContext.getInstance().setLocale(locale);
		VosaoContext.getInstance().getSession().setLocale(locale);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}

}
