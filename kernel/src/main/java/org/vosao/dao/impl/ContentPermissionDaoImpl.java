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

import static com.google.appengine.api.datastore.Query.FilterOperator.EQUAL;

import java.util.ArrayList;
import java.util.List;

import org.vosao.dao.ContentPermissionDao;
import org.vosao.entity.ContentPermissionEntity;

import com.google.appengine.api.datastore.Query;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionDaoImpl extends 
		BaseNativeDaoImpl<ContentPermissionEntity> 
		implements ContentPermissionDao {

	public ContentPermissionDaoImpl() {
		super(ContentPermissionEntity.class);
	}

	@Override
	public List<ContentPermissionEntity> selectByUrl(final String url) {
		Query q = newQuery();
		q.addFilter("url", EQUAL, url);
		return select(q, "selectByUrl", params(url));
	}

	@Override
	public ContentPermissionEntity getByUrlGroup(final String url, 
			final Long groupId) {
		Query q = newQuery();
		q.addFilter("url", EQUAL, url);
		q.addFilter("groupId", EQUAL, groupId);
		return selectOne(q, "getByUrlGroup", params(url, groupId));
	}

	private List<ContentPermissionEntity> selectByGroup(final Long groupId) {
		Query q = newQuery();
		q.addFilter("groupId", EQUAL, groupId);
		return select(q, "selectByGroup", params(groupId));
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
