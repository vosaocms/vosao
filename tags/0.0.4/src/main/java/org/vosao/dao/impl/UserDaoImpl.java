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
import org.vosao.dao.UserDao;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public class UserDaoImpl extends AbstractDaoImpl implements UserDao {

	public void save(final UserEntity user) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (user.getId() != null) {
				UserEntity p = pm.getObjectById(UserEntity.class, user.getId());
				p.copy(user);
			}
			else {
				pm.makePersistent(user);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public UserEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(UserEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<UserEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserEntity.class.getName();
			List<UserEntity> result = (List<UserEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public UserEntity getByEmail(final String email) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserEntity.class.getName() 
					+ " where email == pEmail parameters String pEmail";
			List<UserEntity> result = (List<UserEntity>)pm.newQuery(query)
					.execute(email);
			if (result.size() == 0) {
				return null;
			}
			else {
				return result.get(0);
			}
		}
		finally {
			pm.close();
		}
	}

	public List<UserEntity> getByRole(final UserRole role) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + UserEntity.class.getName() 
				+ " where role == pRole" 
				+ " parameters org.vosao.enums.UserRole pRole";
			List<UserEntity> result = (List<UserEntity>)pm.newQuery(query)
					.execute(role);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(UserEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<Long> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (Long id : ids) {
				pm.deletePersistent(pm.getObjectById(UserEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
