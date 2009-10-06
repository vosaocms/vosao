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
import org.vosao.dao.FieldDao;
import org.vosao.entity.FieldEntity;
import org.vosao.entity.FormEntity;

public class FieldDaoImpl extends AbstractDaoImpl implements FieldDao {

	@Override
	public void save(final FieldEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FieldEntity p = pm.getObjectById(FieldEntity.class, entity.getId());
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
	public FieldEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FieldEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<FieldEntity> getByForm(final FormEntity form) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FieldEntity.class.getName()
				+ " where formId == pFormId parameters String pFormId";
			List<FieldEntity> result = (List<FieldEntity>)pm.newQuery(query)
				.execute(form.getId());
			return copy(result);
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
			pm.deletePersistent(pm.getObjectById(FieldEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(FieldEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FieldEntity getByName(final FormEntity form, final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FieldEntity.class.getName()
				+ " where formId == pFormId && name == pName"
				+ " parameters String pFormId, String pName";
			List<FieldEntity> result = (List<FieldEntity>)pm.newQuery(query)
				.execute(form.getId(), name);
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
