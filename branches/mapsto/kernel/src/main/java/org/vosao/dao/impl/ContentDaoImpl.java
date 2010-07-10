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

package org.vosao.dao.impl;

import java.util.List;

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.ContentDao;
import org.vosao.entity.ContentEntity;

public class ContentDaoImpl extends BaseMapstoDaoImpl<ContentEntity> 
		implements ContentDao {

	public ContentDaoImpl() {
		super("ContentEntity");
	}

	@Override
	public List<ContentEntity> select(final String parentClass, 
			final Long parentKey) {
		Query<ContentEntity> q = newQuery();
		q.addFilter("parentClass", Filter.EQUAL, parentClass);
		q.addFilter("parentKey", Filter.EQUAL, parentKey);
		return q.select("select", params(parentClass, parentKey));
	}
	
	@Override
	public ContentEntity getByLanguage(final String parentClass, 
			final Long parentKey, final String language) {
		Query<ContentEntity> q = newQuery();
		q.addFilter("parentClass", Filter.EQUAL, parentClass);
		q.addFilter("parentKey", Filter.EQUAL, parentKey);
		q.addFilter("languageCode", Filter.EQUAL, language);
		return q.selectOne("getByLanguage", params(parentClass, parentKey, 
				language));
	}

	@Override
	public void removeById(String className, Long id) {
		Query<ContentEntity> q = newQuery();
		q.addFilter("parentClass", Filter.EQUAL, className);
		q.addFilter("parentKey", Filter.EQUAL, id);
		removeSelected(q);
	}
	
}
