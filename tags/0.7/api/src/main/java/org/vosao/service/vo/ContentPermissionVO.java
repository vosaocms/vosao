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

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class ContentPermissionVO {

    private ContentPermissionEntity contentPermission;
    private boolean inherited;
    private GroupEntity group;

	public ContentPermissionVO(final ContentPermissionEntity entity) {
		contentPermission = entity;
	}

	public static List<ContentPermissionVO> create(
			List<ContentPermissionEntity> list) {
		List<ContentPermissionVO> result = new ArrayList<ContentPermissionVO>();
		for (ContentPermissionEntity ContentPermission : list) {
			result.add(new ContentPermissionVO(ContentPermission));
		}
		return result;
	}

	public String getId() {
		return contentPermission.getId().toString();
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
	
	public boolean isAllLanguages() {
		return contentPermission.isAllLanguages();
	}
	
	public String getLanguages() {
		return contentPermission.getLanguages();
	}

	public String getPermission() {
		return contentPermission.getPermission().name();
	}
	
}
