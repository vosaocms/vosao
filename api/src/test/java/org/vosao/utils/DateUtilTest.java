/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

package org.vosao.utils;

import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;

public class DateUtilTest extends TestCase {

	public void testToDate() {
		Date dt = new Date();
		String dtStr = DateUtil.toString(dt);
		try {
			Date dt2 = DateUtil.toDate(dtStr);
			assertEquals(dtStr, DateUtil.toString(dt2));
		}
		catch (ParseException e) {
		}
		assertEquals("01.01.2010", String.format("01.%02d.%d", 1, 2010));
	}
	
}
