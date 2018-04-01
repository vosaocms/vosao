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
import org.vosao.dao.PageDependencyDao;
import org.vosao.entity.PageDependencyEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class PageDependencyDaoImpl extends BaseDaoImpl<PageDependencyEntity> 
		implements PageDependencyDao {

	public PageDependencyDaoImpl() {
		super(PageDependencyEntity.class);
	}

	@Override
	public List<PageDependencyEntity> selectByPage(final String pageUrl) {
		Query q = newQuery();
		q.addFilter("page", EQUAL, pageUrl);
		return select(q, "getByPage", params(pageUrl));
	}
	
	@Override
	public List<PageDependencyEntity> selectByDependency(final String pageUrl) {
		Query q = newQuery();
		q.addFilter("dependency", EQUAL, pageUrl);
		return select(q, "getByDependency", params(pageUrl));
	}

	@Override
	public PageDependencyEntity getByPageAndDependency(String pageUrl,
			String dependency) {
		Query q = newQuery();
		q.addFilter("page", EQUAL, pageUrl);
		q.addFilter("dependency", EQUAL, dependency);
		return selectOne(q, "getByPageAndDependency", params(pageUrl, dependency));
	}
	
}
