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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.UserEntity;

/**
 * Value object to be returned from services.
 * 
 * @author Alexander Oleynik
 */
public class UserVO {

    private UserEntity user;

	public UserVO(final UserEntity entity) {
		user = entity;
	}

	public static List<UserVO> create(List<UserEntity> list) {
		List<UserVO> result = new ArrayList<UserVO>();
		for (UserEntity User : list) {
			result.add(new UserVO(User));
		}
		return result;
	}

	public String getId() {
		return user.getId().toString();
	}

	public String getName() {
		return user.getName();
	}

	public String getEmail() {
		return user.getEmail();
	}
	
	public String getRole() {
		return user.getRole().name();
	}
	
	public boolean isDisabled() {
		return user.isDisabled();
	}
}
