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

import java.util.List;

import org.vosao.dao.FolderPermissionDao;
import org.vosao.entity.FolderPermissionEntity;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionDaoImpl 
		extends BaseDaoImpl<Long, FolderPermissionEntity> 
		implements FolderPermissionDao {

	public FolderPermissionDaoImpl() {
		super(FolderPermissionEntity.class);
	}

	@Override
	public List<FolderPermissionEntity> selectByFolder(final String folderId) {
		String query = "select from " 
				+ FolderPermissionEntity.class.getName()
				+ " where folderId == pFolderId"
				+ " parameters String pFolderId";
		return select(query, params(folderId));
	}

	@Override
	public FolderPermissionEntity getByFolderGroup(final String folderId, 
			final Long groupId) {
		String query = "select from " + FolderPermissionEntity.class.getName()
				+ " where folderId == pFolderId && groupId == pGroupId"
				+ " parameters String pFolderId, Long pGroupId";
		return selectOne(query, params(folderId, groupId));
	}

}
