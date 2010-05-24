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

import java.util.Date;

import com.josephoconnell.html.HTMLInputFilter

/**
 * @author Alexander Oleynik
 */
object ParamUtil {

	def getInteger(s: String, defaultValue: Int): Int = {
		try {
			s.toInt
		}
		catch {
			case e: NumberFormatException => defaultValue
		}
	}
	
	def getLong(s: String, defaultValue: Long): Long = {
		try {
			s.toLong
		}
		catch {
			case e: NumberFormatException => defaultValue
		}
	}

	def getBoolean(s: String, defaultValue: Boolean): Boolean = {
		try {
			s.toBoolean
		}
		catch {
			case e: NumberFormatException => defaultValue
		}
	}

	/**
	 * Convert string to date from format dd.mm.yyyy
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	def getDate(s: String, defaultValue: Date): Date = {
		try {
			DateUtil.toDate(s)
		}
		catch {
			case _ => defaultValue
		}
	}

	val xssFilter = new HTMLInputFilter()
	
	def filterXSS(value: String): String  = xssFilter.filter(value)
	
}
