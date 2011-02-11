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

package org.vosao.service.front;

import java.util.List;

import org.vosao.search.SearchResult;
import org.vosao.service.AbstractService;

/**
 * @author Alexander Oleynik
 */
public interface SearchService extends AbstractService {
	
	static final int DEFAULT_TEXT_SIZE = 256;
	
	SearchResult search(String query, int start, int count, int textSize);

	SearchResult search(String query);

	SearchResult searchFilter(List<String> sections, String query, int start, 
			int count, int textSize);

	SearchResult searchFilter(List<String> sections, String query);

}
