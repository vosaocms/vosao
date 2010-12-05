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

import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
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
	public SearchResult search(String query, int start, int count, int textSize) {
		String language = getBusiness().getLanguage();
		String defaultLanguage = getBusiness().getDefaultLanguage();
		SearchResult result = getBusiness().getSearchEngine().search(
				query, start, count, language, textSize);
		if (!language.equals(defaultLanguage)) {
			SearchResult enResult = getBusiness().getSearchEngine().search(
					query, start, count, defaultLanguage, textSize);
			for (Hit hit : enResult.getHits()) {
				hit.setLocalTitle(hit.getTitle());
				hit.setUrl(hit.getUrl() + "?language=" + defaultLanguage);
			}
			result.setCount(result.getCount() + enResult.getCount());
			result.getHits().addAll(enResult.getHits());
		}
		return result;
	}

	@Override
	public SearchResult search(String query) {
		return search(query, 0, -1, 256);
	}
	
}
