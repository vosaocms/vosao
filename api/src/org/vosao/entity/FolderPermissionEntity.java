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

package org.vosao.entity;

import org.vosao.enums.FolderPermissionType;
import static org.vosao.utils.EntityUtil.*;

import com.google.appengine.api.datastore.Entity;

/**
 * @author Alexander Oleynik
 */
public class FolderPermissionEntity extends BaseEntityImpl {

	private static final long serialVersionUID = 3L;

	private Long folderId;
	private FolderPermissionType permission;
    private Long groupId;
	
	public FolderPermissionEntity() {
	}
	
	public FolderPermissionEntity(Long aFolderId) {
		this();
		folderId = aFolderId;
	}
	
	public FolderPermissionEntity(Long aFolderId, FolderPermissionType perm) {
		this(aFolderId);
		permission = perm;
	}

	public FolderPermissionEntity(Long aFolderId, FolderPermissionType perm,
			Long aGroupId) {
		this(aFolderId, perm);
		groupId = aGroupId;
	}

	@Override
	public void load(Entity entity) {
		super.load(entity);
		folderId = getLongProperty(entity, "folderId");
		permission = FolderPermissionType.valueOf(getStringProperty(entity, 
				"permission"));
		groupId = getLongProperty(entity, "groupId");
	}
	
	@Override
	public void save(Entity entity) {
		super.save(entity);
		setProperty(entity, "folderId", folderId, true);
		setProperty(entity, "permission", permission.name(), false);
		setProperty(entity, "groupId", groupId, true);
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long aFolderId) {
		this.folderId = aFolderId;
	}

	public FolderPermissionType getPermission() {
		return permission;
	}

	public void setPermission(FolderPermissionType permission) {
		this.permission = permission;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public boolean isRead() {
		return permission.equals(FolderPermissionType.READ);
	}

	public boolean isWrite() {
		return permission.equals(FolderPermissionType.WRITE);
	}
	
	public boolean isDenied() {
		return permission.equals(FolderPermissionType.DENIED);
	}

	public boolean isAdmin() {
		return permission.equals(FolderPermissionType.ADMIN);
	}

	public boolean isChangeGranted() {
		return isWrite() || isAdmin();
	}
	
	public boolean isMyPermissionHigher(FolderPermissionEntity perm) {
		return getPermission().ordinal() > perm.getPermission().ordinal();
	}
	
}
