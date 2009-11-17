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
import org.vosao.dao.FormDao;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.FormConfigEntity;
import org.vosao.entity.FormEntity;

public class FormDaoImpl extends AbstractDaoImpl implements FormDao {

	@Override
	public void save(final FormEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FormEntity p = pm.getObjectById(FormEntity.class, entity.getId());
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
	public FormEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(FormEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<FormEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FormEntity.class.getName();
			List<FormEntity> result = (List<FormEntity>)pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public FormEntity getByName(final String name) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FormEntity.class.getName() 
					+ " where name == pName parameters String pName";
			List<FormEntity> result = (List<FormEntity>)pm.newQuery(query)
					.execute(name);
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

	@Override
	public void remove(final String id) {
		if (id == null) {
			return;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(FormEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(FormEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	private List<FormConfigEntity> selectConfig() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + FormConfigEntity.class.getName();
			List<FormConfigEntity> result = (List<FormConfigEntity>)pm.newQuery(
					query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public FormConfigEntity getConfig() {
		List<FormConfigEntity> list = selectConfig();
		if (list.size() > 0) {
			return list.get(0);
		}
		logger.error("Form config for site was not found!");
		return new FormConfigEntity();
	}
	
	@Override
	public void save(final FormConfigEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				FormConfigEntity p = pm.getObjectById(FormConfigEntity.class, 
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
	

}
