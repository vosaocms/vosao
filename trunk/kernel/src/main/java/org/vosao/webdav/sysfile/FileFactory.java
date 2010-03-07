package org.vosao.webdav.sysfile;

public interface FileFactory {

	boolean isCorrectPath(String path);
	
	String getFile(String path);
	
	void setFile(String path, String xml);
	
}
