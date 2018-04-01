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

package org.vosao.dao.tool;

import org.vosao.dao.Dao;
import org.vosao.entity.ContentPermissionEntity;
import org.vosao.enums.ContentPermissionType;

public class ContentPermissionTool {

	private Dao dao;
	
	public ContentPermissionTool(Dao aDao) {
		dao = aDao;
	}
	
	public ContentPermissionEntity addPermission(String anUrl, 
			ContentPermissionType perm,	Long aGroupId) {
		return dao.getContentPermissionDao().save(new ContentPermissionEntity(
				anUrl, perm, aGroupId));
	}
	
}
