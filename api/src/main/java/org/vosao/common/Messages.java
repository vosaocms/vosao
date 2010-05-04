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

package org.vosao.common;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Message bundle helper class for creatng localized messages from Java code.
 * 
 * @author Alexander Oleynik
 *
 */
public class Messages {

	public static final String JSTL_FMT_LOCALE_KEY = 
		"javax.servlet.jsp.jstl.fmt.locale.session";

	private static final String BUNDLE_BASE_NAME = 
		"org.vosao.resources.messages";
	
	private static final Locale[] supportedLocales = {
		Locale.ENGLISH, 
		new Locale("ru")};
	
	private static ResourceBundle getBundle(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Locale sessionLocale = (Locale)session.getAttribute(JSTL_FMT_LOCALE_KEY);
		Locale locale = sessionLocale != null ? sessionLocale : 
			request.getLocale();
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
	}
	
	private static ResourceBundle getDefaultBundle() {
		return ResourceBundle.getBundle(BUNDLE_BASE_NAME, Locale.ENGLISH);
	}

	public static String get(String key, Object...objects) {
		VosaoContext ctx = VosaoContext.getInstance();
		String pattern = "not found";
		if (isLocaleSupported(ctx.getLocale())) {
			pattern  = getBundle(ctx.getRequest()).getString(key);
		}
		else {
			pattern  = getDefaultBundle().getString(key);
		}
		if (objects != null) {
			MessageFormat formatter = new MessageFormat("");
			formatter.setLocale(ctx.getLocale());
			formatter.applyPattern(pattern);
			pattern = formatter.format(objects);
		}
		return pattern;
	}
	
	/**
	 * Generate JavaScript JSON message bundle for JavaScript messages 
	 * localization.
	 * @return JS file.
	 */
	public static String getJSMessages() {
		VosaoContext ctx = VosaoContext.getInstance();
		Map<String, String> messages = new HashMap<String, String>();
		ResourceBundle defaultBundle = getDefaultBundle();
		for (String key : defaultBundle.keySet()) {
			messages.put(key, defaultBundle.getString(key));
		}
		ResourceBundle bundle = getBundle(ctx.getRequest());
		for (String key : bundle.keySet()) {
			messages.put(key, bundle.getString(key));
		}
		StringBuffer result = new StringBuffer();
		result.append("locale = '").append(ctx.getLocale().toString())
				.append("';\n");
		result.append("messages = {\n");
		int i = 0;
		for (String key : messages.keySet()) {
			if (i++ > 0) {
				result.append(",");
			}
			result.append(" '").append(key).append("' : '")
				.append(filterForJS(messages.get(key)))
				.append("'\n");
		}
		result.append("};");
		return result.toString();
	}
	
	private static String filterForJS(String msg) {
		return msg.replaceAll("\n", "").replaceAll("'", "\\\\'");
	}
	
	public static boolean isLocaleSupported(Locale locale) {
		for (Locale l : supportedLocales) {
			if (l.equals(locale)) {
				return true;
			}
		}
		return false;
	}

}
