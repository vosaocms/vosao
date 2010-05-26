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
import com.google.appengine.api.datastore.Entity
import org.vosao.utils.EntityUtil._

class FormEntity extends BaseEntity {

	@BeanProperty
	var name: String

	@BeanProperty
	var title: String

	@BeanProperty
	var email: String

	@BeanProperty
	var letterSubject: String

	@BeanProperty
	var sendButtonTitle: String

	@BeanProperty
	var showResetButton: Boolean

	@BeanProperty
	var resetButtonTitle: String

	@BeanProperty
	var enableCaptcha: Boolean

	def this(aName: String, aEmail: String, aTitle: String, aSubject: String) {
		this()
		name = aName
		email = aEmail
		title = aTitle
		letterSubject = aSubject
	}
	
	override def load(entity: Entity) {
		super.load(entity)
		name = getStringProperty(entity, "name")
		email = getStringProperty(entity, "email")
		title = getStringProperty(entity, "title")
		letterSubject = getStringProperty(entity, "letterSubject")
		sendButtonTitle = getStringProperty(entity, "sendButtonTitle")
		resetButtonTitle = getStringProperty(entity, "resetButtonTitle")
		showResetButton = getBooleanProperty(entity, "showResetButton", false)
		enableCaptcha = getBooleanProperty(entity, "enableCaptcha", false)
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "name", name, true)
		setProperty(entity, "email", email, false)
		setProperty(entity, "title", title, false)
		setProperty(entity, "letterSubject", letterSubject, false)
		setProperty(entity, "sendButtonTitle", sendButtonTitle, false)
		setProperty(entity, "resetButtonTitle", resetButtonTitle, false)
		setProperty(entity, "showResetButton", showResetButton, false)
		setProperty(entity, "enableCaptcha", enableCaptcha, false)
	}

}
