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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.global.CacheService;
import org.vosao.global.PageCache;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PageCacheImpl implements PageCache {

	private static class CacheItem implements Serializable {
		private String content;
		private Date timestamp;
		
		public CacheItem(String content) {
			super();
			this.content = content;
			this.timestamp = new Date();
		}
		
		public String getContent() {
			return content;
		}
		
		public Date getTimestamp() {
			return timestamp;
		}
		
	}
	
	private static final Log logger = LogFactory.getLog(PageCacheImpl.class);

	private String getPageKey(String url) {
		return "page:" + url;
	}
	
	private CacheService getCache() {
		return VosaoContext.getInstance().getBusiness().getSystemService()
				.getCache();
	}
	
	private Dao getDao() {
		return VosaoContext.getInstance().getBusiness().getDao();
	}
	
	@Override
	public String get(String url) {
		try {
			CacheItem item = (CacheItem)getCache().get(getPageKey(url));
			if (item != null) {
				if (getCache().getResetDate() == null 
						|| item.getTimestamp().after(getCache()
								.getResetDate())) {
					return item.getContent();
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void put(String url, String content) {
		getCache().put(getPageKey(url), new CacheItem(content));
	}

	@Override
	public void remove(String url) {
		getCache().remove(getPageKey(url));		
	}

}
