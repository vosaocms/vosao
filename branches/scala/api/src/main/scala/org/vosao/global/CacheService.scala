package org.vosao.global

import javax.cache.Cache

trait CacheService extends Cache {

	def resetLocalCache(): Unit
	
	def getLocalHits(): Int

	def getCacheHits(): Int
	
	def getMemcache(): Cache
	
	/**
	 * Save in cache big object. Big object can have size more than 1 MB. 
	 * (Google limitation for objects stored in cache
	 * http://code.google.com/appengine/docs/python/memcache/overview.html#Quotas_and_Limits)
	 * @param key
	 * @param data
	 */
	def putBlob(key: String, data: Array[Byte]): Unit

	/**
	 * Get big object from cache. Big object can have size more than 1 MB. 
	 * (Google limitation for objects stored in cache
	 * http://code.google.com/appengine/docs/python/memcache/overview.html#Quotas_and_Limits)
	 * @param key
	 */
	def getBlob(key: String): Array[Byte]
}
