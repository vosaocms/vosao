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

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class StrUtil {
	
	private static String _toCSV(Collection<String> list) {
		StringBuffer result = new StringBuffer();
		int count = 0;
		for (String item : list) {
			result.append((count == 0 ? "" : ",")).append(item);
			count++;
		}
		return result.toString();
	}

	public static String toCSV(Set<String> list) {
		return _toCSV(list);
	}
	
	public static String toCSV(List<String> list) {
		return _toCSV(list);
	}
}
