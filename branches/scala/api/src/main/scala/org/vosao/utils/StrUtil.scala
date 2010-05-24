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

import scala.collection.mutable.ListBuffer

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

import org.apache.commons.lang.StringUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

object StrUtil {

	val logger = LogFactory.getLog(StrUtil.getClass)

	def toCSV(list: Iterable[_]): String = list.mkString(",")

	def fromCSV(data: String): List[String] = {
		var result: List[String] = Nil
		if (!StringUtils.isEmpty(data)) {
			if (data.indexOf(',') == -1) {
				result = data :: result
				result.reverse
			}
			for (s <- data.split(",")) {
				result = s :: result
			}
		}
		result
	}

	/**
	 * Gzip the input string into a byte[].
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	def zipStringToBytes(input: String): Array[Byte] = {
		val bos = new ByteArrayOutputStream()
		val bufos = new BufferedOutputStream(new GZIPOutputStream(bos))
		try {
			bufos.write(input.getBytes("UTF-8"))
			bos.toByteArray
		}
		finally {
			bufos.close()
			bos.close();
		}
	}

	/**
	 * Unzip a string out of the given gzipped byte array.
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	def unzipStringFromBytes(bytes: Array[Byte]): String ={
		val bis = new ByteArrayInputStream(bytes)
		val bufis = new BufferedInputStream(new GZIPInputStream(bis))
		val bos = new ByteArrayOutputStream()
		var buf = new Array[Byte](1024);
		var len = 0
		try {
			len = bufis.read(buf)
			while (len > 0) {
				bos.write(buf, 0, len)
				len = bufis.read(buf)
			}
			bos.toString("UTF-8")
		}
		finally {
			bis.close();
			bufis.close();
			bos.close();
		}
	}

	def removeJavascript(s: String): String = {
		val buf = new StringBuffer(s)
		var scriptStart = buf.indexOf("<script")
		while (scriptStart > 0) {
			val scriptEnd = buf.indexOf("</script>", scriptStart) + 9
			if (scriptEnd > 0) buf.replace(scriptStart, scriptEnd, "")
			scriptStart = buf.indexOf("<script", scriptStart)
		}
		buf.toString
	}
	
	def extractTextFromHTML(html: String): String = { 
		removeJavascript(html).replaceAll("<.*?>", "")
	}

	def toLong(list: List[String]): List[Long] = {
		var result = new ListBuffer[Long]()
		for (s <- list) {
			try {
				result += s.toLong
			}
			catch { 
				case e: NumberFormatException => logger.error(
						"Wrong number format " + s)
			}
		}
		result.toList
	}
	
	def splitByWord(data: String): Array[String] = data.split("[ ,.:?!~#\n\t]+")
	
}
