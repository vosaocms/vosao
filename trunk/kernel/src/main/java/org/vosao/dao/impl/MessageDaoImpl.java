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

import org.vosao.dao.MessageDao;
import org.vosao.entity.MessageEntity;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

public class MessageDaoImpl extends BaseDaoImpl<MessageEntity> 
		implements MessageDao {

	public MessageDaoImpl() {
		super(MessageEntity.class);
	}

	@Override
	public List<MessageEntity> selectByCode(final String code) {
		Query q = newQuery();
		q.addFilter("code", FilterOperator.EQUAL, code);
		return select(q, "selectByCode", params(code));
	}
	
	@Override
	public MessageEntity getByCode(final String code, 
			final String languageCode) {
		Query q = newQuery();
		q.addFilter("code", FilterOperator.EQUAL, code);
		q.addFilter("languageCode", FilterOperator.EQUAL, languageCode);
		return selectOne(q, "getByCode", params(code, languageCode));
	}

	@Override
	public List<MessageEntity> select(final String languageCode) {
		Query q = newQuery();
		q.addFilter("languageCode", FilterOperator.EQUAL, languageCode);
		return select(q, "select", params(languageCode));
	}

}
