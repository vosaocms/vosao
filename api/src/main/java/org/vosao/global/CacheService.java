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

package org.vosao.global;

import java.util.Date;

import javax.cache.Cache;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public interface CacheService extends Cache {

	public static final int MEMCACHE_LIMIT = 1048000;

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
	
	Date getResetDate();
}
