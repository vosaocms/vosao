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

package org.vosao.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static interface Filter<T> {
		public boolean filter(T entity);
	}
	
	public static <T> List<T> filter(List<T> list, Filter<T> filter) {
		List<T> result = new ArrayList<T>();
		for (T entity : list) {
			if (filter.filter(entity)) {
				result.add(entity);
			}
		}
		return result;
	}
	
	public static <T> List<T> slice(List<T> list, int index, int count) {
		List<T> result = new ArrayList<T>();
		if (index >= 0 && index < list.size()) {
			int end = index + count < list.size() ? index + count : list.size();
			for (int i = index; i < end; i++) {
				result.add(list.get(i));
			}
		}
		return result;
	}
	
}
