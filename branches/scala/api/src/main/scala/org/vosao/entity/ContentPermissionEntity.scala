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
import scala.collection.mutable.ListBuffer
import org.vosao.utils.EntityUtil._
import org.vosao.enums.ContentPermissionType
import com.google.appengine.api.datastore.Entity

/**
 * @author Alexander Oleynik
 */
class ContentPermissionEntity extends BaseEntity {

	@BeanProperty
	var url: String

	@BeanProperty
	var allLanguages: Boolean = true

	@BeanProperty
	var languages: String

	@BeanProperty
	var permission: String

	@BeanProperty
    var groupId: Long
	
	def this(anUrl: String) {
		this()
		url = anUrl
	}
	
	def this(anUrl: String, perm: String) {
		this(anUrl)
		permission = perm
	}

	def this(anUrl: String, perm: String, aGroupId: Long) {
		this(anUrl, perm)
		groupId = aGroupId
	}

	override def load(entity: Entity) {
		super.load(entity)
		url = getStringProperty(entity, "url")
		allLanguages = getBooleanProperty(entity, "allLanguages", true)
		languages = getStringProperty(entity, "languages")
		permission = getStringProperty(entity, "permission")
		groupId = getLongProperty(entity, "groupId")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "url", url, true)
		setProperty(entity, "allLanguages", allLanguages, false)
		setProperty(entity, "languages", languages, false)
		setProperty(entity, "permission", permission, false)
		setProperty(entity, "groupId", groupId, true)
	}

	def getLanguagesList(): List[String] = {
		var result = new ListBuffer[String]()
		if (languages != null) {
			for (lang <- languages.split(",")) {
				result += lang
			}
		}
		result.toList
	}
	
	def isRead() = permission == ContentPermissionType.READ

	def isWrite() = permission == ContentPermissionType.WRITE
	
	def isWrite(language: String): Boolean = {
		val write = isWrite()
		if (allLanguages) write
		write && getLanguagesList().contains(language)
	}

	def isPublish() = permission == ContentPermissionType.PUBLISH
	
	def isDenied() = permission == ContentPermissionType.DENIED

	def isAdmin() = permission == ContentPermissionType.ADMIN

	def isChangeGranted() = isWrite || isPublish || isAdmin
	
	def isChangeGranted(language: String) = {
		isWrite(language) || isPublish || isAdmin 
	}
			
	def isPublishGranted() = isPublish || isAdmin
	
	def isMyPermissionHigher(perm: ContentPermissionEntity) = {
		ContentPermissionType.isHigher(getPermission, perm.getPermission)
	}
	
}
