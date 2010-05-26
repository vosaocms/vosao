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
import com.google.appengine.api.datastore.Entity;

class PluginEntity extends BaseEntity {
	
	@BeanProperty
	var name: String
	
	@BeanProperty
	var title: String
	
	@BeanProperty
	var description: String
	
	@BeanProperty
	var website: String
	
	@BeanProperty
	var configStructure: String = ""
	
	@BeanProperty
	var configData: String = ""
	
	@BeanProperty
	var entryPointClass: String
	
	@BeanProperty
	var configURL: String
	
	@BeanProperty
	var pageHeader: String = ""
	
	@BeanProperty
	var version: String
	
	@BeanProperty
	var disabled: Boolean = false

	override def load(entity: Entity) {
		super.load(entity)
		name = getStringProperty(entity, "name")
		title = getStringProperty(entity, "title")
		description = getStringProperty(entity, "description")
		website = getStringProperty(entity, "website")
		configStructure = getTextProperty(entity, "configStructure")
		configData = getTextProperty(entity, "configData")
		entryPointClass = getStringProperty(entity, "entryPointClass")
		configURL = getStringProperty(entity, "configURL")
		pageHeader = getTextProperty(entity, "pageHeader")
		version = getStringProperty(entity, "version")
		disabled = getBooleanProperty(entity, "disabled", true)
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "name", name, true)
		setProperty(entity, "title", title, false)
		setProperty(entity, "description", description, false)
		setProperty(entity, "website", website, false)
		setTextProperty(entity, "configStructure", configStructure)
		setTextProperty(entity, "configData", configData)
		setProperty(entity, "entryPointClass", entryPointClass, false)
		setProperty(entity, "configURL", configURL, false)
		setTextProperty(entity, "pageHeader", pageHeader)
		setProperty(entity, "version", version, false)
		setProperty(entity, "disabled", disabled, true)
	}

	def this(name: String, title: String, configStructure: String,
    		configData: String) {
		this()
		this.name = name
		this.title = title
		setConfigStructure(configStructure)
		setConfigData(configData)
	}
}
