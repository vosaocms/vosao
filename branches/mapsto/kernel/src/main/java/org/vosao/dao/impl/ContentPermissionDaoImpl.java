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

import org.mapsto.Filter;
import org.mapsto.Query;
import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.BaseMapstoDaoImpl;
import org.vosao.dao.ContentPermissionDao;
import org.vosao.entity.ContentPermissionEntity;

/**
 * @author Alexander Oleynik
 */
public class ContentPermissionDaoImpl extends 
		BaseMapstoDaoImpl<ContentPermissionEntity> 
		implements ContentPermissionDao {

	public ContentPermissionDaoImpl() {
		super("ContentPermissionEntity");
	}

	@Override
	public List<ContentPermissionEntity> selectByUrl(final String url) {
		Query<ContentPermissionEntity> q = newQuery();
		q.addFilter("url", Filter.EQUAL, url);
		return q.select("selectByUrl", params(url));
	}

	@Override
	public ContentPermissionEntity getByUrlGroup(final String url, 
			final Long groupId) {
		Query<ContentPermissionEntity> q = newQuery();
		q.addFilter("url", Filter.EQUAL, url);
		q.addFilter("groupId", Filter.EQUAL, groupId);
		return q.selectOne("getByUrlGroup", params(url, groupId));
	}

	private List<ContentPermissionEntity> selectByGroup(final Long groupId) {
		Query<ContentPermissionEntity> q = newQuery();
		q.addFilter("groupId", Filter.EQUAL, groupId);
		return q.select("selectByGroup", params(groupId));
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
