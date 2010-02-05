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

import org.dom4j.Element;

public class XmlUtil {

	public static boolean readBooleanAttr(Element e, String name, 
			boolean defaultValue) {
		String value = e.attributeValue(name);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static int readIntAttr(Element e, String name, 
			int defaultValue) {
		String value = e.attributeValue(name);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	public static boolean readBooleanText(Element e, boolean defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static int readIntegerText(Element e, int defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	public static Long readLongText(Element e, Long defaultValue) {
		String value = e.getText();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.valueOf(value);
		}
		catch (Exception ex) {
			return defaultValue;
		}
	}

	public static String notNull(String s) {
		if (s == null) return "";
		return s;
	}
	
}
