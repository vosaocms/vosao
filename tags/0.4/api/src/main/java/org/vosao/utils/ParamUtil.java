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

package org.vosao.utils;

import java.util.Date;

import com.josephoconnell.html.HTMLInputFilter;

/**
 * @author Alexander Oleynik
 */
public class ParamUtil {

	static public Integer getInteger(final String s, 
			final Integer defaultValue) {
		try {
			return Integer.valueOf(s);
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	static public Long getLong(final String s, 
			final Long defaultValue) {
		try {
			return Long.valueOf(s);
		}
		catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	static public Boolean getBoolean(final String s, 
			final Boolean defaultValue) {
		try {
			return Boolean.valueOf(s);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Convert string to date from format dd.mm.yyyy
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	static public Date getDate(final String s, 
			final Date defaultValue) {
		try {
			return DateUtil.toDate(s);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	private static HTMLInputFilter xssFilter = new HTMLInputFilter();
	
	static public String filterXSS(String value) {
		return xssFilter.filter(value);
	}
	
}
