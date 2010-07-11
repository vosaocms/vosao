package org.mapsto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Journal implements Serializable {

	private Map<String, TransactionLog> logs;

	public Journal() {
		logs = new HashMap<String, TransactionLog>();
	}
	
	public Map<String, TransactionLog> getLogs() {
		return logs;
	}
	
	public void put(String name, TransactionLog log) {
		logs.put(name, log);
	}

	public boolean contains(String name) {
		return logs.containsKey(name);
	}
	
	public TransactionLog get(String name) {
		return logs.get(name);
	}
	
}
