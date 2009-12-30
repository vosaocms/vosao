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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ConfigEntity implements BaseEntity {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String googleAnalyticsId;

	@Persistent
	private String siteEmail;

	@Persistent
	private String siteDomain;
	
	@Persistent
	private String editExt;

	@Persistent
	private boolean enableRecaptcha;

	@Persistent
	private String recaptchaPrivateKey;

	@Persistent
	private String recaptchaPublicKey;

	@Persistent
	private String commentsEmail;

	@Persistent(defaultFetchGroup = "true")
	private Text commentsTemplate;
	
	@Persistent
	private String version;

	@Persistent
	private String siteUserLoginUrl;

	public ConfigEntity() {
	}
	
	@Override
	public Object getEntityId() {
		return id;
	}

	public void copy(final ConfigEntity entity) {
		setCommentsEmail(entity.getCommentsEmail());
		setCommentsTemplate(entity.getCommentsTemplate());
		setEditExt(entity.getEditExt());
		setGoogleAnalyticsId(entity.getGoogleAnalyticsId());
		setRecaptchaPrivateKey(entity.getRecaptchaPrivateKey());
		setRecaptchaPublicKey(entity.getRecaptchaPublicKey());
		setSiteDomain(entity.getSiteDomain());
		setSiteEmail(entity.getSiteEmail());
		setEnableRecaptcha(entity.isEnableRecaptcha());
		setVersion(entity.getVersion());
		setSiteUserLoginUrl(entity.getSiteUserLoginUrl());
	}
	
	private String getNotNull(String value) {
		if (value != null) {
			return value;
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
		if (commentsTemplate == null) {
			return null;
		}
		return commentsTemplate.getValue();
	}

	public void setCommentsTemplate(String commentsTemplate) {
		this.commentsTemplate = new Text(commentsTemplate);
	}

	public boolean equals(Object object) {
		if (object instanceof ConfigEntity) {
			ConfigEntity entity = (ConfigEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
	
}
