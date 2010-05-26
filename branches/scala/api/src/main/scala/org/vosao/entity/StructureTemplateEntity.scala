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
import org.vosao.enums.StructureTemplateType
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

class StructureTemplateEntity extends BaseEntity {

	@BeanProperty
	var title: String

	@BeanProperty
	var structureId: Long

	@BeanProperty
	var templateType: String = StructureTemplateType.VELOCITY

	@BeanProperty
	var content: String
	
	override def load(entity: Entity) {
		super.load(entity)
		title = getStringProperty(entity, "title")
		structureId = getLongProperty(entity, "structureId")
		templateType = getStringProperty(entity, "type")
		content = getTextProperty(entity, "content")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "title", title, true)
		setProperty(entity, "structureId", structureId, true)
		setProperty(entity, "type", templateType, false)
		setTextProperty(entity, "content", content)
	}

	def this(title: String, structureId: Long, 
			templateType: String, content: String) {
		this()
		this.title = title
		this.structureId = structureId
		this.templateType = templateType
		this.content = content
	}

	def isVelocity() = templateType == StructureTemplateType.VELOCITY

	def isXSLT() = templateType == StructureTemplateType.XSLT
	
}
