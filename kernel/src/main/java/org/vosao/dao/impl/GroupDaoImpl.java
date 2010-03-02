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

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import org.vosao.dao.GroupDao;
import org.vosao.entity.GroupEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class GroupDaoImpl extends BaseDaoImpl<GroupEntity> 
		implements GroupDao {

	public GroupDaoImpl() {
		super(GroupEntity.class);
	}

	@Override
	public GroupEntity getByName(String name) {
		Query q = newQuery();
		q.addFilter("name", EQUAL, name);
		return selectOne(q, "getByName", params(name));
	}

	@Override
	public GroupEntity getGuestsGroup() {
		return getByName("guests");
	}

}
