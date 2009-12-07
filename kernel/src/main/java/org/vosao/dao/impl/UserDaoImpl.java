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

import javax.jdo.PersistenceManager;

import org.vosao.dao.DaoAction;
import org.vosao.dao.UserDao;
import org.vosao.entity.UserEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.enums.UserRole;

public class UserDaoImpl extends BaseDaoImpl<Long, UserEntity> implements
		UserDao {

	public UserDaoImpl() {
		super(UserEntity.class);
	}

	public UserEntity getByEmail(final String email) {
		String query = "select from " + UserEntity.class.getName()
				+ " where email == pEmail parameters String pEmail";
		return selectOne(query, params(email));
	}

	public List<UserEntity> getByRole(final UserRole role) {
		String query = "select from " + UserEntity.class.getName()
				+ " where role == pRole"
				+ " parameters org.vosao.enums.UserRole pRole";
		return select(query, params(role));
	}

	@Override
	public List<UserEntity> selectByGroup(final Long groupId) {
		return select(new DaoAction<UserEntity>() {

			@Override
			public List<UserEntity> execute(PersistenceManager pm) {
				String query = "select from " + UserGroupEntity.class.getName()
						+ " where groupId == pGroupId"
						+ " parameters Long pGroupId";
				List<UserGroupEntity> userGroups = (List<UserGroupEntity>) pm
						.newQuery(query).execute(groupId);
				List<UserEntity> result = new ArrayList<UserEntity>();
				for (UserGroupEntity userGroup : userGroups) {
					result.add(pm.getObjectById(UserEntity.class, userGroup
							.getUserId()));
				}
				return result;
			}

		});

	}

}
