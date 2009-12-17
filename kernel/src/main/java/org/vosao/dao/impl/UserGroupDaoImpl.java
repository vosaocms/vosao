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

import org.vosao.dao.UserGroupDao;
import org.vosao.entity.UserGroupEntity;

/**
 * @author Alexander Oleynik
 */
public class UserGroupDaoImpl extends BaseDaoImpl<Long, UserGroupEntity> 
		implements UserGroupDao {

	public UserGroupDaoImpl() {
		super(UserGroupEntity.class);
	}

	@Override
	public List<UserGroupEntity> selectByUser(Long userId) {
		String query = "select from " + UserGroupEntity.class.getName()
			    + " where userId == pUserId"
			    + " parameters String pUserId";
		return select(query, params(userId));
	}

	@Override
	public List<UserGroupEntity> selectByGroup(Long groupId) {
		String query = "select from " + UserGroupEntity.class.getName()
			    + " where groupId == pGroupId"
			    + " parameters String pGroupId";
		return select(query, params(groupId));
	}

	@Override
	public UserGroupEntity getByUserGroup(Long groupId, Long userId) {
		String query = "select from " + UserGroupEntity.class.getName()
			    + " where groupId == pGroupId && userId == pUserId"
			    + " parameters String pGroupId, String pUserId";
		return selectOne(query, params(groupId, userId));
	}
	
}
