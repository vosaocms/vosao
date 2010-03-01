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

package org.vosao.entity;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class UserGroupEntity extends BaseNativeEntityImpl {

	private static final long serialVersionUID = 3L;

	private Long groupId;
	private Long userId;

	public UserGroupEntity() {
	}
	
	public UserGroupEntity(Long aGroupId, Long aUserId) {
		this();
		groupId = aGroupId;
		userId = aUserId;
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		groupId = getLongProperty(entity, "groupId");
		userId = getLongProperty(entity, "userId");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		entity.setProperty("groupId", groupId);
		entity.setProperty("userId", userId);
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGroupId() {
		return groupId;
	}
	
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
