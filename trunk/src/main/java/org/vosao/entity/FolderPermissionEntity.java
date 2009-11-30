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

package org.vosao.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.vosao.enums.FolderPermissionType;

/**
 * @author Alexander Oleynik
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FolderPermissionEntity implements Serializable {

	private static final long serialVersionUID = 2L;

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String folderId;
	
	@Persistent
	private FolderPermissionType permission;
	
	@Persistent
    private Long groupId;
	
	public FolderPermissionEntity() {
	}
	
	public FolderPermissionEntity(String aFolderId) {
		this();
		folderId = aFolderId;
	}
	
	public FolderPermissionEntity(String aFolderId, FolderPermissionType perm) {
		this(aFolderId);
		permission = perm;
	}

	public FolderPermissionEntity(String aFolderId, FolderPermissionType perm,
			Long aGroupId) {
		this(aFolderId, perm);
		groupId = aGroupId;
	}

	public void copy(final FolderPermissionEntity entity) {
		setFolderId(entity.getFolderId());
		setPermission(entity.getPermission());
		setGroupId(entity.getGroupId());
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String aFolderId) {
		this.folderId = aFolderId;
	}

	public boolean equals(Object object) {
		if (object instanceof FolderPermissionEntity) {
			FolderPermissionEntity entity = (FolderPermissionEntity)object;
			if (getId() == null && entity.getId() == null) {
				return true;
			}
			if (getId() != null && getId().equals(entity.getId())) {
				return true;
			}
		}
		return false;
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
