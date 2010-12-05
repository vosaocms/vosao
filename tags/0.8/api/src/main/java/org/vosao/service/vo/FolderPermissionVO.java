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

package org.vosao.service.vo;

import org.vosao.entity.FolderPermissionEntity;
import org.vosao.entity.GroupEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class FolderPermissionVO {

    private FolderPermissionEntity folderPermission;
    private boolean inherited;
    private GroupEntity group;

	public FolderPermissionVO(final FolderPermissionEntity entity) {
		folderPermission = entity;
	}

	public String getId() {
		return folderPermission.getId() == null ? "null" : 
			folderPermission.getId().toString();
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	public GroupEntity getGroup() {
		return group;
	}

	public void setGroup(GroupEntity group) {
		this.group = group;
	}
	
	public String getPermission() {
		return folderPermission.getPermission().name();
	}
	
}
