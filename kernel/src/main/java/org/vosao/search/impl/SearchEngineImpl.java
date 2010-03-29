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

package org.vosao.search.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.PageEntity;
import org.vosao.search.Hit;
import org.vosao.search.SearchEngine;
import org.vosao.search.SearchResult;
import org.vosao.utils.StrUtil;

import com.google.appengine.api.datastore.KeyFactory;

public class SearchEngineImpl implements SearchEngine {

	private static final Log logger = LogFactory.getLog(
			SearchEngineImpl.class);

	
	private static final String INDEX_MOD_DATE = "IndexModDate";
	
	private Business business;
	private HashMap<String, ArrayList<Long>> index;
	private Date indexModDate;

	@Override
	public SearchResult search(String query, int start, int count,
			String language, int textSize) {
		try {
		
		checkIndex();
		SearchResult result = new SearchResult();
		List<String> urls = getContentUrls(getContentIds(query));
		//logger.info("found urls " + urls.toString());
		int startIndex = start < urls.size() ? start : urls.size();
		int endIndex = startIndex + count;
		if (count == -1) {
			endIndex = urls.size();
		}
		if (endIndex > urls.size()) {
			endIndex = urls.size();
		}
		for (int i = startIndex; i < endIndex; i++) {
			String url = urls.get(i);
			PageEntity page = getBusiness().getDao().getPageDao()
				.getByUrl(url);
			if (page != null) {
				String content = getBusiness().getDao().getPageDao().getContent(
								page.getId(), language);
				if (content == null) {
					content = getBusiness().getDao().getPageDao().getContent(
							page.getId(), "en");					
				}
				content = StrUtil.extractTextFromHTML(content);
				if (content.length() > textSize) {
					content = content.substring(0, textSize);
				}
				Hit hit = new Hit(page, content);
				result.getHits().add(hit);
			}
		}	
		result.setCount(urls.size());
		return result;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return new SearchResult();
		}
	}

	private List<ContentEntity> getContentIds(String query) {
		String[] words = query.split("\\W+");
		if (words.length == 0) {
			return Collections.EMPTY_LIST;
		}
		List<Long> keys = getContentKeys(words[0]);
		int i = 0;
		for (String word : words) {
			if (i++ > 0) {
				keys = keysLogicalAnd(keys, getContentKeys(word));
			}
		}
		//logger.info("found keys " + keys.toString());
		return getContents(keys);		
	}	
		
	private List<Long> keysLogicalAnd(List<Long> l1, List<Long> l2) {
		List<Long> result = new ArrayList<Long>();
		for (Long i : l1) {
			if (l2.contains(i) && !result.contains(i)) {
				result.add(i);
			}
		}
		return result;
	}	
	
	private List<Long> getContentKeys(String word) {
		if (getIndex().containsKey(word)) {
			return getIndex().get(word);
		}
		return Collections.EMPTY_LIST;
	}
	
	private List<ContentEntity> getContents(List<Long> keys) {	
		List<ContentEntity> result = new ArrayList<ContentEntity>();
		for (Long id : keys) {
			ContentEntity contentEntity = getBusiness().getDao().getContentDao()
					.getById(id);
			if (contentEntity == null) {
				logger.error("ContentEntity not found " + id);
				continue;
			}
			result.add(contentEntity);
		}
		return result;
	}

	private Long getContentKey(String id) {
		return KeyFactory.stringToKey(id).getId();		
	}
	
	private List<String> getContentUrls(List<ContentEntity> contents) {
		List<String> urls = new ArrayList<String>();
		for (ContentEntity content : contents) {
			PageEntity page = getBusiness().getDao().getPageDao()
				.getById(content.getParentKey());
			if (page != null) {
				if (!page.isSearchable()) {
					continue;
				}
		    	if (getBusiness().getContentPermissionBusiness().getPermission(
		    			page.getFriendlyURL(), 
		    			VosaoContext.getInstance().getUser()).isDenied()) {
		    		continue;
		    	}
		    	PageEntity approvedPage = getBusiness().getDao().getPageDao()
		    			.getByUrl(page.getFriendlyURL());
				if (approvedPage != null && !urls.contains(page.getFriendlyURL())) {
					urls.add(page.getFriendlyURL());
				}
			}
			else {
				logger.error("PageEntity not found for content " 
						+ content.getParentKey());
			}
		}
		return urls;
	}
	
