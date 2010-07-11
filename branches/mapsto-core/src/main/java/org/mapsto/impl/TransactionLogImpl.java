package org.mapsto.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.mapsto.TransactionLog;

public class TransactionLogImpl<T> implements TransactionLog<T> {

	private Set<T> updated;
	private Set<Long> removed;
	
	public TransactionLogImpl() {
		updated = new HashSet<T>();
		removed = new HashSet<Long>();
	}
	
	@Override
	public void clear() {
		updated.clear();
		removed.clear();
	}

	@Override
	public Set<T> getUpdated() {
		return updated;
	}

	@Override
	public Set<Long> getRemoved() {
		return removed;
	}

	@Override
	public void addUpdated(T entity) {
		updated.add(entity);
	}

	@Override
	public void addRemoved(Long id) {
		removed.add(id);
	}

	@Override
	public void addUpdated(Collection<T> entities) {
		updated.addAll(entities);		
	}

	@Override
	public void addRemoved(Collection<Long> ids) {
		removed.addAll(ids);
	}

	@Override
	public boolean isEmpty() {
		return updated.isEmpty() && removed.isEmpty();
	}

}
