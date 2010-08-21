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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.LanguageEntity;
import org.vosao.global.CacheService;
import org.vosao.global.PageCache;
import org.vosao.global.PageCacheItem;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class PageCacheImpl implements PageCache {

	private static final Log logger = LogFactory.getLog(PageCacheImpl.class);

	private String getPageKey(String url, String language) {
		return "page:" + url + ":" + language;
	}
	
	private CacheService getCache() {
		return VosaoContext.getInstance().getBusiness().getSystemService()
				.getCache();
	}
	
	private Dao getDao() {
		return VosaoContext.getInstance().getBusiness().getDao();
	}
	
	@Override
	public PageCacheItem get(String url, String language) {
		try {
			PageCacheItem item = (PageCacheItem)getCache().get(getPageKey(url,
					language));
			if (item != null) {
				if (getCache().getResetDate() == null 
						|| item.getTimestamp().after(getCache()
								.getResetDate())) {
					return item;
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void put(String url, String language, String content,
			String contentType) {
		getCache().put(getPageKey(url, language), 
				new PageCacheItem(content, contentType));
	}

	@Override
	public void remove(String url) {
		for (LanguageEntity lang : getDao().getLanguageDao().select()) {
			getCache().remove(getPageKey(url, lang.getCode()));		
		}
	}

	@Override
	public boolean contains(String url) {
		for (LanguageEntity lang : getDao().getLanguageDao().select()) {
			if (getCache().containsKey(getPageKey(url, lang.getCode()))) {
				return true;
			}
		}
		return false;
	}

}
