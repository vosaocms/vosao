package com;

import java.util.Map;

import com.google.apphosting.api.ApiProxy;

/**
 * project test environment
 */
class TestEnvironment implements ApiProxy.Environment {
    /**
     * get GAE app id
     *
     * @return app id
     */
    public String getAppId() {
        return "m2";
    }

    /**
     * get version id
     *
     * @return version
     */
    public String getVersionId() {
        return "1.0";
    }

    public void setDefaultNamespace(String s) {
    }

    public String getRequestNamespace() {
        return null;
    }

    public String getDefaultNamespace() {
        return null;
    }

    public String getAuthDomain() {
        return null;
    }

    public boolean isLoggedIn() {
        return false;
    }

    public String getEmail() {
        return null;
    }

    public boolean isAdmin() {
        return false;
    }

	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
}
