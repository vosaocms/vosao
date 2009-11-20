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

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.UserGroupDao;
import org.vosao.entity.UserGroupEntity;

/**
 * @author Alexander Oleynik
 */
public class UserGroupDaoImpl extends AbstractDaoImpl implements UserGroupDao {

	@Override
	public void save(final UserGroupEntity UserGroup) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (UserGroup.getId() != null) {
				UserGroupEntity p = pm.getObjectById(UserGroupEntity.class, UserGroup.getId());
				p.copy(UserGroup);
			}
			else {
				pm.makePersistent(UserGroup);
			}
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public UserGroupEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(UserGroupEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<UserGroupEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserGroupEntity.class.getName();
			List<UserGroupEntity> result = (List<UserGroupEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<UserGroupEntity> selectByUser(Long userId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserGroupEntity.class.getName()
			    + " where userId == pUserId"
			    + " parameters String pUserId";
			List<UserGroupEntity> result = (List<UserGroupEntity>)
					pm.newQuery(query).execute(userId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<UserGroupEntity> selectByGroup(Long groupId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserGroupEntity.class.getName()
			    + " where groupId == pGroupId"
			    + " parameters String pGroupId";
			List<UserGroupEntity> result = (List<UserGroupEntity>)
					pm.newQuery(query).execute(groupId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public void remove(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(UserGroupEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final List<Long> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (Long id : ids) {
				pm.deletePersistent(pm.getObjectById(UserGroupEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
