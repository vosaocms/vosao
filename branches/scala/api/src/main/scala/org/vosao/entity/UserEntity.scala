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
import org.vosao.enums.UserRole
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity

/**
 * @author Alexander Oleynik
 */
class UserEntity extends BaseEntity {

	@BeanProperty
	var name: String

	@BeanProperty
	var password: String

	@BeanProperty
	var email: String

	@BeanProperty
	var role: String = UserRole.USER
	
	override def load(entity: Entity) {
		super.load(entity)
		name = getStringProperty(entity, "name")
		password = getStringProperty(entity, "password")
		email = getStringProperty(entity, "email")
		role = getStringProperty(entity, "role")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "name", name, false)
		setProperty(entity, "password", password, false)
		setProperty(entity, "email", email, true)
		setProperty(entity, "role", role, true)
	}

	def this(aName: String, aPassword: String,
			anEmail: String, aRole: String) {
		this()
		name = aName
		password = aPassword
		email = anEmail
		role = aRole
	}
	
	def isAdmin() = role == UserRole.ADMIN

	def isSiteUser() = role == UserRole.SITE_USER

	def isUser() = role == UserRole.USER
	
	def isEditor() = isAdmin || isUser
	
}
