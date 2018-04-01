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

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.LanguageDao;
import org.vosao.entity.LanguageEntity;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class LanguageDaoImpl extends BaseDaoImpl<LanguageEntity> 
		implements LanguageDao {

	public LanguageDaoImpl() {
		super(LanguageEntity.class);
	}

	public LanguageEntity getByCode(final String code) {
		Query q = newQuery();
		q.addFilter("code", FilterOperator.EQUAL, code);
		return selectOne(q, "getByCode", params(code));
	}

}
