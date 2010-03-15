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

package org.vosao.plugins.register.entity;

import org.vosao.entity.BaseEntityImpl;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class RegisterConfigEntity extends BaseEntityImpl {

	private String adminEmail;
	private boolean sendConfirmAdmin;
	private boolean sendConfirmUser;
	private int clearDays;
	private String registerFormTemplate;
	private String confirmUserTemplate;
	private String confirmAdminTemplate;
	private boolean captcha;

	public RegisterConfigEntity() {
		registerFormTemplate = "";
		confirmUserTemplate = "";
		confirmAdminTemplate = "";
		captcha = false;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		adminEmail = getStringProperty(entity, "adminEmail");
		sendConfirmAdmin = getBooleanProperty(entity, "sendConfirmAdmin", false);
		sendConfirmUser = getBooleanProperty(entity, "sendConfirmUser", false);
		clearDays = getIntegerProperty(entity, "clearDays", 10);
		registerFormTemplate = getTextProperty(entity, "registerFormTemplate");
		confirmUserTemplate = getTextProperty(entity, "confirmUserTemplate");
		confirmAdminTemplate = getTextProperty(entity, "confirmAdminTemplate");
		captcha = getBooleanProperty(entity, "captcha", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("adminEmail", adminEmail);
		entity.setProperty("sendConfirmAdmin", sendConfirmAdmin);
		entity.setProperty("sendConfirmUser", sendConfirmUser);
		entity.setProperty("clearDays", clearDays);
		entity.setProperty("registerFormTemplate", new Text(registerFormTemplate));
		entity.setProperty("confirmUserTemplate", new Text(confirmUserTemplate));
		entity.setProperty("confirmAdminTemplate", new Text(confirmAdminTemplate));
		entity.setProperty("captcha", captcha);
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String value) {
		this.adminEmail = value;
	}

	public boolean isSendConfirmAdmin() {
		return sendConfirmAdmin;
	}

	public void setSendConfirmAdmin(boolean sendConfirmAdmin) {
		this.sendConfirmAdmin = sendConfirmAdmin;
	}

	public boolean isSendConfirmUser() {
		return sendConfirmUser;
	}

	public void setSendConfirmUser(boolean sendConfirmUser) {
		this.sendConfirmUser = sendConfirmUser;
	}

	public int getClearDays() {
		return clearDays;
	}

	public void setClearDays(int clearDays) {
		this.clearDays = clearDays;
	}

	public String getRegisterFormTemplate() {
		return registerFormTemplate;
	}

	public void setRegisterFormTemplate(String registerFormTemplate) {
		this.registerFormTemplate = registerFormTemplate;
	}

	public String getConfirmUserTemplate() {
		return confirmUserTemplate;
	}

	public void setConfirmUserTemplate(String confirmUserTemplate) {
		this.confirmUserTemplate = confirmUserTemplate;
	}

	public String getConfirmAdminTemplate() {
		return confirmAdminTemplate;
	}

	public void setConfirmAdminTemplate(String confirmAdminTemplate) {
		this.confirmAdminTemplate = confirmAdminTemplate;
	}

	public boolean isCaptcha() {
		return captcha;
	}

	public void setCaptcha(boolean captcha) {
		this.captcha = captcha;
	}

}
