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

import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtil {

	val formatter = new SimpleDateFormat("dd.MM.yyyy")
	val dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
	val headerFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z")
	
	def toString(date: Date) = {
		if (date == null) ""
		formatter.format(date)
	}
	
	def dateTimeToString(date: Date): String = {
		dateTimeFormatter.format(date)
	}
	
	def dateTimeToDate(str: String): Date = {
		dateTimeFormatter.parse(str)
	}

	def toDate(str: String): Date = {
		formatter.parse(str);
	}

	def toHeaderString(date: Date): String = {
		if (date == null) ""
		headerFormatter.format(date)
	}
	
}
