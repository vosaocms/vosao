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
import java.util.Date
import org.vosao.utils.FolderUtil
import org.vosao.utils.EntityUtil._

import com.google.appengine.api.datastore.Entity

object FileEntity {
	final val IMAGE_EXTENSIONS = Array("jpg","jpeg","png","ico", "gif")
}

class FileEntity extends BaseEntity {

	@BeanProperty
	var title: String

	@BeanProperty
	var filename: String	

	@BeanProperty
	var folderId: Long	

	@BeanProperty
	var mimeType: String

	@BeanProperty
	var lastModifiedTime: Date

	@BeanProperty
	var size: Int

	override def load(entity: Entity) {
		super.load(entity)
		title = getStringProperty(entity, "title")
		filename = getStringProperty(entity, "filename")
		folderId = getLongProperty(entity, "folderId")
		mimeType = getStringProperty(entity, "mimeType")
		lastModifiedTime = getDateProperty(entity, "lastModifiedTime")
		size = getIntegerProperty(entity, "size", 0)
	}
	
	override def save(entity: Entity) {
		super.save(entity)
		setProperty(entity, "title", title, false)
		setProperty(entity, "filename", filename, true)
		setProperty(entity, "folderId", folderId, true)
		setProperty(entity, "mimeType", mimeType, false)
		setProperty(entity, "lastModifiedTime", lastModifiedTime, false)
		setProperty(entity, "size", size, false)
	}

	def this(aTitle: String, aName: String, aFolderId: Long,
			aMimeType: String, aMdttime: Date, aSize: Int) {
		this()
		title = aTitle
		filename = aName
		folderId = aFolderId
		mimeType = aMimeType
		lastModifiedTime = aMdttime
		size = aSize
	}
	
	def isImage(): Boolean = {
		FileEntity.IMAGE_EXTENSIONS.exists(
				ext => ext == FolderUtil.getFileExt(getFilename()))
	}

}
