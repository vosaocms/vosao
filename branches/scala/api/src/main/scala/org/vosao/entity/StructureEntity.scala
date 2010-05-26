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
import scala.xml._
import org.vosao.business.vo.StructureFieldVO
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

class StructureEntity extends BaseEntity {

	@BeanProperty
	var title: String

	@BeanProperty
	var content: String = ""
	
	override def load(entity: Entity) {
		super.load(entity)
		title = getStringProperty(entity, "title")
		content = getTextProperty(entity, "content")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "title", title, true)
		setTextProperty(entity, "content", content)
	}

	def this(title: String, content: String) {
		this()
		this.title = title
		this.content = content
	}

	def  getFields(): List[StructureFieldVO] = {
		val doc = XML.load(getContent)
		var result = new ListBuffer[StructureFieldVO]()
		doc match {
			case <fields>{fields @ _*}</fields> =>
				for (field @ <field>{_*}</field> <- fields) {
					result += new StructureFieldVO(
							(field \ "title").text,
							(field \ "name").text,
							(field \ "type").text)
				}
		}
		result.toList
	}
}
