/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.global.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

public class CacheServiceImpl implements CacheService {

	private static final Log log = LogFactory.getLog(CacheServiceImpl.class);

	private static final long LOCAL_CACHE_TTL = 5000;
	
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
	public Map getAll(Collection arg0) {
		log.error("getAll(Collection arg0) not implemented");		
		return null;
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
	public Object get(Object arg0) {
		if (localCache.containsKey(arg0)) {
			localHits++;
			return localCache.get(arg0);
		}
		Object value = cache.get(arg0);
		if (value != null) {
			cacheHits++;
			localCache.put((String)arg0, value);
		}
		return value;
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
	public Object put(Object arg0, Object arg1) {
		localCache.put((String)arg0, arg1);
		return cache.put(arg0, arg1);
	}

	@Override
	public void putAll(Map arg0) {
		log.error("putAll(Map arg0) not implemented");		
	}

	@Override
	public Object remove(Object arg0) {
		localCache.remove(arg0);
		return cache.remove(arg0);
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

}
