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

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.UserGroupDao;
import org.vosao.entity.UserGroupEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class UserGroupDaoImpl extends BaseNativeDaoImpl<UserGroupEntity> 
		implements UserGroupDao {

	public UserGroupDaoImpl() {
		super(UserGroupEntity.class);
	}

	@Override
	public List<UserGroupEntity> selectByUser(Long userId) {
		Query q = newQuery();
		q.addFilter("userId", EQUAL, userId);
		return select(q, "selectByUser", params(userId));
	}

	@Override
	public List<UserGroupEntity> selectByGroup(Long groupId) {
		Query q = newQuery();
		q.addFilter("groupId", EQUAL, groupId);
		return select(q, "selectByGroup", params(groupId));
	}

	@Override
	public UserGroupEntity getByUserGroup(Long groupId, Long userId) {
		Query q = newQuery();
		q.addFilter("userId", EQUAL, userId);
		q.addFilter("groupId", EQUAL, groupId);
		return selectOne(q, "getByUserGroup", params(groupId, userId));
	}

	@Override
	public void removeByGroup(List<Long> groupIds) {
		for (Long groupId : groupIds) {
			List<UserGroupEntity> list = selectByGroup(groupId);
		    remove(getIds(list));	
		}
	}

	private List<Long> getIds(List<UserGroupEntity> list) {
		List<Long> result = new ArrayList<Long>();
		for (UserGroupEntity e : list) {
			result.add(e.getId());
		}
		return result;
	}
	
	@Override
	public void removeByUser(List<Long> userIds) {
		for (Long userId : userIds) {
			List<UserGroupEntity> list = selectByUser(userId);
		    remove(getIds(list));	
		}
	}
	
}
