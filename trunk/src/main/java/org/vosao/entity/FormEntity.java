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

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FormEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String title;

	@Persistent
	private String email;

	@Persistent
	private String letterSubject;

	@Persistent
	private String sendButtonTitle;

	@Persistent
	private boolean showResetButton;

	@Persistent
	private String resetButtonTitle;

	@Persistent
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
	
	public void copy(final FormEntity entity) {
		setTitle(entity.getTitle());
		setName(entity.getName());
		setEmail(entity.getEmail());
		setLetterSubject(entity.getLetterSubject());
		setSendButtonTitle(entity.getSendButtonTitle());
		setShowResetButton(entity.isShowResetButton());
		setResetButtonTitle(entity.getResetButtonTitle());
		setEnableCaptcha(entity.isEnableCaptcha());
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

	public boolean equals(Object object) {
		if (object instanceof FormEntity) {
			FormEntity entity = (FormEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
