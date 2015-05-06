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

package org.vosao.search.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.mq.message.IndexMessage;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.entity.LanguageEntity;
import org.vosao.entity.PageEntity;
import org.vosao.search.Hit;
import org.vosao.search.SearchEngine;
import org.vosao.search.SearchIndex;
import org.vosao.search.SearchResult;
import org.vosao.search.SearchResultFilter;
import org.vosao.utils.ListUtil;
import org.vosao.utils.StreamUtil;

/**
 *  
 * @author Alexander Oleynik
 *
 */

public class SearchEngineImpl implements SearchEngine {

	private static final Log logger = LogFactory.getLog(
			SearchEngineImpl.class);

	private Map<String, SearchIndex> indexes;

	private SearchIndex getSearchIndex(String language) {
		if (indexes == null) {
			indexes = new HashMap<String, SearchIndex>();
		}
		if (!indexes.containsKey(language)) {
			indexes.put(language, new SearchIndexImpl(language));
		}
		return indexes.get(language);
	}
	
	@Override
	public void reindex()  {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).clear();
			try {
				getSearchIndex(language.getCode()).saveIndex();
			} catch (IOException e) {
				logger.error(StreamUtil.getStackTrace(e));
			}
		}
		getBusiness().getMessageQueue().publish(new IndexMessage());
	}
	
	@Override
	public SearchResult search(SearchResultFilter filter, String query, 
			int start, int count, String language, int textSize) {
		// Search in language index first for all results
		
		logger.info("into engine.search : language = " + language);
		
		List<Hit> hits = getSearchIndex(language).search(filter, query, textSize);
		
		// Search in all other languages for all results
		for (LanguageEntity lang : getDao().getLanguageDao().select()) {
			if (!lang.getCode().equals(language)) {
				
				logger.info("Searching in " + lang.getCode());
				
				hits.addAll(getSearchIndex(lang.getCode()).search(filter, query, 
						textSize));
			}
		}
		// paginate result and return
		SearchResult result = new SearchResult();
		
		logger.info("Number of hits = " + hits.size());
		result.setCount(hits.size());
		
		int startIndex = start < hits.size() ? start : hits.size();
		int endIndex = startIndex + count;
		if (count == -1) {
			endIndex = hits.size();
		}
		if (endIndex > hits.size()) {
			endIndex = hits.size();
		}
		result.setHits(ListUtil.slice(hits, startIndex, count));
		
		logger.info("out of engine.search");
		return result;
	}

	@Override
	public List<PageEntity> search(SearchResultFilter filter, String query, 
			int start, int count, String language) {
		// Search in language index first for all results
		
		logger.info("into engine.search : language = " + language);
		
		List<PageEntity> pages = getSearchIndex(language).search(filter, query);
		
		// Search in all other languages for all results
		for (LanguageEntity lang : getDao().getLanguageDao().select()) {
			if (!lang.getCode().equals(language)) {
				
				logger.info("Searching in " + lang.getCode());
				
				pages.addAll(getSearchIndex(lang.getCode()).search(filter, query));
			}
		}
				
		logger.info("Number of pages = " + pages.size());
		
		int startIndex = start < pages.size() ? start : pages.size();
		int endIndex = startIndex + count;
		if (count == -1) {
			endIndex = pages.size();
		}
		if (endIndex > pages.size()) {
			endIndex = pages.size();
		}
				
		logger.info("out of engine.search");
		return ListUtil.slice(pages, startIndex, count);

	}

	
	@Override
	public void updateIndex(Long pageId) throws IOException {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).updateIndex(pageId);
		}
	}

	@Override
	public void removeFromIndex(Long pageId) {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).removeFromIndex(pageId);
		}
	}
 
	@Override
	public void saveIndex() throws IOException {
		for (LanguageEntity language : getDao().getLanguageDao().select()) {
			getSearchIndex(language.getCode()).saveIndex();
		}
	}
	
	private Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	private Dao getDao() {
		return getBusiness().getDao();
	}
}
