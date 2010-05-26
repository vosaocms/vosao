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
import java.util.Date
import com.google.appengine.api.datastore.Entity

/**
 * @author Alexander Oleynik
 */
class CommentEntity extends BaseEntity {

	@BeanProperty
	var pageUrl: String
	
	@BeanProperty
	var name: String
	
	@BeanProperty
	var content: String
	
	@BeanProperty
	var publishDate: Date = new Date
	
	@BeanProperty
	var disabled: Boolean

	override def load(entity: Entity) {
		super.load(entity)
		pageUrl = getStringProperty(entity, "pageUrl")
		name = getStringProperty(entity, "name")
		content = getTextProperty(entity, "content")
		publishDate = getDateProperty(entity, "publishDate")
		disabled = getBooleanProperty(entity, "disabled", false)
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "pageUrl", pageUrl, true)
		setProperty(entity, "name", name, false)
		setTextProperty(entity, "content", content)
		setProperty(entity, "publishDate", publishDate, false)
		setProperty(entity, "disabled", disabled, true)
	}

	def this(aName: String, aContent: String, 
			aPublishDate: Date, aPageUrl: String) {
		this()
		setName(aName)
		setContent(aContent)
		setPublishDate(aPublishDate)
		setPageUrl(aPageUrl)
		setDisabled(false)
	}

	def this(aName: String, aContent: String, 
			aPublishDate: Date, aPageUrl: String, 
			aDisabled: Boolean) {
		this(aName, aContent, aPublishDate, aPageUrl)
		setDisabled(aDisabled)
	}
	
}
