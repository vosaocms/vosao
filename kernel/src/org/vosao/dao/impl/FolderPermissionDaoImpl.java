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

package org.vosao.dao.impl;

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FolderPermissionDao;
import org.vosao.entity.FolderPermissionEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionDaoImpl 
		extends BaseDaoImpl<FolderPermissionEntity> 
		implements FolderPermissionDao {

	public FolderPermissionDaoImpl() {
		super(FolderPermissionEntity.class);
	}

	@Override
	public List<FolderPermissionEntity> selectByFolder(final Long folderId) {
		Query q = newQuery();
		q.addFilter("folderId", EQUAL, folderId);
		return select(q, "selectByFolder", params(folderId));
	}

	@Override
	public FolderPermissionEntity getByFolderGroup(final Long folderId, 
			final Long groupId) {
		Query q = newQuery();
		q.addFilter("folderId", EQUAL, folderId);
		q.addFilter("groupId", EQUAL, groupId);
		return selectOne(q, "getByFolderGroup", params(folderId, groupId));
	}

	private List<FolderPermissionEntity> selectByGroup(final Long groupId) {
		Query q = newQuery();
		q.addFilter("groupId", EQUAL, groupId);
		return select(q, "selectByGroup", params(groupId));
	}

	@Override
	public void removeByGroup(List<Long> groupIds) {
		for (Long groupId : groupIds) {
			List<FolderPermissionEntity> list = selectByGroup(groupId);
			remove(getIds(list));
		}
	}

	private List<Long> getIds(List<FolderPermissionEntity> list) {
		List<Long> result = new ArrayList<Long>();
		for (FolderPermissionEntity e : list) {
			result.add(e.getId());
		}
		return result;
	}

}
