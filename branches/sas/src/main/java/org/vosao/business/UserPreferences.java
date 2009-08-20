package org.vosao.business;

import java.io.Serializable;

import org.vosao.jsf.PageBeanSession;

public class UserPreferences implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int counter;
	
	private PageBeanSession pageBeanSession; 

	public UserPreferences() {
		pageBeanSession = new PageBeanSession();
	}
	
	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void incrementCounter() {
		counter++;
	}

	public PageBeanSession getPageBeanSession() {
		return pageBeanSession;
	}

	public void setPageBeanSession(PageBeanSession pageBeanSession) {
		this.pageBeanSession = pageBeanSession;
	}
	

}
