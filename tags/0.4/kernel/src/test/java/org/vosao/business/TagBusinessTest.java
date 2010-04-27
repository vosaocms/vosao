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

package org.vosao.business;

import org.vosao.entity.TagEntity;

public class TagBusinessTest extends AbstractBusinessTest {

	private TagEntity addTag(Long parent, String name) {
		return getDao().getTagDao().save(new TagEntity(parent, name));
	}
	
	public void testGetByPath() {
		TagEntity t1 = addTag(null, "t1");
		TagEntity t2 = addTag(null, "t2");
		TagEntity t3 = addTag(t1.getId(), "t3");
		TagEntity t4 = addTag(t3.getId(), "t4");
		TagEntity t = getBusiness().getTagBusiness().getByPath("/t1");
		assertNotNull(t);
		assertEquals("t1", t.getName());
		t = getBusiness().getTagBusiness().getByPath("/t2");
		assertNotNull(t);
		assertEquals("t2", t.getName());
		t = getBusiness().getTagBusiness().getByPath("/t1/t3");
		assertNotNull(t);
		assertEquals("t3", t.getName());
		t = getBusiness().getTagBusiness().getByPath("/t1/t3/t4");
		assertNotNull(t);
		assertEquals("t4", t.getName());
	}

	public void testGetPath() {
		TagEntity t1 = addTag(null, "t1");
		TagEntity t2 = addTag(null, "t2");
		TagEntity t3 = addTag(t1.getId(), "t3");
		TagEntity t4 = addTag(t3.getId(), "t4");
		String p = getBusiness().getTagBusiness().getPath(t4);
		assertEquals("/t1/t3/t4", p);
		p = getBusiness().getTagBusiness().getPath(t2);
		assertEquals("/t2", p);
		p = getBusiness().getTagBusiness().getPath(t3);
		assertEquals("/t1/t3", p);
	}
	
	
}
