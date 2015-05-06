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

package org.vosao.service.front.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.vosao.entity.PageEntity;
import org.vosao.search.Hit;
import org.vosao.search.SearchResult;
import org.vosao.service.front.SearchService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class SearchServiceImpl extends AbstractServiceImpl 
		implements SearchService {

	@Override
	public SearchResult searchFilter(List<String> sections, String text, int start, 
			int count, int textSize) {
		
		logger.info("into searchFilter");
		String query = text.toLowerCase();
		String language = getBusiness().getLanguage();
		String defaultLanguage = getBusiness().getDefaultLanguage();
		SearchResult result = getBusiness().getSearchEngine().search(
				new SectionSearchFilter(sections),
				query, start, count, language, textSize);
		
		if (!language.equals(defaultLanguage)) {
			
			logger.info("Searching in defaultLanguage = " + defaultLanguage);
			SearchResult enResult = getBusiness().getSearchEngine().search(
					new SectionSearchFilter(sections),
					query, start, count, defaultLanguage, textSize);
			
			for (Hit hit : enResult.getHits()) {
				hit.setLocalTitle(hit.getTitle());
				hit.setUrl(hit.getUrl() + "?language=" + defaultLanguage);
			}
			result.setCount(result.getCount() + enResult.getCount());
			result.getHits().addAll(enResult.getHits());
		}
		logger.info("out of searchFilter");
		return result;
	}

	@Override
	public List<PageEntity> searchFilter(List<String> sections, String text, int start, 
			int count) {
		
		logger.info("into searchFilter");
		String query = text.toLowerCase();
		String language = getBusiness().getLanguage();
		
		List<PageEntity> pages = getBusiness().getSearchEngine().search(
				new SectionSearchFilter(sections),
				query, start, count, language);
		
		logger.info("out of searchFilter");
		return pages;
	}

	@Override
	public SearchResult search(String query) {
		return search(query, 0, -1, DEFAULT_TEXT_SIZE);
	}

	@Override
	public SearchResult search(String query, int start,
			int count, int textSize) {
		return searchFilter(Arrays.asList("/"), query, start, count, textSize);
	}

	@Override
	public SearchResult searchFilter(List<String> sections, String query) {
		return searchFilter(sections, query, 0, -1, DEFAULT_TEXT_SIZE);
	}
	
}
