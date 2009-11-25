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
import org.vosao.dao.GroupDao;
import org.vosao.entity.GroupEntity;

/**
 * @author Alexander Oleynik
 */
public class GroupDaoImpl extends AbstractDaoImpl implements GroupDao {

	@Override
	public void save(final GroupEntity group) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (group.getId() != null) {
				GroupEntity p = pm.getObjectById(GroupEntity.class, group.getId());
				p.copy(group);
			}
			else {
				pm.makePersistent(group);
			}
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public GroupEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(GroupEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<GroupEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + GroupEntity.class.getName();
			List<GroupEntity> result = (List<GroupEntity>)pm.newQuery(query).execute();
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
			pm.deletePersistent(pm.getObjectById(GroupEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(GroupEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public GroupEntity getByName(String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + GroupEntity.class.getName()
				+ " where name == pName"
				+ " parameters String pName";
			List<GroupEntity> result = (List<GroupEntity>)
					pm.newQuery(query).execute(name);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
		finally {
			pm.close();
		}
	}

	@Override
	public GroupEntity getGuestsGroup() {
		return getByName("guests");
	}

}
