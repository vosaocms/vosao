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

package org.vosao.dao.impl;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.PageAttributeDao;
import org.vosao.entity.PageAttributeEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class PageAttributeDaoImpl extends BaseDaoImpl<PageAttributeEntity> 
		implements PageAttributeDao {

	public PageAttributeDaoImpl() {
		super(PageAttributeEntity.class);
	}

	@Override
	public List<PageAttributeEntity> getByPage(final String pageUrl) {
		return getByPage(pageUrl, null);
	}
	
	@Override
	public List<PageAttributeEntity> getByPageInherited(final String pageUrl) {
		return getByPage(pageUrl, true);
	}

	private List<PageAttributeEntity> getByPage(final String pageUrl, 
			Boolean inherited) {
		Query q = newQuery();
		q.addFilter("pageUrl", EQUAL, pageUrl);
		if (inherited != null) {
			q.addFilter("inherited", EQUAL, inherited);
		}
		return select(q, "getByPage", params(pageUrl, inherited));
	}

	@Override
	public void removeByPage(String url) {
		Query q = newQuery();
		q.addFilter("pageUrl", EQUAL, url);
		removeSelected(q);
	}
	
}
