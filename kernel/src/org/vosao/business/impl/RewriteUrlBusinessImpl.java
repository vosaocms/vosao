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

package org.vosao.business.impl;

import java.util.HashMap;
import java.util.Map;

import org.vosao.business.RewriteUrlBusiness;

public class RewriteUrlBusinessImpl extends AbstractBusinessImpl 
	implements RewriteUrlBusiness {

	private Map<String,String> rules = new HashMap<String, String>();
	
	@Override
	public void addRule(String from, String to) {
		rules.put(from, to);
	}

	@Override
	public String rewrite(String url) {
		String newUrl = null;
		for (String from : rules.keySet()) {
			if (url.matches(from)) {
				newUrl = url.replaceAll(from, rules.get(from));
			}
		}
		return newUrl != null ? newUrl : url;
	}

	@Override
	public void addRules(Map<String, String> rules) {
		this.rules.putAll(rules);
	}

	@Override
	public void removeRules(Map<String, String> rules) {
		for (String key : rules.keySet()) {
			this.rules.remove(key);
		}
	}
	
}
