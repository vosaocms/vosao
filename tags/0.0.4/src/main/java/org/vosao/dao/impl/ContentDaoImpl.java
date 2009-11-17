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
import org.vosao.dao.ContentDao;
import org.vosao.entity.ContentEntity;

public class ContentDaoImpl extends AbstractDaoImpl implements ContentDao {

	@Override
	public void save(final ContentEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				ContentEntity p = pm.getObjectById(ContentEntity.class, 
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
	public ContentEntity getById(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(ContentEntity.class, id);
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
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(ContentEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(ContentEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<ContentEntity> select(final String parentClass, 
			final String parentKey) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ContentEntity.class.getName() 
					+ " where parentClass == pParentClass" 
					+ "   && parentKey == pParentKey" 
					+ " parameters String pParentClass, String pParentKey";
			List<ContentEntity> result = (List<ContentEntity>) 
					pm.newQuery(query).execute(parentClass, parentKey);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public ContentEntity getByLanguage(final String parentClass, 
			final String parentKey, final String language) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + ContentEntity.class.getName() 
					+ " where parentClass == pParentClass" 
					+ "   && parentKey == pParentKey"
					+ "   && languageCode == pLanguage"
					+ " parameters String pParentClass, String pParentKey," 
					+ "   String pLanguage";
			List<ContentEntity> result =  (List<ContentEntity>)
					pm.newQuery(query).execute(
							parentClass, parentKey, language);
			if (result.size() > 0) {
				return result.get(0);
			}
			return null;
		}
		finally {
			pm.close();
		}
	}
	
}
