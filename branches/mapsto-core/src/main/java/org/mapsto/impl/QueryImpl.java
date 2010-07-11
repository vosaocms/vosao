package org.mapsto.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.mapsto.Filter;
import org.mapsto.Query;
import org.mapsto.Table;

public class QueryImpl<T> implements Query<T> {

	private Table<T> table;
	private List<Filter> filters;
	
	public QueryImpl(Table<T> table) {
		this.table = table;
		filters = new ArrayList<Filter>();
	}
	
	public Query<T> addFilter(String field, int operator, Object value) {
		filters.add(new FilterImpl(field, operator, value));
		return this;
	}
	
	public T selectOne() {
		Map<Long, T> result = table.getEntites();
		for (Filter<T> filter : filters) {
			result = applyFilter(result, filter);
		}
		if (!result.isEmpty()) {
			return result.values().iterator().next();
		}
		return null;
	}
	
	public List<T> select() {
		Map<Long, T> result = table.getEntites();
		for (Filter<T> filter : filters) {
			result = applyFilter(result, filter);
		}
		return new ArrayList(result.values());
	}

	private Map<Long, T> applyFilter(Map<Long, T> entities, Filter<T> filter) {
		Map<Long, T> result = new HashMap<Long, T>();
		for (Long id : entities.keySet()) {
			T entity = entities.get(id);
			if (filter.check(entity)) {
				result.put(id, entity);
			}
		}
		return result;
	}

	@Override
	public Query<T> addFilter(Filter<T> filter) {
		filters.add(filter);
		return this;
	}

	@Override
	public List<T> select(String name, Object[] params) {
		return select();
	}

	@Override
	public T selectOne(String name, Object[] params) {
		return selectOne();
	}
}
