/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.entity.field;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;

public class PageAttributesField implements Map<String, String>, Serializable {

	protected static final Log logger = LogFactory.getLog(
			PageAttributesField.class);

	private Map<String, Map<String, String>> data;
	
	public PageAttributesField(String attributes) {
		data = new HashMap<String, Map<String, String>>();
		if (StringUtils.isEmpty(attributes)) {
			return;
		}
		try {
			JSONObject obj = new JSONObject(attributes);
			Iterator<String> attributeIter = obj.keys();
			while (attributeIter.hasNext()) {
				String attrName = attributeIter.next();
				if (!data.containsKey(attrName)) {
					data.put(attrName, new HashMap<String, String>());
				}
				JSONObject attr = obj.getJSONObject(attrName);
				Iterator<String> langIter = attr.keys();
				while (langIter.hasNext()) {
					String language = langIter.next();
					data.get(attrName).put(language, attr.getString(language));
				}
			}
		} catch (org.json.JSONException e) {
			logger.error("Page atributes parsing problem: " + attributes);
		}
	}
	
	public void set(String name, String language, String value) {
		if (!data.containsKey(name)) {
			data.put(name, new HashMap<String, String>());
		}
		data.get(name).put(language, value);
	}
	
	public String asJSON() {
		JSONObject obj = new JSONObject(data);
		return obj.toString();
	}
	
	@Override
	public void clear() {
		// Not implemented
	}

	@Override
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// Not implemented
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return Collections.EMPTY_SET;
	}

	private static Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	@Override
	public String get(Object key) {
		if (data.containsKey(key)) {
			if (data.get(key).containsKey(getBusiness().getLanguage())) {
				return data.get(key).get(getBusiness().getLanguage());
			}
			String defaultLanguage = getBusiness().getDefaultLanguage();
			if (data.get(key).containsKey(defaultLanguage)) {
				return data.get(key).get(defaultLanguage);
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public String put(String key, String value) {
		// Not implemented
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		// Not implemented
	}

	@Override
	public String remove(Object key) {
		data.remove(key);
		return null;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public Collection<String> values() {
		// Not implemented
		return null;
	}

}
