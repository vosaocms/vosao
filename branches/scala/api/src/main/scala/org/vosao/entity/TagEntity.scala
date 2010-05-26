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

import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty
import org.vosao.utils.EntityUtil._
import com.google.appengine.api.datastore.Entity

/**
 * @author Alexander Oleynik
 */
class TagEntity extends BaseEntity {

	@BeanProperty
	var parent: Long

	@BeanProperty
	var name: String

	@BeanProperty
	var pages: List[String] = Nil
	
	override def load(entity: Entity) {
		super.load(entity)
		parent = getLongProperty(entity, "parent")
		name = getStringProperty(entity, "name")
		pages = getListProperty(entity, "pages").asInstanceOf[List[String]]
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "name", name, true)
		setProperty(entity, "parent", parent, true)
		setProperty(entity, "pages", pages)
	}

	def this(aParent: Long, aName: String) {
		this()
		name = aName
		parent = aParent
	}
	
}
