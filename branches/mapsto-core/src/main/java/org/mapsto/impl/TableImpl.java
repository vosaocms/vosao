package org.mapsto.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapsto.Entity;
import org.mapsto.Query;
import org.mapsto.Table;
import org.mapsto.TransactionLog;

public class TableImpl<T extends Entity> implements Table<T> {

	private String name;
	private Map<Long, T> entities;
	private TransactionLog<T> transactionLog;
	
	public TableImpl(String name) {
		transactionLog = new TransactionLogImpl<T>();
		entities = new HashMap<Long, T>();
		this.name = name;
	}
	
	@Override
	public T getById(Long id) {
		return entities.get(id);
	}

	@Override
	public T save(T value) {
		if (value.getId() == null) {
			value.setId(nextValue());
		}
		entities.put(value.getId(), value);
		transactionLog.addUpdated(value);
		return value;
	}

	private long nextValue() {
		return ((System.currentTimeMillis() >> 4) << 8) | 
				(System.nanoTime() & 0xff);
	}

	@Override
	public Query<T> newQuery() {
		return new QueryImpl<T>(this);
	}

	@Override
	public Map<Long, T> getEntites() {
		return entities;
	}

	@Override
	public void clear() {
		entities.clear();		
	}

	@Override
	public void remove(T entity) {
		if (entities.containsKey(entity.getId())) {
			transactionLog.addRemoved(entity.getId());
			entities.remove(entity.getId());
		}
	}

	@Override
	public void removeAll(Collection<T> entities) {
		for (T entity : entities) {
			remove(entity);
		}
	}

	@Override
	public List<T> getById(List<Long> ids) {
		if (ids == null) {
			return Collections.EMPTY_LIST;
		}
		List<T> result = new ArrayList<T>();
		for (Long id : ids) {
			if (getById(id) != null) {
				result.add(getById(id));
			}
		}
		return result;
	}

	@Override
	public TransactionLog getTransactionLog() {
		return transactionLog;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void put(Collection<T> values) {
		for (T entity : values) {
			save(entity);
		}
		
	}

	@Override
	public void remove(Long id) {
		if (entities.containsKey(id)) {
			transactionLog.addRemoved(id);
			entities.remove(id);
		}
	}

	@Override
	public void remove(Collection<Long> ids) {
		for (Long id : ids) {
			remove(id);
		}
	}

	@Override
	public void removeAll() {
		for (Long id : entities.keySet()) {
			transactionLog.addRemoved(id);
		}
		entities.clear();
	}

	@Override
	public List<T> select() {
		return newQuery().select();
	}

	@Override
	public void setEntities(Map<Long, T> entities) {
		this.entities = entities;	
	}

	@Override
	public Object[] params(Object... params) {
		return params;
	}

	@Override
	public void removeSelected(Query<T> query) {
		for (T entity : query.select()) {
			remove(entity);
		}
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		
	}
}
