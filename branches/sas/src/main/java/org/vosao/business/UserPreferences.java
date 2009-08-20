package org.vosao.business;

import java.io.Serializable;

public class UserPreferences implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int counter;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void incrementCounter() {
		counter++;
	}
	

}
