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

package org.vosao.utils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.vosao.common.VosaoContext;

public class DateUtil {

	private static final Format formatter = new SimpleDateFormat("dd.MM.yyyy");
	private static final DateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
	private static final Format headerFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
	
	public static String toString(final Date date) {
		if (date == null) return "";
		return formatter.format(date);
	}
	
	public static TimeZone getTimeZone() {
		return VosaoContext.getInstance().getBusiness().getTimeZone();
	}
	
	public static String dateTimeToString(final Date date) {
		if (date == null) {
			return "null";
		}
		TimeZone tz = getTimeZone();
		if (!tz.equals(dateTimeFormatter.getTimeZone())) {
			dateTimeFormatter.setTimeZone(tz);
		}
		return dateTimeFormatter.format(date);
	}
	
	public static String timeToString(final Date date) {
		if (date == null) {
			return "null";
		}
		TimeZone tz = getTimeZone();
		if (!tz.equals(timeFormatter.getTimeZone())) {
			timeFormatter.setTimeZone(tz);
		}
		return timeFormatter.format(date);
	}

	public static Date dateTimeToDate(final String str) throws ParseException {
		TimeZone tz = getTimeZone();
		if (!tz.equals(dateTimeFormatter.getTimeZone())) {
			dateTimeFormatter.setTimeZone(tz);
		}
		return (Date)dateTimeFormatter.parseObject(str);
	}

	public static Date toDate(final String str) throws ParseException {
		return (Date)formatter.parseObject(str);
	}

	public static String toHeaderString(final Date date) {
		if (date == null) {
			return "";
		}
		return headerFormatter.format(date);
	}
	
	
}
