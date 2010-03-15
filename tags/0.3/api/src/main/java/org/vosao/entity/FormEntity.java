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

package org.vosao.entity;

import com.google.appengine.api.datastore.Entity;


public class FormEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 1L;

	private String name;
	private String title;
	private String email;
	private String letterSubject;
	private String sendButtonTitle;
	private boolean showResetButton;
	private String resetButtonTitle;
	private boolean enableCaptcha;

	public FormEntity() {
	}
	
	public FormEntity(String aName, String aEmail, String aTitle, 
			String aSubject) {
		this();
		name = aName;
		email = aEmail;
		title = aTitle;
		letterSubject = aSubject;
	}
	
	@Override
	public void load(Entity entity) {
		super.load(entity);
		name = getStringProperty(entity, "name");
		email = getStringProperty(entity, "email");
		title = getStringProperty(entity, "title");
		letterSubject = getStringProperty(entity, "letterSubject");
		sendButtonTitle = getStringProperty(entity, "sendButtonTitle");
		resetButtonTitle = getStringProperty(entity, "resetButtonTitle");
		showResetButton = getBooleanProperty(entity, "showResetButton", false);
		enableCaptcha = getBooleanProperty(entity, "enableCaptcha", false);
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("name", name);
		entity.setProperty("email", email);
		entity.setProperty("title", title);
		entity.setProperty("letterSubject", letterSubject);
		entity.setProperty("sendButtonTitle", sendButtonTitle);
		entity.setProperty("resetButtonTitle", resetButtonTitle);
		entity.setProperty("showResetButton", showResetButton);
		entity.setProperty("enableCaptcha", enableCaptcha);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLetterSubject() {
		return letterSubject;
	}

	public void setLetterSubject(String letterSubject) {
		this.letterSubject = letterSubject;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSendButtonTitle() {
		return sendButtonTitle;
	}

	public void setSendButtonTitle(String sendButtonTitle) {
		this.sendButtonTitle = sendButtonTitle;
	}

	public boolean isShowResetButton() {
		return showResetButton;
	}

	public void setShowResetButton(boolean showResetButton) {
		this.showResetButton = showResetButton;
	}

	public String getResetButtonTitle() {
		return resetButtonTitle;
	}

	public void setResetButtonTitle(String resetButtonTitle) {
		this.resetButtonTitle = resetButtonTitle;
	}

	public boolean isEnableCaptcha() {
		return enableCaptcha;
	}

	public void setEnableCaptcha(boolean enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
	
}
