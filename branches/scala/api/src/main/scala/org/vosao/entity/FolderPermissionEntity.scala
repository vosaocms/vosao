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

package org.vosao.entity

import scala.reflect.BeanProperty
import org.vosao.enums.FolderPermissionType
import org.vosao.utils.EntityUtil._

import com.google.appengine.api.datastore.Entity

/**
 * @author Alexander Oleynik
 */
class FolderPermissionEntity extends BaseEntity {

	@BeanProperty
	var folderId: Long

	@BeanProperty
	var permission: String

	@BeanProperty
    var groupId: Long
	
	def this(aFolderId: Long) {
		this()
		folderId = aFolderId
	}
	
	def this(aFolderId: Long, perm: String) {
		this(aFolderId)
		permission = perm
	}

	def this(aFolderId: Long, perm: String, aGroupId: Long) {
		this(aFolderId, perm)
		groupId = aGroupId
	}

	override def load(entity: Entity) {
		super.load(entity)
		folderId = getLongProperty(entity, "folderId")
		permission = getStringProperty(entity, "permission")
		groupId = getLongProperty(entity, "groupId")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "folderId", folderId, true)
		setProperty(entity, "permission", permission, false)
		setProperty(entity, "groupId", groupId, true)
	}

	def isRead() = permission == FolderPermissionType.READ

	def isWrite() = permission == FolderPermissionType.WRITE
	
	def isDenied() = permission == FolderPermissionType.DENIED

	def isAdmin() = permission == FolderPermissionType.ADMIN

	def isChangeGranted() = isWrite || isAdmin
	
	def isMyPermissionHigher(perm: FolderPermissionEntity) = {
		FolderPermissionType.isHigher(getPermission(), perm.getPermission())
	}
	
}
