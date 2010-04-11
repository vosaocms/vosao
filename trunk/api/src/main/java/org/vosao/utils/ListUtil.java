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
