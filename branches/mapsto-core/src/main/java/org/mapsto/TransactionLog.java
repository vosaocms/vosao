package org.mapsto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface TransactionLog<T> extends Serializable {

	void clear();
	
	Set<T> getUpdated();
	
	Set<Long> getRemoved();
	
	void addUpdated(T entity);

	void addUpdated(Collection<T> entities);

	void addRemoved(Long id);

	void addRemoved(Collection<Long> ids);
	
	boolean isEmpty();
}
