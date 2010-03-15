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

package org.vosao.plugins.superfish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.utils.StrUtil;

public class SuperfishConfig {

	private Map<String, Integer> enabledPages;

	public SuperfishConfig() {
		enabledPages = new HashMap<String, Integer>();
	}
	
	public SuperfishConfig(String xml) {
		this();
		if (!StringUtils.isEmpty(xml)) {
			for (String page : xml.split(",")) {
				String[] items = page.split(":");
				try {
					enabledPages.put(items[0], Integer.valueOf(items[1]));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String toXML() {
		List<String> list = new ArrayList<String>();
		for (String key : enabledPages.keySet()) {
			list.add(key + ":" + enabledPages.get(key));
		}
		return StrUtil.toCSV(list);
	}
	
	public Map<String, Integer> getEnabledPages() {
		return enabledPages;
	}

	public void setEnabledPages(Map<String, Integer> enabledPages) {
		this.enabledPages = enabledPages;
	}
	
	
	
}
