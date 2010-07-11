package org.mapsto;

import java.util.List;

public interface Query<T> {

	Query<T> addFilter(String field, int operator, Object value);
	
	Query<T> addFilter(Filter<T> filter);

	T selectOne();
	
	T selectOne(String name, Object[] params);

	List<T> select();

	List<T> select(String name, Object[] params);
}
