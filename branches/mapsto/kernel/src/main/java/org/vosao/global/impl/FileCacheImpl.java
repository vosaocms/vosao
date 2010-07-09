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

import javax.cache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.global.FileCache;
import org.vosao.global.FileCacheItem;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class FileCacheImpl implements FileCache {

	private static final Log logger = LogFactory.getLog(FileCacheImpl.class);
	
	private Cache cache;
	
	public FileCacheImpl(Cache cache) {
		this.cache = cache;
	}
	
	private String getFileKey(String path) {
		return "file:" + path;
	}
	
	@Override
	public FileCacheItem get(String path) {
		return (FileCacheItem)cache.get(getFileKey(path));
	}

	@Override
	public void put(String path, FileCacheItem item) {
		cache.put(getFileKey(path), item);
	}

	@Override
	public void remove(String path) {
		cache.remove(getFileKey(path));
	}

}
