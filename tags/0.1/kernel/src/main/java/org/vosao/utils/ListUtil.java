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
	
}
