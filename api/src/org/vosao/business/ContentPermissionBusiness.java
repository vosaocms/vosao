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

package org.vosao.business;

import java.util.List;

import org.vosao.entity.ContentPermissionEntity;
import org.vosao.entity.GroupEntity;
import org.vosao.entity.UserEntity;
import org.vosao.enums.ContentPermissionType;

/**
 * @author Alexander Oleynik
 */
public interface ContentPermissionBusiness {
	
	ContentPermissionEntity getPermission(final String url, 
			final UserEntity user);

	ContentPermissionEntity getGuestPermission(final String url);

	void setPermission(final String url, final GroupEntity group, 
			final ContentPermissionType permission);

	void setPermission(final String url, final GroupEntity group, 
			final ContentPermissionType permission, final String languages);
	
	List<String> validateBeforeUpdate(final ContentPermissionEntity perm);

	List<ContentPermissionEntity> getInheritedPermissions(final String url);
	
	List<ContentPermissionEntity> selectByUrl(String pageUrl);
	
}
