/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.entity;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public interface BaseEntity extends Serializable {

	Key getKey();

	void setKey(Key key);
	
	Long getId();

	void setId(Long id);
	
	void save(Entity entity);
	
	void load(Entity entity);
	
	boolean isNew();
	
	/**
	 * Copy all fields from entity.
	 * @param entity - data to copy from.
	 */
	void copy(BaseEntity entity);
	
	// audit fields
	
	String getCreateUserEmail();

	void setCreateUserEmail(String createUserEmail);

	Date getCreateDate();

	void setCreateDate(Date createDate);

	String getModUserEmail();

	void setModUserEmail(String modUserEmail);

	Date getModDate();

	void setModDate(Date modDate);

	String getCreateDateString();
	
	String getModDateString();

	String getCreateDateTimeString();
	
	String getModDateTimeString();

}
