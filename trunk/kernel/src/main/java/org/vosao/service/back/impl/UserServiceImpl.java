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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.datanucleus.util.StringUtils;
import org.vosao.common.BCrypt;
import org.vosao.common.Messages;
import org.vosao.entity.UserEntity;
import org.vosao.enums.UserRole;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.UserService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.UserVO;
import org.vosao.utils.ParamUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class UserServiceImpl extends AbstractServiceImpl 
		implements UserService {

	@Override
	public List<UserVO> select() {
		return UserVO.create(getDao().getUserDao().select());
	}

	@Override
	public ServiceResponse remove(List<String> ids) {
		List<Long> idList = new ArrayList<Long>();
		String msg = Messages.get("users.success_delete");
		for (String idString : ids) {
			Long id = Long.valueOf(idString);
			if (getBusiness().getUser().getId().equals(id)) {
				msg = Messages.get("users.cant_delete_myself");
			}
			else {
				idList.add(id);
			}
		}
		getBusiness().getUserBusiness().remove(idList);
		return ServiceResponse.createSuccessResponse(msg);
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
		if (!StringUtils.isEmpty(vo.get("email"))) {
			user.setEmail(vo.get("email"));
		}
		if (!StringUtils.isEmpty(vo.get("password"))) {
			user.setPassword(BCrypt.hashpw(vo.get("password"), 
					BCrypt.gensalt()));
		}
		if (!StringUtils.isEmpty(vo.get("role"))) {
			user.setRole(UserRole.valueOf(vo.get("role")));
		}
		if (!StringUtils.isEmpty(vo.get("disabled"))) {
			user.setDisabled(ParamUtil.getBoolean(vo.get("disabled"), false));
		}
		List<String> errors = getBusiness().getUserBusiness()
				.validateBeforeUpdate(user);
		if (errors.isEmpty()) {
			getDao().getUserDao().save(user);
			return ServiceResponse.createSuccessResponse(
					Messages.get("user.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured"), errors);
		}
	}

	@Override
	public UserEntity getLoggedIn() {
		return getBusiness().getUser();
	}

	@Override
	public List<UserVO> selectByGroup(String groupId) {
		return UserVO.create(getDao().getUserDao().selectByGroup(
				Long.valueOf(groupId)));
	}

	@Override
	public ServiceResponse disable(Long userId, boolean disable) {
		UserEntity user = getDao().getUserDao().getById(userId);
		if (user == null) {
			return ServiceResponse.createErrorResponse(Messages.get(
					"user_not_found"));
		}
		user.setDisabled(disable);
		getDao().getUserDao().save(user);
		return ServiceResponse.createSuccessResponse(Messages.get("success"));
	}


}
