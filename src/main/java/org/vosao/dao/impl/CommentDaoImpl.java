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

import java.util.Collections;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.vosao.dao.CommentDao;
import org.vosao.entity.CommentEntity;

/**
 * @author Alexander Oleynik
 */
public class CommentDaoImpl extends BaseDaoImpl<String, CommentEntity> 
		implements CommentDao {

	public CommentDaoImpl() {
		super(CommentEntity.class);
	}

	@Override
	public List<CommentEntity> getByPage(final String pageUrl) {
		String query = "select from " + CommentEntity.class.getName()
			    + " where pageUrl == pPageUrl parameters String pPageUrl";
		List<CommentEntity> result = select(query, params(pageUrl));
		Collections.sort(result, new CommentEntity.PublishDateDesc());
		return result;
	}
	
	private CommentEntity getById(final String id, PersistenceManager pm) {
		if (id == null) {
			return null;
		}
		try {
			return pm.getObjectById(CommentEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
	}	
	
	@Override
	public void disable(List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				CommentEntity comment = getById(id, pm);
				if (comment != null) {
					comment.setDisabled(true);
				}
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public void enable(List<String> ids) {
		PersistenceManager pm = getPersistenceManager();
		try {
			for (String id : ids) {
				CommentEntity comment = getById(id, pm);
				if (comment != null) {
					comment.setDisabled(false);
				}
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<CommentEntity> getByPage(String pageUrl, boolean disabled) {
		String query = "select from " + CommentEntity.class.getName()
			    + " where pageUrl == pPageUrl && disabled == pDisabled"
			    + " parameters String pPageUrl, boolean pDisabled";
		List<CommentEntity> result = select(query, params(pageUrl, disabled));
		Collections.sort(result, new CommentEntity.PublishDateDesc());
		return result;
	}
	
}
