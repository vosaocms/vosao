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
import org.vosao.dao.MessageDao;
import org.vosao.entity.MessageEntity;

public class MessageDaoImpl extends AbstractDaoImpl implements MessageDao {

	@Override
	public void save(final MessageEntity entity) {
		PersistenceManager pm = getPersistenceManager();
		try {
			if (entity.getId() != null) {
				MessageEntity p = pm.getObjectById(MessageEntity.class, 
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
	public MessageEntity getById(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			return pm.getObjectById(MessageEntity.class, id);
		}
		catch (NucleusObjectNotFoundException e) {
			return null;
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public List<MessageEntity> selectByCode(final String code) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + MessageEntity.class.getName()
				+ " where code == pCode"
				+ " parameters String pCode";
			List<MessageEntity> result = (List<MessageEntity>)
					pm.newQuery(query).execute(code);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
	@Override
	public MessageEntity getByCode(final String code, 
			final String languageCode) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + MessageEntity.class.getName() 
					+ " where code == pCode && languageCode == pLanguageCode" 
					+ " parameters String pCode, String pLanguageCode";
			List<MessageEntity> result = (List<MessageEntity>)
					pm.newQuery(query).execute(code, languageCode);
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
	public void remove(final String id) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(MessageEntity.class, id));
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
				pm.deletePersistent(pm.getObjectById(MessageEntity.class, id));
			}
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<MessageEntity> select(final String languageCode) {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + MessageEntity.class.getName()
				+ " where languageCode == pLanguageCode"
				+ " parameters String pLanguageCode";
			List<MessageEntity> result = (List<MessageEntity>)
					pm.newQuery(query).execute(languageCode);
			return copy(result);
		}
		finally {
			pm.close();
		}
	}

	@Override
	public List<MessageEntity> select() {
		PersistenceManager pm = getPersistenceManager();
		try {
			String query = "select from " + MessageEntity.class.getName();
			List<MessageEntity> result = (List<MessageEntity>)
					pm.newQuery(query).execute();
			return copy(result);
		}
		finally {
			pm.close();
		}
	}
	
}
