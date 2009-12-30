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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jdo.PersistenceManagerFactoryUtils;
import org.vosao.dao.AbstractDao;
import org.vosao.dao.cache.EntityCache;
import org.vosao.global.SystemService;

public class AbstractDaoImpl implements AbstractDao, Serializable {

	protected static final Log logger = LogFactory.getLog(AbstractDaoImpl.class);

	private PersistenceManagerFactory pmf;
	private EntityCache daoCache;

	public PersistenceManagerFactory getPersistenceManagerFactory() {
		return pmf;
	}

	public void setPersistenceManagerFactory(PersistenceManagerFactory factory) {
		pmf = factory;		
	}
	
	protected PersistenceManager getPersistenceManager() {  
		return PersistenceManagerFactoryUtils  
	    		.getPersistenceManager(pmf, true);
	} 

	protected <T> List<T> copy(final List<T> list) {
		List<T> result = new ArrayList<T>();
		result.addAll(list);
		return result;
	}

	@Override
	public EntityCache getDaoCache() {
		return daoCache;
	}

	@Override
	public void setDaoCache(EntityCache bean) {
		daoCache = bean;		
	}

}
