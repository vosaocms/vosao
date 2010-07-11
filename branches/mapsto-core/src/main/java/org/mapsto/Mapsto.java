package org.mapsto;

import java.util.Map;

public interface Mapsto {

	void register(Table... table);
	
	Table getTable(String name);
	
	Map<String, Table> getTables();
	
	void setTables(Map<String, Table> tables);
	
	void clearJournal();
	
	void clear();
	
	Journal getJournal();

	void apply(Journal journal);
	
	boolean isChanged();
}
