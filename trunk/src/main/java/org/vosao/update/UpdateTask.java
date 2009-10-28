package org.vosao.update;

public interface UpdateTask {

	String getFromVersion();

	String getToVersion();
	
	void update() throws UpdateException;
	
}
