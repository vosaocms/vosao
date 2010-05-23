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

import junit.framework.TestCase

class ArrayUtilTest extends TestCase {

	def testChunks() {
		val big: Array[Byte] = Array(1,2,3,4,5,6,7,8,9,10,11,12,13)
		val chunks = ArrayUtil.makeChunks(big, 2)
		assert(7 == chunks.length)
		val big2 = ArrayUtil.packChunks(chunks)
		assert(big.length == big2.length)
		for (i <- 0 until big.length)
			assert(big(i) == big2(i), "index=" + i + "big=" + big(i) + " " 
					+ big2.mkString + ".")
	}
	
}
