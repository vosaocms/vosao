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
import java.util.Collections;
import java.util.List;

import org.vosao.dao.FolderDao;
import org.vosao.entity.FolderEntity;

public class FolderDaoImpl extends BaseDaoImpl<String, FolderEntity> 
		implements FolderDao {

	public FolderDaoImpl() {
		super(FolderEntity.class);
	}

	public List<FolderEntity> getByParent(final String id) {
		String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent parameters String pParent";
		return select(query, params(id));
	}
	
	public FolderEntity getByParentName(final String parentId, 
			final String name) {
		String query = "select from " + FolderEntity.class.getName()
			    + " where parent == pParent && name == pName " 
			    + "parameters String pParent, String pName";
		return selectOne(query, params(parentId, name));
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
	public String getFolderPath(String folderId) {
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
	
}
