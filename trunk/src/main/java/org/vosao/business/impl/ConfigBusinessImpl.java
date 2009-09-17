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
	
	
}
