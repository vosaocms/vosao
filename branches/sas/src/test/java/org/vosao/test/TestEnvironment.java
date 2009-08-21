package org.vosao.test;

import java.util.HashMap;
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
        return "Unit Tests";
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
        return "";
    }

    public String getDefaultNamespace() {
        return "";
    }

    public String getAuthDomain() {
        return "gmail.com";
    }

    public boolean isLoggedIn() {
        return false;
    }

    public String getEmail() {
        return "gmail.com";
    }

    public boolean isAdmin() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> map = new HashMap<String, Object> ();
        return map;
    } 	
}

