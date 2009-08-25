package org.vosao.business;

import java.io.Serializable;

import org.vosao.entity.UserEntity;

public class UserPreferences implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int counter;
	private UserEntity user;
	
	public UserPreferences() {
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

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public boolean isLoggedIn() {
		return user != null;
	}
	
}
