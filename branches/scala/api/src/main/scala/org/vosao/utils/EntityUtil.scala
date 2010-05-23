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

package org.vosao.utils

import java.util.Date

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import com.google.appengine.api.datastore.Blob
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Text

object EntityUtil {

	val logger = LogFactory.getLog(EntityUtil.getClass)

	def getKind(clazz: Class[_]) = {
		clazz.getName.substring(clazz.getName.lastIndexOf('.') + 1)
	}

	def getIntegerProperty(entity: Entity, name: String, defaultValue: Int) : Int = {
		val p: Any = entity.getProperty(name) 
		p match {
			case i: Int => i
			case l: Long => l.toInt
			case _ => defaultValue
		}
	}

	def getLongProperty(entity: Entity, name: String, defaultValue: Long): Long = {
		val p: Any = entity.getProperty(name)
		p match {
			case l: Long => l
			case _ => defaultValue
		}
	}

	def getLongProperty(entity: Entity, name: String): Long = getLongProperty(entity, name, 0L)

	def getStringProperty(entity: Entity, name: String): String = {
		val p: Any = entity.getProperty(name)
		p match {
			case s: String => s
			case _ => null
		}
	}

	def getTextProperty(entity: Entity, name: String): String = {
		val p: Any = entity.getProperty(name)
		p match {
			case t: Text => t.getValue
			case _ => null
		}
	}

	def getDateProperty(entity: Entity, name: String): Date = {
		val p: Any = entity.getProperty(name)
		p match {
			case d: Date => d
			case _ => null
		}
	}

	def getBooleanProperty(entity: Entity, name: String, defaultValue: Boolean): Boolean = {
		val p: Any = entity.getProperty(name)
		p match {
			case b: Boolean => b
			case _ => defaultValue
		}
	}

	def getBlobProperty(entity: Entity, name: String): Array[Byte] = {
		val p: Any = entity.getProperty(name)
		p match {
			case b: Blob => b.getBytes
			case _ => null
		}
	}

	def getListProperty(entity: Entity, name: String): List[Any] = {
		val p: Any = entity.getProperty(name)
		p match {
			case l: java.util.List[Any] => List(l)
			case _ => Nil
		}
	}

	def setAnyProperty(entity: Entity, name: String, value: Any, indexed: Boolean) {
		if (indexed) entity.setProperty(name, value)
		else entity.setUnindexedProperty(name, value)
	}

	def setProperty(entity: Entity, name: String, value: Int, indexed: Boolean) {
		setAnyProperty(entity, name, value, indexed)
	}

	def setProperty(entity: Entity, name: String, value: Long, indexed: Boolean) {
		setAnyProperty(entity, name, value, indexed)
	}

	def setProperty(entity: Entity, name: String, value: String, indexed: Boolean) {
		setAnyProperty(entity, name, value, indexed)
	}

	def setTextProperty(entity: Entity, name: String, value: String) {
		if (value == null) entity.setProperty(name, null)
		else entity.setProperty(name, new Text(value))
	}

	def setProperty(entity: Entity, name: String, value: Date, indexed: Boolean) {
		setAnyProperty(entity, name, value, indexed)
	}

	def setProperty(entity: Entity, name: String, value: Boolean, indexed: Boolean) {
		setAnyProperty(entity, name, value, indexed)
	}
	
	def setProperty(entity: Entity, name: String, value: Array[Byte]) {
		if (value == null) entity.setProperty(name, null)
		else entity.setProperty(name, new Blob(value))
	}

	def setProperty(entity: Entity, name: String, value: List[Any]) {
		if (value == null) entity.setProperty(name, null)
		else entity.setProperty(name, java.util.Arrays.asList(value))
	}
	
}