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

object ArrayUtil {

	def makeChunks(data: Array[Byte], chunkSize: Int): List[Array[Byte]] = {
		var result: List[Array[Byte]] = Nil
		val n = data.length / chunkSize
		val finalChunkSize = data.length % chunkSize
		var start = 0
		for (i <- 0 until n) {
			start = i * chunkSize
			result = data.slice(start, start + chunkSize) :: result
		}
		if (finalChunkSize > 0) {
			start = n * chunkSize
			result = data.slice(start, start + finalChunkSize) :: result
		}
		result.reverse
	}
	
	def packChunks(data: List[Array[Byte]]): Array[Byte] = {
		var size = 0;
		data.foreach(c => size += c.length)
		var result = new Array[Byte](size)
		var start = 0
		for (chunk <- data) {
			Array.copy(chunk, 0, result, start, chunk.length)
			start += chunk.length
		}
		result
	}
	
}
