package org.mapsto.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapsto.Journal;
import org.mapsto.Mapsto;
import org.mapsto.Table;
import org.mapsto.TransactionLog;

public class MapstoImpl implements Mapsto {

	private Map<String, Table> tables;
	
	public MapstoImpl() {
		tables = new HashMap<String, Table>();
	}
	
	@Override
	public void register(Table... aTables) {
		for (Table table : aTables) {
			tables.put(table.getName(), table);
		}
	}

	@Override
	public void clearJournal() {
		for (Table table : tables.values()) {
			table.getTransactionLog().clear();
		}
	}

	@Override
	public Journal getJournal() {
		Journal result = new Journal();
		for (Table table : tables.values()) {
			result.put(table.getName(), table.getTransactionLog());
		}
		return result;
	}

	@Override
	public void apply(Journal journal) {
		for (Table table : tables.values()) {
			if (journal.contains(table.getName())) {
				table.put(journal.get(table.getName()).getUpdated());
				table.remove(journal.get(table.getName()).getRemoved());
			}
		}
	}

	@Override
	public Table getTable(String name) {
		return tables.get(name);
	}

	@Override
	public Map<String, Table> getTables() {
		return tables;
	}

	@Override
	public void setTables(Map<String, Table> values) {
		for (String name : values.keySet()) {
			if (tables.containsKey(name)) {
				tables.get(name).setEntities(values.get(name).getEntites());
			}
		}
	}

	@Override
	public boolean isChanged() {
		for (TransactionLog log : getJournal().getLogs().values()) {
			if (!log.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		for (Table table : tables.values()) {
			table.clear();
		}
	}

}
