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
import com.google.appengine.api.datastore.Text
import org.vosao.utils.EntityUtil._

object FormConfigEntity {
	val FORM_TEMPLATE = "formTemplate"
	val LETTER_TEMPLATE = "letterTemplate"
}

class FormConfigEntity extends BaseEntity {

	@BeanProperty
	var formTemplate: String = ""

	@BeanProperty
	var letterTemplate: String = ""

	override def load(entity: Entity) {
		super.load(entity)
		formTemplate = getTextProperty(entity, "formTemplate")
		letterTemplate = getTextProperty(entity, "letterTemplate")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setTextProperty(entity, "formTemplate", formTemplate)
		setTextProperty(entity, "letterTemplate", letterTemplate)
	}

}
