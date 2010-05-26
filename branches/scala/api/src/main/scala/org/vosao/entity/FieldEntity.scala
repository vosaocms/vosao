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

package org.vosao.entity;

import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty
import org.vosao.utils.EntityUtil._
import org.apache.commons.lang.StringUtils
import org.vosao.enums.FieldType

import com.google.appengine.api.datastore.Entity

class FieldEntity extends BaseEntity {

	class Option(value: String, selected: Boolean) {
		@BeanProperty
		var value: String
		
		@BeanProperty
		var selected: Boolean
	}

	@BeanProperty
	var formId: Long

	@BeanProperty
	var name: String

	@BeanProperty
	var title: String

	@BeanProperty
	var fieldType: String

	@BeanProperty
	var mandatory: Boolean

	@BeanProperty
	var values: String

	@BeanProperty
	var defaultValue: String

	@BeanProperty
	var height: Int = 1

	@BeanProperty
	var width: Int = 20

	@BeanProperty
	var index: Int

	@BeanProperty
	var regex: String

	var regexMessage: String

	def this(formId: Long, name: String, title: String,
			fieldType: String, opt: Boolean, defaultValue: String) {
		this()
		this.formId = formId
		this.name = name
		this.title = title
		this.fieldType = fieldType
		this.mandatory = opt
		this.defaultValue = defaultValue
	}

	override def load(entity: Entity) {
		super.load(entity)
		formId = getLongProperty(entity, "formId")
		name = getStringProperty(entity, "name")
		title = getStringProperty(entity, "title")
		fieldType = getStringProperty(entity, "fieldType")
		mandatory = getBooleanProperty(entity, "mandatory", false)
		values = getStringProperty(entity, "values")
		defaultValue = getStringProperty(entity, "defaultValue")
		height = getIntegerProperty(entity, "height", 1)
		width = getIntegerProperty(entity, "width", 20)
		index = getIntegerProperty(entity, "index", 0)
		regex = getStringProperty(entity, "regex")
		regexMessage = getStringProperty(entity, "regexMessage")
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "formId", formId, true)
		setProperty(entity, "name", name, true)
		setProperty(entity, "title", title, false)
		setProperty(entity, "fieldType", fieldType, false)
		setProperty(entity, "mandatory", mandatory, false)
		setProperty(entity, "values", values, false)
		setProperty(entity, "defaultValue", defaultValue, false)
		setProperty(entity, "height", height, false)
		setProperty(entity, "width", width, false)
		setProperty(entity, "index", index, false)
		setProperty(entity, "regex", regex, false)
		setProperty(entity, "regexMessage", regexMessage, false)
	}
	
	def getOptions(): List[Option] = {
		var result = new ListBuffer[Option]()
		val opts = getValues().split("\n")
		for (opt <- opts) {
			val v = opt.replace("*", "")
			val selected = opt.charAt(0) == '*'
			result += new Option(v, selected)
		}
		result.toList
	}

	def setRegexMessage(regexMessage: String) {
		this.regexMessage = regexMessage
		parseRegexMessage()
	}
	
	def getRegexMessage() = regexMessage

	var messages: Map[String, String]
	
	private def parseRegexMessage() {
		messages = scala.collection.mutable.Map.empty()
		if (!StringUtils.isEmpty(getRegexMessage)) {
			for (token <- getRegexMessage().split("::")) {
				val code = token.substring(0, 2) 
				val msg = token.substring(2)
				messages(code) = msg
			}
		}
	}
	
	def getRegexMessage(language: String): String = {
		if (messages == null) parseRegexMessage()
		messages(language)
	}
	
}
