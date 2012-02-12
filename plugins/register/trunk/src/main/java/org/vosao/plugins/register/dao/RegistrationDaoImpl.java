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

package org.vosao.plugins.register.dao;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.plugins.register.entity.RegistrationEntity;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class RegistrationDaoImpl extends BaseDaoImpl<RegistrationEntity> 
		implements RegistrationDao {

	public RegistrationDaoImpl() {
		super(RegistrationEntity.class);
	}

	@Override
	public RegistrationEntity getByEmail(String email) {
		Query q = newQuery();
		q.addFilter("email", FilterOperator.EQUAL, email);
		return selectOne(q, "getByEmail", params(email));
	}

	@Override
	public RegistrationEntity getBySessionKey(String key) {
		Query q = newQuery();
		q.addFilter("sessionKey", FilterOperator.EQUAL, key);
		return selectOne(q, "getBySessionKey", params(key));
	}
	
}
