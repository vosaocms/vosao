package org.vosao.business.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.ConfigBusiness;
import org.vosao.entity.ConfigEntity;

public class ConfigBusinessImpl extends AbstractBusinessImpl 
	implements ConfigBusiness {

	private static final Log logger = LogFactory.getLog(ConfigBusinessImpl.class);

	private static final String GOOGLE_ANALYTICS_ID_PARAM = "googleAnalyticsId";
	
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
	
	
}
