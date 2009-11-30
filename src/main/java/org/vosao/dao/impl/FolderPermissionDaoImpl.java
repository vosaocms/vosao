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
import org.vosao.dao.FolderPermissionDao;
import org.vosao.entity.FolderPermissionEntity;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionDaoImpl extends AbstractDaoImpl 
		implements FolderPermissionDao {

	@Override
	public void save(final FolderPermissionEntity FolderPermission) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (FolderPermission.getId() != null) {
				FolderPermissionEntity p = pm.getObjectById(
						FolderPermissionEntity.class, FolderPermission.getId());
				p.copy(FolderPermission);
			}
			else {
				pm.makePersistent(FolderPermission);
			}
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public FolderPermissionEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FolderPermissionEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<FolderPermissionEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderPermissionEntity.class.getName();
			List<FolderPermissionEntity> result = 
				(List<FolderPermissionEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<FolderPermissionEntity> selectByFolder(final String folderId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " 
				+ FolderPermissionEntity.class.getName()
				+ " where folderId == pFolderId"
				+ " parameters String pFolderId";
			List<FolderPermissionEntity> result = 
				(List<FolderPermissionEntity>)pm.newQuery(query).execute(
						folderId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FolderPermissionEntity getByFolderGroup(final String folderId, 
			final Long groupId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FolderPermissionEntity.class.getName()
				+ " where folderId == pFolderId && groupId == pGroupId"
				+ " parameters String pFolderId, Long pGroupId";
			List<FolderPermissionEntity> result = 
				(List<FolderPermissionEntity>)pm.newQuery(query).execute(
						folderId, groupId);
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
	public void remove(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FolderPermissionEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(FolderPermissionEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
