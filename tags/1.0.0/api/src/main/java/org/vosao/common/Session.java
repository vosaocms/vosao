package org.vosao.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.vosao.entity.ConfigEntity;
import org.vosao.utils.CipherUtils;

public class Session {

	private static final Log logger = LogFactory.getLog(Session.class);

	private static final String SESSION_COOKIE = "vosao_session";
	private static final String SESSION_CREATED = "sc";
	private static final String SESSION_ACCESSED = "sa";

	private static final String LOCALE_COUNTRY = "localeCountry";
	private static final String LOCALE_LANGAUGE = "localeLanguage";

	// Session timeout 30 minutes
	private static final Long SESSION_TIMEOUT = 30 * 60 * 1000L;

	// Session lifetime 24 hours
	private static final Long SESSION_LIFETIME = 24 * 60 * 60 * 1000L;

	private Map<String, String> map;

	public Session(HttpServletRequest request) {
		map = new HashMap<String, String>();
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (SESSION_COOKIE.equals(cookie.getName())) {
					parseSessionData(decrypt(cookie.getValue()));
				}
			}
		}
		if (isTimeout()) {
			map.clear();
			map.put(SESSION_CREATED, String.valueOf(new Date().getTime()));
		}
		map.put(SESSION_ACCESSED, String.valueOf(new Date().getTime()));
	}

	private boolean isTimeout() {
		if (!map.containsKey(SESSION_CREATED)
				|| !map.containsKey(SESSION_ACCESSED)) {
			return true;
		}
		return Long.valueOf(map.get(SESSION_ACCESSED)) + SESSION_TIMEOUT < new Date()
				.getTime()
				|| Long.valueOf(map.get(SESSION_CREATED)) + SESSION_LIFETIME < new Date()
						.getTime();
	}

	private void parseSessionData(String value) {
		try {
			if (value == null) return;
			JSONObject obj = new JSONObject(value);
			Iterator<String> iterator = obj.keys();
			while (iterator.hasNext()) {
				String name = iterator.next();
				map.put(name, obj.getString(name));
			}
		} catch (JSONException e) {
			logger.warn("Error parsing cookie session. " + value);
		}
	}

	public String getString(String name) {
		return map.get(name);
	}

	public Long getLong(String name) {
		String value = getString(name);
		return value != null ? Long.valueOf(value) : null;
	}

	public void set(String name, String value) {
		map.put(name, value);
	}

	public void set(String name, Long value) {
		if (value != null) {
			set(name, value.toString());
		}
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void save(HttpServletResponse response) {
		Cookie cookie = new Cookie(SESSION_COOKIE, encrypt(
				new JSONObject(map).toString()));
		cookie.setPath("/");
		cookie.setMaxAge(new Long(SESSION_LIFETIME / 1000).intValue());
		response.addCookie(cookie);
	}

	public Locale getLocale() {
		String lang = getString(LOCALE_LANGAUGE);
		String country = getString(LOCALE_COUNTRY);
		return lang != null && country != null ? new Locale(lang, country)
				: null;
	}

	public void setLocale(Locale locale) {
		set(LOCALE_COUNTRY, locale.getCountry());
		set(LOCALE_LANGAUGE, locale.getLanguage());
	}

	public String encrypt(String data) {
		VosaoContext ctx = VosaoContext.getInstance();
		if (ctx.getConfig() == null) return null;
		String password = ctx.getConfig().getSessionKey();
		if (password == null) {
			ConfigEntity config = ctx.getConfig();
			password = CipherUtils.generateKey();
			config.setSessionKey(password);
			ctx.getBusiness().getDao().getConfigDao().save(config);
		}
		return CipherUtils.encrypt(data, password);
	}
	
	public String decrypt(String data) {
		VosaoContext ctx = VosaoContext.getInstance();
		if (ctx.getConfig() == null) return null;
		String password = ctx.getConfig().getSessionKey();
		if (password == null) {
			ConfigEntity config = ctx.getConfig();
			password = CipherUtils.generateKey();
			config.setSessionKey(password);
			ctx.getBusiness().getDao().getConfigDao().save(config);
		}
		return CipherUtils.decrypt(data, password);
	}

	
}
