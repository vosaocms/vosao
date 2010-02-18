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

package org.vosao.service.front.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.search.SearchResult;
import org.vosao.service.front.SearchService;
import org.vosao.service.impl.AbstractServiceImpl;

/**
 * @author Alexander Oleynik
 */
public class SearchServiceImpl extends AbstractServiceImpl 
		implements SearchService {

	private static Log logger = LogFactory.getLog(SearchServiceImpl.class);

	@Override
	public SearchResult search(String query, int start, int count, int textSize,
			HttpServletRequest request) {
		String language = getBusiness().getLanguage(request);
		return getBusiness().getSearchEngine().search(query, start, count,
				language, textSize);
	}

	@Override
	public SearchResult search(String query, HttpServletRequest request) {
			String language = getBusiness().getLanguage(request);
		return getBusiness().getSearchEngine().search(query, 0, -1,
				language, 256);
	}
	
}
