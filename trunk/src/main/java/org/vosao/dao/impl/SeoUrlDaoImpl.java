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
import org.vosao.dao.SeoUrlDao;
import org.vosao.entity.SeoUrlEntity;

public class SeoUrlDaoImpl extends AbstractDaoImpl implements SeoUrlDao {

	public void save(final SeoUrlEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				SeoUrlEntity p = pm.getObjectById(SeoUrlEntity.class, 
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
	
	public SeoUrlEntity getById(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(SeoUrlEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<SeoUrlEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + SeoUrlEntity.class.getName();
			List<SeoUrlEntity> result = (List<SeoUrlEntity>)pm.newQuery(query)
					.execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	public SeoUrlEntity getByFrom(final String from) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + SeoUrlEntity.class.getName() 
					+ " where fromLink == pFrom parameters String pFrom";
			List<SeoUrlEntity> result = (List<SeoUrlEntity>)pm.newQuery(query)
					.execute(from);
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

	public void remove(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(SeoUrlEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(SeoUrlEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

}
