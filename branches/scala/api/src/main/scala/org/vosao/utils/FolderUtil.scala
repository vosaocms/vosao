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

import java.io.UnsupportedEncodingException
import java.util.zip.ZipEntry

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Folder utils.
 * 
 * @author Aleksandr Oleynik
 */
object FolderUtil {
	
	val logger = LogFactory.getLog(FolderUtil.getClass)
	
	def getPathChain(path: String): Array[String] = {
        if (path == null) new Array[String](0)
		path.split("/").tail
	}
	
	def getPathChain(entry: ZipEntry): Array[String] = {
		getPathChain("/" + entry.getName)
	}

	def getFolderChain(chain: Array[String]): Array[String] = chain
	
	def getFolderName(path: String): String = {
		val chain = getPathChain(path)
		if (chain.length > 0) chain(chain.length-1)
		else null;
	}
	
	def getFilePath(path: String): String = {
		path.lastIndexOf("/") match {
			case -1 => ""	
			case 0  => "/"
			case s  => path.substring(0, s)
		}
	}
	
	def getFileName(path: String): String = {
		path.substring(path.lastIndexOf("/") + 1, path.length())
	}

	def getFilePath(entry: ZipEntry): String = getFilePath("/" + entry.getName)

	def getFileExt(path: String): String = {
		path.substring(path.lastIndexOf(".") + 1, path.length()).toLowerCase()
	}
	
	def getParentPath(path: String): String = {
		val i = path.lastIndexOf('/')
		if (i > 0) path.substring(0, i)
		else null;
	}

	def getPageURLFromFolderPath(path: String): String = {
		if (path.startsWith("/page")) {
			val result = path.replace("/page", "")
			if (result.length() == 0) "/"
			result;
		}
		null;
	}
	
	def removeTrailingSlash(path: String): String = {
		if (path.equals("")) "/"
		if (path.charAt(path.length() - 1) == '/' && path != "/") {
			path.substring(0, path.length() - 1)
		}
		path;
	}
	
}