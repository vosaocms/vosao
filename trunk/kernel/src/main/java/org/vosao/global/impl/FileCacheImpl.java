package org.vosao.global.impl;

import javax.cache.Cache;

import org.vosao.entity.FileEntity;
import org.vosao.global.FileCache;

public class FileCacheImpl implements FileCache {

	private Cache cache;
	
	public FileCacheImpl(Cache cache) {
		this.cache = cache;
	}
	
	@Override
	public byte[] getContent(String path) {
		return (byte[])cache.get("data:" + path);
	}

	@Override
	public FileEntity getFile(String path) {
		return (FileEntity)cache.get(path);
	}

	@Override
	public boolean isInCache(String path) {
		return cache.containsKey(path) && cache.containsKey("data:" + path);
	}

	@Override
	public boolean isInPublicCache(String path) {
		return isInCache(path) && cache.containsKey("public:" + path);
	}

	@Override
	public void put(String path, FileEntity file, byte[] content) {
		cache.put(path, file);
		cache.put("data:" + path, content);
	}

	@Override
	public void makePublic(String path) {
		cache.put("public:" + path, true);
	}

	@Override
	public void remove(String path) {
		cache.remove(path);
		cache.remove("data:" + path);
		cache.remove("public:" + path);
	}

}
