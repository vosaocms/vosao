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

package org.vosao.business.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.CurrentUser;
import org.vosao.business.UserBusiness;
import org.vosao.entity.UserEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class UserBusinessImpl extends AbstractBusinessImpl 
	implements UserBusiness {

	private static final Log logger = LogFactory.getLog(UserBusinessImpl.class);
	
	@Override
	public List<String> validateBeforeUpdate(final UserEntity user) {
		List<String> errors = new ArrayList<String>();
		UserEntity foundUser = getDao().getUserDao().getByEmail(user.getEmail());
		if (user.getId() == null) {
			if (foundUser != null) {
				errors.add("User with such email already exists");
			}
		}
		else {
			if (foundUser != null && !foundUser.getId().equals(user.getId())) {
				errors.add("User with such email already exists");
			}
		}
		if (StringUtil.isEmpty(user.getEmail())) {
			errors.add("Email is empty");
		}
		return errors;
	}

	@Override
	public void remove(List<Long> ids) {
		if (CurrentUser.getInstance().isAdmin()) {
			getDao().getUserGroupDao().removeByUser(ids);
			getDao().getUserDao().remove(ids);
		}
	}
	
}
