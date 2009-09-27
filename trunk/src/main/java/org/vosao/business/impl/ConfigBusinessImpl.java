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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.ConfigBusiness;
import org.vosao.entity.ConfigEntity;

public class ConfigBusinessImpl extends AbstractBusinessImpl 
	implements ConfigBusiness {

	private static final Log logger = LogFactory.getLog(ConfigBusinessImpl.class);

	private static final String GOOGLE_ANALYTICS_ID_PARAM = "googleAnalyticsId";
	private static final String SITE_EMAIL_PARAM = "siteEmail";
	private static final String SITE_DOMAIN_PARAM = "siteDomain";
	private static final String EDIT_EXT = "editExt";
	private static final String RECAPTCHA_PRIVATE_KEY = "recaptchaPrivateKey";
	private static final String RECAPTCHA_PUBLIC_KEY = "recaptchaPublicKey";
	
	private String getConfigParam(final String name) {
		ConfigEntity config = getDao().getConfigDao().getByName(name);
		if (config != null) {
			return config.getValue();
		}
		return null;
	}

	private void setConfigParam(final String name, final String value) {
		ConfigEntity config = getDao().getConfigDao().getByName(name);
		if (config == null) {
			config = new ConfigEntity(name, value);
		}
		else {
			config.setValue(value);
		}
		getDao().getConfigDao().save(config);
	}

	@Override
	public String getGoogleAnalyticsId() {
		return getConfigParam(GOOGLE_ANALYTICS_ID_PARAM);
	}

	@Override
	public void setGoogleAnalyticsId(String id) {
		setConfigParam(GOOGLE_ANALYTICS_ID_PARAM, id);
	}

	@Override
	public String getSiteEmail() {
		return getConfigParam(SITE_EMAIL_PARAM);
	}

	@Override
	public void setSiteEmail(String email) {
		setConfigParam(SITE_EMAIL_PARAM, email);
	}

	@Override
	public String getSiteDomain() {
		return getConfigParam(SITE_DOMAIN_PARAM);
	}

	@Override
	public void setSiteDomain(String domain) {
		setConfigParam(SITE_DOMAIN_PARAM, domain);
	}

	@Override
	public String getEditExt() {
		return getConfigParam(EDIT_EXT);
	}

	@Override
	public void setEditExt(String value) {
		setConfigParam(EDIT_EXT, value);
	}

	@Override
	public String getRecaptchaPrivateKey() {
		return getConfigParam(RECAPTCHA_PRIVATE_KEY);
	}

	@Override
	public void setRecaptchaPrivateKey(String value) {
		setConfigParam(RECAPTCHA_PRIVATE_KEY, value);
	}

	@Override
	public String getRecaptchaPublicKey() {
		return getConfigParam(RECAPTCHA_PUBLIC_KEY);
	}

	@Override
	public void setRecaptchaPublicKey(String value) {
		setConfigParam(RECAPTCHA_PUBLIC_KEY, value);
	}
	
	
}
