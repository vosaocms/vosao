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
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

class TemplateEntity extends BaseEntity {

	@BeanProperty
	var title: String

	@BeanProperty
	var url: String

	@BeanProperty
	var content: String = ""
	
	override def load(entity: Entity) {
		super.load(entity)
		title = getStringProperty(entity, "title")
		url = getStringProperty(entity, "url")
		content = getTextProperty(entity, "content")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "title", title, false)
		setProperty(entity, "url", url, true)
		setTextProperty(entity, "content", content)
	}

	def this(title: String, content: String) {
		this()
		this.title = title
		this.content = content
	}

	def this(title: String, content: String, url: String) {
		this(title, content)
		this.url = url
	}

}
