package org.vosao.global;

import javax.cache.Cache;

public interface CacheService extends Cache {

	void resetLocalCache();
	
	int getLocalHits();

	int getCacheHits();
	
	Cache getMemcache();
	
	/**
	 * Save in cache big object. Big object can have size more than 1 MB. 
	 * (Google limitation for objects stored in cache
	 * http://code.google.com/appengine/docs/python/memcache/overview.html#Quotas_and_Limits)
	 * @param key
	 * @param data
	 */
	void putBlob(String key, byte[] data);

	/**
	 * Get big object from cache. Big object can have size more than 1 MB. 
	 * (Google limitation for objects stored in cache
	 * http://code.google.com/appengine/docs/python/memcache/overview.html#Quotas_and_Limits)
	 * @param key
	 */
	byte[] getBlob(String key);
}
