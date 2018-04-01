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

package org.vosao.service.back.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserGroupEntity;
import org.vosao.i18n.Messages;
import org.vosao.service.ServiceResponse;
import org.vosao.service.back.GroupService;
import org.vosao.service.impl.AbstractServiceImpl;
import org.vosao.service.vo.GroupVO;
import org.vosao.service.vo.UserVO;

/**
 * @author Alexander Oleynik
 */
public class GroupServiceImpl extends AbstractServiceImpl 
		implements GroupService {

	@Override
	public List<GroupVO> select() {
		List<GroupVO> result = GroupVO.create(getDao().getGroupDao().select());
		for (GroupVO group : result) {
			group.setUsers(UserVO.create(getDao().getUserDao().selectByGroup(
					Long.valueOf(group.getId()))));
		}
		return result;
	}

	@Override
	public ServiceResponse remove(List<String> ids) {
		List<Long> idList = new ArrayList<Long>();
		for (String id : ids) {
			idList.add(Long.valueOf(id));
		}
		getBusiness().getGroupBusiness().remove(idList);
		return ServiceResponse.createSuccessResponse(
				Messages.get("groups.success_delete"));
	}

	@Override
	public GroupVO getById(Long id) {
		GroupEntity group = getDao().getGroupDao().getById(id);
		if (group != null) {
			GroupVO result = new GroupVO(group);
			result.setUsers(UserVO.create(getDao().getUserDao().selectByGroup(
					id)));
			return result;
		}
		return null;
	}

	@Override
	public ServiceResponse save(Map<String, String> vo) {
		GroupEntity group = null;
		if (!StringUtils.isEmpty(vo.get("id"))) {
			group = getDao().getGroupDao().getById(Long.valueOf(vo.get("id")));
		}
		if (group == null) {
			group = new GroupEntity();
		}
		group.setName(vo.get("name"));
		List<String> errors = getBusiness().getGroupBusiness()
				.validateBeforeUpdate(group);
		if (errors.isEmpty()) {
			getDao().getGroupDao().save(group);
			return ServiceResponse.createSuccessResponse(
						Messages.get("group.success_save"));
		}
		else {
			return ServiceResponse.createErrorResponse(
					Messages.get("errors_occured", errors));
		}
	}

	@Override
	public ServiceResponse setGroupUsers(String groupId, List<String> ids) {
		Long id = Long.valueOf(groupId);
		GroupEntity group = getDao().getGroupDao().getById(id);
		if (group != null) {
			List<UserGroupEntity> userGroups = getDao().getUserGroupDao()
					.selectByGroup(id);
			List<String> usersExist = new ArrayList<String>();
			for (UserGroupEntity userGroup : userGroups) {
				if (!ids.contains(userGroup.getUserId().toString())) {
					getDao().getUserGroupDao().remove(userGroup.getId());
				}
				else {
					usersExist.add(userGroup.getUserId().toString());
				}
			}
			for (String userId : ids) {
				if (!usersExist.contains(userId)) {
					UserGroupEntity userGroup = new UserGroupEntity(
							group.getId(), Long.valueOf(userId));
					getDao().getUserGroupDao().save(userGroup);
				}
			}
			return ServiceResponse.createSuccessResponse(
				Messages.get("group.success_update"));
		}
		return ServiceResponse.createErrorResponse(
				Messages.get("group_not_found"));
	}

}
