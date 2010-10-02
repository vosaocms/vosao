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

package org.vosao.entity.field;

import junit.framework.TestCase;

import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;

public class PageAttributesFieldTest extends TestCase {

	@Override
	public void setUp() {
		VosaoContext.getInstance().setConfig(new ConfigEntity());
	}
	
	public void testParse() {
		PageAttributesField field = new PageAttributesField(
				"{test:{ru:'russian', en:'english'}}");
		assertEquals(1, field.size());	
		assertEquals("english", field.get("test"));
	}

	public void testPut() {
		PageAttributesField field = new PageAttributesField(null);
		field.set("test", "en", "value");	
		assertEquals(1, field.size());	
		assertEquals("value", field.get("test"));
		assertNotNull(field.asJSON());
	}
}
