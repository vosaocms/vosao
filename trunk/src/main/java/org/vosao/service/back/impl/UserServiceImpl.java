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

package org.vosao.service.back.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.util.StringUtils;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.UserService;
import org.vosao.service.impl.AbstractServiceImpl;

public class UserServiceImpl extends AbstractServiceImpl 
		implements UserService {

	private static final Log logger = LogFactory.getLog(UserServiceImpl.class);

	@Override
	public List<UserEntity> select() {
		return getDao().getUserDao().select();
	}

	@Override
	public ServiceResponse remove(List<Long> ids) {
		getDao().getUserDao().remove(ids);
		return ServiceResponse.createSuccessResponse(
				"Users were successfully deleted");
	}

	@Override
	public UserEntity getById(Long id) {
		return getDao().getUserDao().getById(id);
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		UserEntity user = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			user = getDao().getUserDao().getById(Long.valueOf(vo.get("id")));
		}
		if (user == null) {
			user = new UserEntity();
		}
		user.setName(vo.get("name"));
		user.setEmail(vo.get("email"));
		if (!StringUtils.isEmpty(vo.get("password"))) {
			user.setPassword(vo.get("password"));
		}
		user.setRole(UserRole.valueOf(vo.get("role")));
		List<String> errors = getBusiness().getUserBusiness()
				.validateBeforeUpdate(user);
		if (errors.isEmpty()) {
			getDao().getUserDao().save(user);
			return ServiceResponse.createSuccessResponse(
						"User was successfully saved.");
		}
		else {
			return ServiceResponse.createErrorResponse(
					"Error occured during user save", errors);
		}
	}

	@Override
	public UserEntity getLoggedIn(HttpServletRequest request) {
		return getBusiness().getUserPreferences(request).getUser();
	}

}
