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
import org.vosao.dao.FolderDao;
import org.vosao.entity.FolderEntity;

public class FolderDaoImpl extends AbstractDaoImpl implements FolderDao {

	public void save(final FolderEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FolderEntity p = pm.getObjectById(FolderEntity.class, 
						entity.getId());
				p.copy(entity);
			}
			else {
				pm.makePersistent(entity);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public FolderEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FolderEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<FolderEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName();
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
					.execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FolderEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(FolderEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	public List<FolderEntity> getByParent(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent parameters String pParent";
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
				.execute(id);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public FolderEntity getByParentName(final String parentId, 
			final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent && name == pName " 
			    + "parameters String pParent, String pName";
			List<FolderEntity> result = (List<FolderEntity>)pm.newQuery(query)
				.execute(parentId, name);
			if (result.size() == 0) {
				return null;
			}
			return result.get(0);
		}
		finally {
			pm.close();
		}
		
	}

	
}
