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

package org.vosao.entity.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;

public class UserHelper {

	public static Map<Long, UserEntity> createIdMap(List<UserEntity> list) {
		Map<Long, UserEntity> map = new HashMap<Long, UserEntity>();
		for (UserEntity user : list) {
			map.put(user.getId(), user);
		}
		return map;
	}
	
	public static UserEntity ADMIN = new UserEntity("System", "", 
			"system@vosao.org", UserRole.ADMIN);
	
}
