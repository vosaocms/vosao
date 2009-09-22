package org.vosao.business;


public interface ConfigBusiness {

	String getGoogleAnalyticsId();
	void setGoogleAnalyticsId(final String id);

	String getSiteEmail();
	void setSiteEmail(final String email);

	String getSiteDomain();
	void setSiteDomain(final String domain);
	
	String getEditExt();
	void setEditExt(final String value);
}