	private void checkIndex() {
		if (index == null) {
			loadIndex();
			return;
		}
		Date date = (Date) getBusiness().getSystemService().getCache()
				.getMemcache().get(INDEX_MOD_DATE);
		if (date == null) {
			getBusiness().getSystemService().getCache()
					.getMemcache().put(INDEX_MOD_DATE, indexModDate);
			return;
		}
		if (!date.equals(indexModDate)) {
			loadIndex();
		}
	}
	
	@Override
	public void updateIndex(PageEntity page) {
		List<ContentEntity> contents = getBusiness().getDao().getPageDao()
				.getContents(page.getId());
		for (ContentEntity content : contents) {
			updateIndex(content);
		}
	}

	@Override
	public void updateIndex(ContentEntity content) {
		String data = StrUtil.extractTextFromHTML(content.getContent()
				.toLowerCase());
		//logger.info(data);
		String[] words = data.split("\\W+");
		Long key = content.getId();
		clearIndex(key);
		for (String word : words) {
			if (word.length() < 3) {
				continue;
			}
			if (!getIndex().containsKey(word)) {
				getIndex().put(word, new ArrayList<Long>());
			}
			if (getIndex().get(word).indexOf(key) == -1) {
				getIndex().get(word).add(key);
			}
		}
	}

	private void clearIndex(Long id) {
		for (String word : getIndex().keySet()) {
			getIndex().get(word).remove(id);
		}
	}
	
	public void saveIndex() {
		try {
			byte[] indexContent = StrUtil.zipStringToBytes(indexToString());
			FileEntity file = getBusiness().getFileBusiness()
					.saveFile("/tmp/index.bin",	indexContent);
			indexModDate = file.getLastModifiedTime();
			getBusiness().getSystemService().getCache().getMemcache().put(
					INDEX_MOD_DATE, file.getLastModifiedTime());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String indexToString() {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (String word : getIndex().keySet()) {
			if (getIndex().get(word).isEmpty()) {
				continue;
			}
			buf.append(i++ == 0 ? "" : ":").append(word).append("=");
			int j = 0;
			for (Long id : getIndex().get(word)) {
				buf.append(j++ == 0 ? "" : ",").append(id);
			}
		}
		return buf.toString();
	}
	
	private void indexFromString(String data) {
		for (String wordBuf : data.split("\\:")) {
			//logger.info(wordBuf);
			String[] wordStruc = wordBuf.split("\\=");
			if (wordStruc.length != 2 ) {
				logger.error("Problem with index " + wordBuf);
				continue;
			}
			index.put(wordStruc[0], new ArrayList<Long>());
			for (String key : wordStruc[1].split(",")) {
				index.get(wordStruc[0]).add(Long.valueOf(key));
			}
		}
	}
	
	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public HashMap<String, ArrayList<Long>> getIndex() {
		if (index == null) {
			loadIndex();
		}
		return index;
	}
	
	private void loadIndex() {
		try {
			index = new HashMap<String, ArrayList<Long>>();
			FileEntity file = getBusiness().getFileBusiness()
					.findFile("/tmp/index.bin");
			if (file == null) {
				return;
			}
			byte[] data = getBusiness().getDao().getFileDao()
					.getFileContent(file);
			if (data != null) {
				String strIndex = StrUtil.unzipStringFromBytes(data);
				indexFromString(strIndex);
				indexModDate = file.getLastModifiedTime();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reindex() {
		/*List<PageEntity> pages = getBusiness().getDao().getPageDao().select();
		Queue queue = getBusiness().getSystemService().getQueue("reindex");
		for (PageEntity page : pages) {
			queue.add(url(IndexTaskServlet.TASK_URL)
					.param("pageId", page.getId()));
		}*/
		reindexInRequest();
	}

	@Override
	public void reindexInRequest() {
		List<PageEntity> pages = getBusiness().getDao().getPageDao().select();
		for (PageEntity page : pages) {
			getBusiness().getSearchEngine().updateIndex(page);
		}
		getBusiness().getSearchEngine().saveIndex();
	}

	@Override
	public void removeFromIndex(ContentEntity content) {
		clearIndex(content.getId());
	}

	@Override
	public void removeFromIndex(PageEntity page) {
		List<ContentEntity> contents = getBusiness().getDao().getPageDao()
				.getContents(page.getId());
		for (ContentEntity content : contents) {
			removeFromIndex(content);
		}
	}
	
}
