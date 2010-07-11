package org.mapsto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Table<T> extends Serializable {

	String getName();
	
	T getById(Long id);
	
	List<T> getById(List<Long> ids);
	
	T save(T value);
	
	void put(Collection<T> values);

	void remove(T entity);
	
	void removeAll(Collection<T> entities);

	void remove(Long id);
	
	void remove(Collection<Long> ids);
	
	void removeAll();
	
	void removeSelected(Query<T> query);

	Map<Long, T> getEntites();
	
	void setEntities(Map<Long, T> entities);
	
	Query<T> newQuery();
	
	void clear();
	
	TransactionLog getTransactionLog();
	
	List<T> select();
	
	Object[] params(Object...params);
	
	void clearCache();

}
