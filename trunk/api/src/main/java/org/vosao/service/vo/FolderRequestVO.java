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

package org.vosao.service.vo;

import java.util.Collections;
import java.util.List;

import org.vosao.entity.FileEntity;
import org.vosao.entity.FolderEntity;
import org.vosao.entity.FolderPermissionEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class FolderRequestVO {

    private FolderEntity folder;
    private FolderEntity parent;
    private List<FolderEntity> ancestors;
    private List<FileEntity> files;
    private List<FolderEntity> children;
    private List<FolderPermissionVO> permissions;
    private List<GroupVO> groups;
    private FolderPermissionEntity folderPermission;
	
    public FolderRequestVO() {
    	files = Collections.EMPTY_LIST;
    	children = Collections.EMPTY_LIST;
    	permissions = Collections.EMPTY_LIST;
    	groups = Collections.EMPTY_LIST;
    	ancestors = Collections.EMPTY_LIST;
    }

	public FolderEntity getFolder() {
		return folder;
	}

	public void setFolder(FolderEntity folder) {
		this.folder = folder;
	}

	public List<FolderEntity> getChildren() {
		return children;
	}

	public void setChildren(List<FolderEntity> children) {
		this.children = children;
	}

	public List<FolderPermissionVO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<FolderPermissionVO> permissions) {
		this.permissions = permissions;
	}

	public List<GroupVO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupVO> groups) {
		this.groups = groups;
	}

	public FolderPermissionEntity getFolderPermission() {
		return folderPermission;
	}

	public void setFolderPermission(FolderPermissionEntity folderPermission) {
		this.folderPermission = folderPermission;
	}

	public List<FileEntity> getFiles() {
		return files;
	}

	public void setFiles(List<FileEntity> files) {
		this.files = files;
	}

	public List<FolderEntity> getAncestors() {
		return ancestors;
	}

	public void setAncestors(List<FolderEntity> ancestors) {
		this.ancestors = ancestors;
	}

	public FolderEntity getParent() {
		return parent;
	}

	public void setParent(FolderEntity parent) {
		this.parent = parent;
	}
    
}
