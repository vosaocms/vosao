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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.FileEntity;
import org.vosao.entity.PageEntity;
import org.vosao.search.Hit;
import org.vosao.search.SearchIndex;
import org.vosao.search.SearchResult;
import org.vosao.utils.StrUtil;

public class SearchIndexImpl implements SearchIndex {

	private static final Log logger = LogFactory.getLog(
			SearchIndexImpl.class);

	private static final String INDEX_MOD_DATE = "IndexModDate";

	private String language;
	private Map<String, Set<Long>> index;
	private Date indexModDate;

	public SearchIndexImpl(String aLanguage) {
		language = aLanguage;
	}
	
	@Override
	public void updateIndex(Long pageId) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page == null) {
			return;
		}
		refreshIndex();
		List<PageEntity> versions = getDao().getPageDao().selectByUrl(
				page.getFriendlyURL());
		for (PageEntity version : versions) {
			removeFromIndex(version.getId());
		}
		page = getDao().getPageDao().getByUrl(page.getFriendlyURL());
		if (page == null) {
			return;
		}
		if (!page.isSearchable()) {
			return;
		}
		String content = getDao().getPageDao().getContent(page.getId(), 
				getLanguage());
		if (content == null) {
			return;
		}
		String data = StrUtil.extractSearchTextFromHTML(content.toLowerCase());
		String[] words = StrUtil.splitByWord(data);
		for (String word : words) {
			if (word.length() < 3) {
				continue;
			}
			if (!getIndex().containsKey(word)) {
				getIndex().put(word, new HashSet<Long>());
			}
			if (!getIndex().get(word).contains(page.getId())) {
				getIndex().get(word).add(page.getId());
			}
		}
	}
	
	@Override
	public void removeFromIndex(Long pageId) {
		for (Set<Long> pages : getIndex().values()) {
			pages.remove(pageId);
		}
	}
	
	@Override
	public void saveIndex() {
		try {
			byte[] indexContent = StrUtil.zipStringToBytes(indexToString());
			FileEntity file = getBusiness().getFileBusiness()
					.saveFile(getIndexFilename(), indexContent);
			indexModDate = file.getLastModifiedTime();
			getBusiness().getSystemService().getCache().getMemcache().put(
					getIndexKey(), indexModDate);
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

	@Override
	public SearchResult search(String query, int start, int count,
			int textSize) {
		try {
		
		refreshIndex();
		SearchResult result = new SearchResult();
		List<Long> pages = new ArrayList<Long>(getPageIds(query));
		//logger.info("found pages " + pages.toString());
		int startIndex = start < pages.size() ? start : pages.size();
		int endIndex = startIndex + count;
		if (count == -1) {
			endIndex = pages.size();
		}
		if (endIndex > pages.size()) {
			endIndex = pages.size();
		}
		for (int i = startIndex; i < endIndex; i++) {
			PageEntity page = getDao().getPageDao().getById(pages.get(i));
			if (page != null) {
				ContentEntity content = getBusiness().getPageBusiness()
						.getPageContent(page, language);
				if (content != null) {
					String text = StrUtil.extractSearchTextFromHTML(
							content.getContent());
					if (text.length() > textSize) {
						text = text.substring(0, textSize);
					}
					result.getHits().add(new Hit(page, text));
				}
			}
			else {
				logger.error("Page not found " + pages.get(i) + ". Rebuild index.");
			}
		}	
		result.setCount(pages.size());
		return result;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			return new SearchResult();
		}
	}

	private Set<Long> getPageIds(String query) {
		String[] words = StrUtil.splitByWord(query);
		if (words.length == 0) {
			return Collections.EMPTY_SET;
		}
		Set<Long> keys = getPageKeys(words[0]);
		int i = 0;
		for (String word : words) {
			if (i++ > 0) {
				keys = keysLogicalAnd(keys, getPageKeys(word));
			}
		}
		//logger.info("found keys " + keys.toString());
		return keys;		
	}	
		
	private Set<Long> keysLogicalAnd(Set<Long> l1, Set<Long> l2) {
		Set<Long> result = new HashSet<Long>();
		for (Long i : l1) {
			if (l2.contains(i) && !result.contains(i)) {
				result.add(i);
			}
		}
		return result;
	}	
	
	private Set<Long> getPageKeys(String word) {
		if (getIndex().containsKey(word)) {
			return getIndex().get(word);
		}
		return Collections.EMPTY_SET;
	}

	private String getIndexKey() {
		return INDEX_MOD_DATE + getLanguage();
	}
	
	private void refreshIndex() {
		Date date = (Date) getBusiness().getSystemService().getCache()
				.getMemcache().get(getIndexKey());
		if (index == null || date == null || !date.equals(indexModDate)) {
			loadIndex();
		}
	}

	private String getIndexFilename() {
		return "/tmp/index_" + getLanguage() + ".bin";
	}
	
	private void loadIndex() {
		try {
			index = new HashMap<String, Set<Long>>();
			indexModDate = null;
			FileEntity file = getBusiness().getFileBusiness()
					.findFile(getIndexFilename());
			if (file == null) {
				return;
			}
			byte[] data = getDao().getFileDao().getFileContent(file);
			if (data != null) {
				String strIndex = StrUtil.unzipStringFromBytes(data);
				indexFromString(strIndex);
				indexModDate = file.getLastModifiedTime();
				Date dt = (Date)getBusiness().getSystemService().getCache()
						.getMemcache().get(getIndexKey());
				if (dt == null || dt.before(indexModDate)) {
					getBusiness().getSystemService().getCache().getMemcache()
							.put(getIndexKey(), indexModDate);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void indexFromString(String data) {
		if (StringUtils.isEmpty(data)) {
			return;
		}
		for (String wordBuf : data.split("\\:")) {
			//logger.info(wordBuf);
			String[] wordStruc = wordBuf.split("\\=");
			if (wordStruc.length != 2 ) {
				logger.error("Problem with index " + wordBuf);
				continue;
			}
			index.put(wordStruc[0], new HashSet<Long>());
			for (String key : wordStruc[1].split(",")) {
				index.get(wordStruc[0]).add(Long.valueOf(key));
			}
		}
	}

	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	private Dao getDao() {
		return getBusiness().getDao();
	}
	
	@Override
	public String getLanguage() {
		return language;
	}

	private Map<String, Set<Long>> getIndex() {
		if (index == null) {
			index = new HashMap<String, Set<Long>>();
		}
		return index;
	}

	@Override
	public void clear() {
		getIndex().clear();
	}
	
}
