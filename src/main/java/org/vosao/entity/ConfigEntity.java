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
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ConfigEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String GOOGLE_ANALYTICS_ID_PARAM = "googleAnalyticsId";
	public static final String SITE_EMAIL = "siteEmail";
	public static final String SITE_DOMAIN = "siteDomain";
	public static final String EDIT_EXT = "editExt";
	public static final String RECAPTCHA_PRIVATE_KEY = "recaptchaPrivateKey";
	public static final String RECAPTCHA_PUBLIC_KEY = "recaptchaPublicKey";
	public static final String COMMENTS_EMAIL = "commentsEmail";
	public static final String COMMENTS_TEMPLATE = "commentsTemplate";
	public static final String FORM_TEMPLATE = "formTemplate";

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
	private String recaptchaPrivateKey;

	@Persistent
	private String recaptchaPublicKey;

	@Persistent
	private String commentsEmail;

	@Persistent(defaultFetchGroup = "true")
	private Text commentsTemplate;
	
	@Persistent(defaultFetchGroup = "true")
	private Text formTemplate;

	public ConfigEntity() {
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
		setFormTemplate(entity.getFormTemplate());
	}
	
	/**
	 * Get all configs with values.
	 * @return configs with not null values.
	 */
	public Map<String, String> getConfigMap() {
		Map<String, String> result = new HashMap<String, String>();
		result.put(COMMENTS_EMAIL, getNotNull(getCommentsEmail()));
		result.put(COMMENTS_TEMPLATE, getNotNull(getCommentsTemplate()));
		result.put(EDIT_EXT, getNotNull(getEditExt()));
		result.put(GOOGLE_ANALYTICS_ID_PARAM, getNotNull(getGoogleAnalyticsId()));
		result.put(RECAPTCHA_PRIVATE_KEY, getNotNull(getRecaptchaPrivateKey()));
		result.put(RECAPTCHA_PUBLIC_KEY, getNotNull(getRecaptchaPublicKey()));
		result.put(SITE_DOMAIN, getNotNull(getSiteDomain()));
		result.put(SITE_EMAIL, getNotNull(getSiteEmail()));
		result.put(FORM_TEMPLATE, getNotNull(getFormTemplate()));
		return result;
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
	
	public String getFormTemplate() {
		if (formTemplate == null) {
			return null;
		}
		return formTemplate.getValue();
	}

	public void setFormTemplate(String formTemplate) {
		this.formTemplate = new Text(formTemplate);
	}
	
}
