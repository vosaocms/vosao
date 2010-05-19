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

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.UserDao;
import org.vosao.dao.UserGroupDao;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.UserRole;

import com.google.appengine.api.datastore.Query;
import static com.google.appengine.api.datastore.Query.FilterOperator.*;

public class UserDaoImpl extends BaseDaoImpl<UserEntity> 
		implements UserDao {

	private UserGroupDao userGroupDao;
	
	public UserDaoImpl() {
		super(UserEntity.class);
	}

	public UserEntity getByEmail(final String email) {
		Query q = newQuery();
		q.addFilter("email", EQUAL, email);
		return selectOne(q, "getByEmail", params(email));
	}

	public List<UserEntity> getByRole(final UserRole role) {
		Query q = newQuery();
		q.addFilter("role", EQUAL, role.name());
		return select(q, "getByRole", params(role));
	}

	@Override
	public List<UserEntity> selectByGroup(final Long groupId) {
		List<UserGroupEntity> users = getUserGroupDao().selectByGroup(groupId);
		List<UserEntity> result = new ArrayList<UserEntity>();
		for (UserGroupEntity userGroup : users) {
			UserEntity user = getById(userGroup.getUserId());
			if (user != null) {
				result.add(user);
			}
		}
		return result;
	}

	public UserGroupDao getUserGroupDao() {
		return userGroupDao;
	}

	public void setUserGroupDao(UserGroupDao userGroupDao) {
		this.userGroupDao = userGroupDao;
	}

	@Override
	public UserEntity getByKey(String key) {
		if (key == null) {
			return null;
		}
		Query q = newQuery();
		q.addFilter("forgotPasswordKey", EQUAL, key);
		return selectOne(q, "getByKey", params(key));
	}

}
