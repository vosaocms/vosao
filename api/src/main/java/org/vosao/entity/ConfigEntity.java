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

import static org.vosao.utils.EntityUtil.getBooleanProperty;
import static org.vosao.utils.EntityUtil.getStringProperty;
import static org.vosao.utils.EntityUtil.getTextProperty;
import static org.vosao.utils.EntityUtil.setProperty;
import static org.vosao.utils.EntityUtil.setTextProperty;

import com.google.appengine.api.datastore.Entity;

public class ConfigEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 2L;

	private String googleAnalyticsId;
	private String siteEmail;
	private String siteDomain;
	private String editExt;
	private boolean enableRecaptcha;
	private String recaptchaPrivateKey;
	private String recaptchaPublicKey;
	private String commentsEmail;
	private String commentsTemplate;
	private String version;
	private String siteUserLoginUrl;
	private boolean enablePicasa;
	private String picasaUser;
	private String picasaPassword;

	public ConfigEntity() {
		commentsTemplate = "";
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		googleAnalyticsId = getStringProperty(entity, "googleAnalyticsId");
		siteEmail = getStringProperty(entity, "siteEmail");
		siteDomain = getStringProperty(entity, "siteDomain");
		editExt = getStringProperty(entity, "editExt");
		enableRecaptcha = getBooleanProperty(entity, "enableRecaptcha", false);
		recaptchaPrivateKey = getStringProperty(entity, "recaptchaPrivateKey");
		recaptchaPublicKey = getStringProperty(entity, "recaptchaPublicKey");
		commentsEmail = getStringProperty(entity, "commentsEmail");
		commentsTemplate = getTextProperty(entity, "commentsTemplate");
		version = getStringProperty(entity, "version");
		siteUserLoginUrl = getStringProperty(entity, "siteUserLoginUrl");
		enablePicasa = getBooleanProperty(entity, "enablePicasa", false);
		picasaUser = getStringProperty(entity, "picasaUser");
		picasaPassword = getStringProperty(entity, "picasaPassword");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "googleAnalyticsId", googleAnalyticsId, false);
		setProperty(entity, "siteEmail", siteEmail, false);
		setProperty(entity, "siteDomain", siteDomain, false);
		setProperty(entity, "editExt", editExt, false);
		setProperty(entity, "enableRecaptcha", enableRecaptcha, false);
		setProperty(entity, "recaptchaPrivateKey", recaptchaPrivateKey, false);
		setProperty(entity, "recaptchaPublicKey", recaptchaPublicKey, false);
		setProperty(entity, "commentsEmail", commentsEmail, false);
		setTextProperty(entity, "commentsTemplate", commentsTemplate);
		setProperty(entity, "version", version, false);
		setProperty(entity, "siteUserLoginUrl", siteUserLoginUrl, false);
		setProperty(entity, "enablePicasa", enablePicasa, false);
		setProperty(entity, "picasaUser", picasaUser, false);
		setProperty(entity, "picasaPassword", picasaPassword, false);
	}

	public String getGoogleAnalyticsId() {
		return googleAnalyticsId;
	}

	public void setGoogleAnalyticsId(String googleAnalyticsId) {
		this.googleAnalyticsId = googleAnalyticsId;
	}

	public String getSiteEmail() {
		return siteEmail;
	}

	public void setSiteEmail(String siteEmail) {
		this.siteEmail = siteEmail;
	}

	public String getSiteDomain() {
		return siteDomain;
	}

	public void setSiteDomain(String siteDomain) {
		this.siteDomain = siteDomain;
	}

	public String getEditExt() {
		return editExt;
	}

	public void setEditExt(String editExt) {
		this.editExt = editExt;
	}

	public String getRecaptchaPrivateKey() {
		return recaptchaPrivateKey;
	}

	public void setRecaptchaPrivateKey(String recaptchaPrivateKey) {
		this.recaptchaPrivateKey = recaptchaPrivateKey;
	}

	public String getRecaptchaPublicKey() {
		return recaptchaPublicKey;
	}

	public void setRecaptchaPublicKey(String recaptchaPublicKey) {
		this.recaptchaPublicKey = recaptchaPublicKey;
	}

	public String getCommentsEmail() {
		return commentsEmail;
	}

	public void setCommentsEmail(String commentsEmail) {
		this.commentsEmail = commentsEmail;
	}

	public String getCommentsTemplate() {
		return commentsTemplate;
	}

	public void setCommentsTemplate(String commentsTemplate) {
		this.commentsTemplate = commentsTemplate;
	}

	public boolean isEnableRecaptcha() {
		return enableRecaptcha;
	}

	public void setEnableRecaptcha(boolean enableRecaptcha) {
		this.enableRecaptcha = enableRecaptcha;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSiteUserLoginUrl() {
		return siteUserLoginUrl;
	}

	public void setSiteUserLoginUrl(String siteUserLoginUrl) {
		this.siteUserLoginUrl = siteUserLoginUrl;
	}

	public boolean isEnablePicasa() {
		return enablePicasa;
	}

	public void setEnablePicasa(boolean enablePicasa) {
		this.enablePicasa = enablePicasa;
	}

	public String getPicasaUser() {
		return picasaUser;
	}

	public void setPicasaUser(String picasaUser) {
		this.picasaUser = picasaUser;
	}

	public String getPicasaPassword() {
		return picasaPassword;
	}

	public void setPicasaPassword(String picasaPassword) {
		this.picasaPassword = picasaPassword;
	}
	
}
