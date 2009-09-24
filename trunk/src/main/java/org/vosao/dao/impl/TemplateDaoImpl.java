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
import org.vosao.dao.TemplateDao;
import org.vosao.entity.PageEntity;
import org.vosao.entity.TemplateEntity;

public class TemplateDaoImpl extends AbstractDaoImpl implements TemplateDao {

	public void save(final TemplateEntity Template) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (Template.getId() != null) {
				TemplateEntity p = pm.getObjectById(TemplateEntity.class, Template.getId());
				p.copy(Template);
			}
			else {
				pm.makePersistent(Template);
			}
		}
		finally {
			pm.close();
		}
	}
	
	public TemplateEntity getById(final String id) {
		if (id == null) {
			return null;
		}
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(TemplateEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	public List<TemplateEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + TemplateEntity.class.getName();
			List<TemplateEntity> result = (List<TemplateEntity>)pm.newQuery(query).execute();
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
			pm.deletePersistent(pm.getObjectById(TemplateEntity.class, id));
		}
		finally {
			pm.close();
		}
	}
	
	public void remove(final List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				pm.deletePersistent(pm.getObjectById(TemplateEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	public TemplateEntity getByUrl(final String url) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + TemplateEntity.class.getName()
			    + " where url == pUrl parameters String pUrl";
			List<TemplateEntity> result = (List<TemplateEntity>)pm.newQuery(query)
				.execute(url);
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
