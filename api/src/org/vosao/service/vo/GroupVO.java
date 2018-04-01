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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.vosao.entity.GroupEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class GroupVO {

    private GroupEntity group;
    private List<UserVO> users;

	public GroupVO(final GroupEntity entity) {
		group = entity;
		users = new ArrayList<UserVO>();
	}

	public static List<GroupVO> create(List<GroupEntity> list) {
		List<GroupVO> result = new ArrayList<GroupVO>();
		for (GroupEntity Group : list) {
			result.add(new GroupVO(Group));
		}
		return result;
	}

	public String getId() {
		return group.getId().toString();
	}

	public String getName() {
		return group.getName();
	}

	public List<UserVO> getUsers() {
		return users;
	}

	public void setUsers(List<UserVO> users) {
		this.users = users;
	}

}
