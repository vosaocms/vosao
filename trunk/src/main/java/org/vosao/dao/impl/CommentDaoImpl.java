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
import org.vosao.dao.CommentDao;
import org.vosao.entity.CommentEntity;

public class CommentDaoImpl extends AbstractDaoImpl implements CommentDao {

	@Override
	public void save(final CommentEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				CommentEntity p = pm.getObjectById(CommentEntity.class, 
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
	
	@Override
	public CommentEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(CommentEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(CommentEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(CommentEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<CommentEntity> getByPage(final String pageId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + CommentEntity.class.getName()
			    + " where pageId == pPageId parameters String pPageId";
			List<CommentEntity> result = (List<CommentEntity>)pm.newQuery(query)
				.execute(pageId);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
}
