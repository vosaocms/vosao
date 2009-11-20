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
import org.vosao.dao.ContentPermissionDao;
import org.vosao.entity.ContentPermissionEntity;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionDaoImpl extends AbstractDaoImpl implements ContentPermissionDao {

	@Override
	public void save(final ContentPermissionEntity ContentPermission) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (ContentPermission.getId() != null) {
				ContentPermissionEntity p = pm.getObjectById(ContentPermissionEntity.class, ContentPermission.getId());
				p.copy(ContentPermission);
			}
			else {
				pm.makePersistent(ContentPermission);
			}
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public ContentPermissionEntity getById(final Long id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(ContentPermissionEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<ContentPermissionEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ContentPermissionEntity.class.getName();
			List<ContentPermissionEntity> result = 
				(List<ContentPermissionEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<ContentPermissionEntity> selectByUrl(final String url) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " 
				+ ContentPermissionEntity.class.getName()
				+ " where url == pUrl"
				+ " parameters String pUrl";
			List<ContentPermissionEntity> result = 
				(List<ContentPermissionEntity>)pm.newQuery(query).execute(url);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public ContentPermissionEntity getByUrlGroup(final String url, 
			final Long groupId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ContentPermissionEntity.class.getName()
				+ " where url == pUrl && groupId == pGroupId"
				+ " parameters String pUrl, Long pGroupId";
			List<ContentPermissionEntity> result = 
				(List<ContentPermissionEntity>)pm.newQuery(query).execute(
						url, groupId);
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
			pm.deletePersistent(pm.getObjectById(ContentPermissionEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(ContentPermissionEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
