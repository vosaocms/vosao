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

import java.util.Collections;
import java.util.List;

import org.vosao.entity.PageEntity;
import org.vosao.search.SearchResultFilter;

/**
 * @author Alexander Oleynik
 */
public class SectionSearchFilter implements SearchResultFilter {

	private List<String> sections;
	
	public SectionSearchFilter(List<String> sections) {
		this.sections = sections != null ? sections : Collections.EMPTY_LIST;
	}

	@Override
	public boolean check(PageEntity page) {
		if (page == null) {
			return false;
		}
		for (String url : sections) {
			if (page.getFriendlyURL().startsWith(url)) {
				return true;
			}
		}
		return false;
	}
	
}
