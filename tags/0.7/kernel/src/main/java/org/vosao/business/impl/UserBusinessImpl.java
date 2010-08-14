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

package org.vosao.business.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.vosao.business.UserBusiness;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.UserEntity;
import org.vosao.i18n.Messages;
import org.vosao.utils.EmailUtil;
import org.vosao.utils.HashUtil;
import org.vosao.utils.StreamUtil;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class UserBusinessImpl extends AbstractBusinessImpl 
	implements UserBusiness {
	
	@Override
	public List<String> validateBeforeUpdate(final UserEntity user) {
		List<String> errors = new ArrayList<String>();
		UserEntity foundUser = getDao().getUserDao().getByEmail(user.getEmail());
		if (user.getId() == null) {
			if (foundUser != null) {
				errors.add(Messages.get("user_already_exists"));
			}
		}
		else {
			if (foundUser != null && !foundUser.getId().equals(user.getId())) {
				errors.add(Messages.get("user_already_exists"));
			}
		}
		if (StringUtil.isEmpty(user.getEmail())) {
			errors.add(Messages.get("email_is_empty"));
		}
		return errors;
	}

	@Override
	public void remove(List<Long> ids) {
		if (VosaoContext.getInstance().getUser().isAdmin()) {
			getDao().getUserGroupDao().removeByUser(ids);
			getDao().getUserDao().remove(ids);
		}
	}

	@Override
	public void forgotPassword(String email) {
		UserEntity user = getDao().getUserDao().getByEmail(email);
		if (user == null) {
			return;
		}
		String key = HashUtil.getMD5(email 
				+ String.valueOf((new Date()).getTime()));
		user.setForgotPasswordKey(key);
		getDao().getUserDao().save(user);
		String template = "";
		try {
			template = StreamUtil.getTextResource(
					"org/vosao/resources/html/forgot-letter.html");
		}
		catch (IOException e) {
			logger.error(e.getMessage());
			return;
		}
		ConfigEntity config = getDao().getConfigDao().getConfig();
		VelocityContext context = new VelocityContext();
		context.put("user", user);
		context.put("config", config);
		context.put("key", key);
		String letter = getSystemService().render(template, context);
		String error = EmailUtil.sendEmail(letter, "Forgot password", 
				config.getSiteEmail(), "Site admin", email);
		if (error != null) {
			logger.error(error);
		}
	}
	
}
