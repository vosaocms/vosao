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

public class FormDaoImpl extends BaseDaoImpl<String, FormEntity> 
		implements FormDao {

	public FormDaoImpl() {
		super(FormEntity.class);
	}

	@Override
	public FormEntity getByName(final String name) {
		String query = "select from " + FormEntity.class.getName() 
					+ " where name == pName parameters String pName";
		return selectOne(query, params(name));
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
