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

object LanguageEntity {
	final val ENGLISH_CODE = "en"
	final val ENGLISH_TITLE = "English"
}

class LanguageEntity extends BaseEntity {

	@BeanProperty
	var code: String = ""

	@BeanProperty
	var title: String = ""

	override def load(entity: Entity) {
		super.load(entity)
		code = getStringProperty(entity, "code")
		title = getStringProperty(entity, "title")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "code", code, true)
		setProperty(entity, "title", title, false)
	}

	def this(code: String, title: String) {
		this()
		this.code = code
		this.title = title
	}
}
