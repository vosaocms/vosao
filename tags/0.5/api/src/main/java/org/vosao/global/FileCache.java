package org.vosao.global;

import org.vosao.entity.FileEntity;

public interface FileCache {

	boolean isInCache(String path);
	
	boolean isInPublicCache(String path);
	
	void put(String path, FileEntity file, byte[] content);
	
	FileEntity getFile(String path);
	
	byte[] getContent(String path);
	
	void makePublic(String path);

	void remove(String path);
	
}
