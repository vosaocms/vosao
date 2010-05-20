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
import java.util.Collections;
import java.util.List;

import org.vosao.dao.BaseDaoImpl;
import org.vosao.dao.FolderDao;
import org.vosao.entity.FolderEntity;

import com.google.appengine.api.datastore.Query;

public class FolderDaoImpl extends BaseDaoImpl<FolderEntity> 
		implements FolderDao {

	public FolderDaoImpl() {
		super(FolderEntity.class);
	}

	public List<FolderEntity> getByParent(final Long id) {
		Query q = newQuery();
		q.addFilter("parentId", EQUAL, id);
		return select(q, "getByParent", params(id));
	}
	
	public FolderEntity getByParentName(final Long parentId, 
			final String name) {
		Query q = newQuery();
		q.addFilter("parentId", EQUAL, parentId);
		q.addFilter("name", EQUAL, name);
		return selectOne(q, "getByParentName", params(parentId, name));
	}

	@Override
	public FolderEntity getByPath(String path) {
		FolderEntity result = getByParentName(null, "/");
		for (String name : path.split("/")) {
			if (name.equals("")) {
				continue;
			}
			result = getByParentName(result.getId(), name);
			if (result == null) {
				return null;
			}
		}
		return result;
	}

	@Override
	public String getFolderPath(Long folderId) {
		FolderEntity folder = getById(folderId);
		List<String> names = new ArrayList<String>();
		while(folder != null) {
			names.add(folder.getName());
			folder = getById(folder.getParent());
		}
		Collections.reverse(names);
		StringBuffer result = new StringBuffer();
		for (String name : names) {
			if (!name.equals("/")) {
				result.append("/").append(name);
			}
		}
		return result.toString();
	}

	@Override
	public List<FolderEntity> getAncestors(FolderEntity folder) {
		List<FolderEntity> result = new ArrayList<FolderEntity>();
		FolderEntity current = folder;
		while (current.getParent() != null) {
			FolderEntity parent = getById(current.getParent());
			if (parent == null) {
				break;
			}
			result.add(parent);
			current = parent;
		}
		Collections.reverse(result);
		return result;
	}
	
}
