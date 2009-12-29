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

package org.vosao.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.ContentPermissionDao;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.UserGroupEntity;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionDaoImpl extends 
		BaseDaoImpl<Long, ContentPermissionEntity> 
		implements ContentPermissionDao {

	public ContentPermissionDaoImpl() {
		super(ContentPermissionEntity.class);
	}

	@Override
	public List<ContentPermissionEntity> selectByUrl(final String url) {
		String query = "select from " 
				+ ContentPermissionEntity.class.getName()
				+ " where url == pUrl"
				+ " parameters String pUrl";
		return select(query, params(url));
	}

	@Override
	public ContentPermissionEntity getByUrlGroup(final String url, 
			final Long groupId) {
		String query = "select from " + ContentPermissionEntity.class.getName()
				+ " where url == pUrl && groupId == pGroupId"
				+ " parameters String pUrl, Long pGroupId";
		return selectOne(query, params(url, groupId));
	}

	private List<ContentPermissionEntity> selectByGroup(final Long groupId) {
		String query = "select from " + ContentPermissionEntity.class.getName()
				+ " where groupId == pGroupId"
				+ " parameters Long pGroupId";
		return select(query, params(groupId));
	}
		
	@Override
	public void removeByGroup(List<Long> groupIds) {
		for (Long groupId : groupIds) {
			List<ContentPermissionEntity> list = selectByGroup(groupId);
			remove(getIds(list));
		}
	}

	private List<Long> getIds(List<ContentPermissionEntity> list) {
		List<Long> result = new ArrayList<Long>();
		for (ContentPermissionEntity e : list) {
			result.add(e.getId());
		}
		return result;
	}
	
}
