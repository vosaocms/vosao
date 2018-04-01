/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.global.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.cache.Cache;
import javax.cache.CacheEntry;
import javax.cache.CacheException;
import javax.cache.CacheListener;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.global.CacheService;
import org.vosao.utils.ArrayUtil;
import org.vosao.utils.StrUtil;

import com.google.appengine.api.memcache.InvalidValueException;

public class CacheServiceImpl implements CacheService {

	private static final Log log = LogFactory.getLog(CacheServiceImpl.class);

	private static final long LOCAL_CACHE_TTL = 5000;
	private static final String RESET_DATE_KEY = "cacheResetDate";
	
	private Cache cache;
	private Map<String, Object> localCache;
	private long localCacheTime;
	private int localHits;
	private int cacheHits;

	public CacheServiceImpl() {
		try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(
            		Collections.emptyMap());
        } catch (CacheException e) {
            log.error("Can't init cache manager. " + e.getMessage());
        }
        localCache = new HashMap<String, Object>();
        localCacheTime = System.currentTimeMillis();
	}
	
	@Override
	public void resetLocalCache() {
		if (System.currentTimeMillis() - localCacheTime > LOCAL_CACHE_TTL) {
			localCache.clear(); 
	        localCacheTime = System.currentTimeMillis();
		}
	}

	@Override
	public void addListener(CacheListener arg0) {
		log.error("addListener(CacheListener arg0) not implemented");		
	}

	@Override
	public void evict() {
		log.error("evict() not implemented");		
	}

	@Override
	public Map getAll(Collection keys) {
		Map result = new HashMap();
		List memcacheKeys = new ArrayList();
		for (Object key : keys) {
			if (localCache.containsKey(key)) {
				result.put(key, localCache.get(key));
			}
			else {
				memcacheKeys.add(key);
			}
		}
		result.putAll(cache.getAll(memcacheKeys));
		return result;
	}

	@Override
	public CacheEntry getCacheEntry(Object arg0) {
		log.error("getCacheEntry(Object arg0) not implemented");		
		return null;
	}

	@Override
	public CacheStatistics getCacheStatistics() {
		log.error("getCacheStatistics() not implemented");		
		return null;
	}

	@Override
	public void load(Object arg0) {
		log.error("load(Object arg0) not implemented");		
	}

	@Override
	public void loadAll(Collection arg0) {
		log.error("loadAll(Collection arg0) not implemented");		
	}

	@Override
	public Object peek(Object arg0) {
		log.error("peek(Object arg0) not implemented");		
		return null;
	}

	@Override
	public void removeListener(CacheListener arg0) {
		log.error("removeListener(CacheListener arg0) not implemented");		
	}

	@Override
	public void clear() {
		localCache.clear();
		cache.clear();
		put(RESET_DATE_KEY, new Date());
	}

	@Override
	public boolean containsKey(Object arg0) {
		if (localCache.containsKey(arg0)) {
			return true;
		}
		return cache.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		log.error("containsValue(Object arg0) not implemented");		
		return false;
	}

	@Override
	public Set entrySet() {
		log.error("entrySet() not implemented");		
		return null;
	}

	@Override
	public Object get(Object key) {
		try {
			if (localCache.containsKey(key)) {
				localHits++;
				return localCache.get(key);
			}
			Object value = cache.get(key);
			localCache.put((String)key, value);
			cacheHits++;
			return value;
		}
		catch (InvalidValueException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean isEmpty() {
		log.error("isEmpty() not implemented");		
		return false;
	}

	@Override
	public Set keySet() {
		log.error("keySet() not implemented");		
		return null;
	}

	@Override
	public Object put(Object key, Object value) {
		localCache.put((String)key, value);
		try {
			return cache.put(key, value);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return value;
		}
	}

	@Override
	public void putAll(Map map) {
		localCache.putAll(map);
		try {
			cache.putAll(map);
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public Object remove(Object key) {
		localCache.remove(key);
		try {
			return cache.remove(key);
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public int size() {
		log.error("size() not implemented");		
		return 0;
	}

	@Override
	public Collection values() {
		log.error("values() not implemented");		
		return null;
	}

	@Override
	public int getLocalHits() {
		return localHits;
	}

	public int getCacheHits() {
		return cacheHits;
	}

	@Override
	public Cache getMemcache() {
		return cache;
	}

	@Override
	public byte[] getBlob(String key) {
		String chunkList = (String)get(key);
		if (chunkList != null) {
			List<byte[]> data = new ArrayList<byte[]>();
			int size = 0;
			for (String chunkKey : StrUtil.fromCSV(chunkList)) {
				byte[] chunk = (byte[])get(chunkKey);
				if (chunk == null) {
					return null;
				}
				data.add(chunk);
				size += chunk.length;
			}
			return ArrayUtil.packChunks(data);
		}
		return null;
	}

	public static int CACHE_SIZE_LIMIT = 1000000; 

	@Override
	public void putBlob(String key, byte[] data) {
		List<String> chunkList = new ArrayList<String>();
		List<byte[]> chunks = ArrayUtil.makeChunks(data, CACHE_SIZE_LIMIT);
		int i = 0;
		for (byte[] chunk : chunks) {
			String chunkKey = key + String.valueOf(i);
			put(chunkKey, chunk);
			chunkList.add(chunkKey);
			i++;
		}
		put(key, StrUtil.toCSV(chunkList));
	}

	@Override
	public Date getResetDate() {
		return (Date)get(RESET_DATE_KEY);
	}

}
