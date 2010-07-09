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

package org.vosao.dao;

import java.util.List;

import org.vosao.entity.TagEntity;

public class TagDaoTest extends AbstractDaoTest {

	private TagEntity addTag(Long parent, String name) {
		return getDao().getTagDao().save(new TagEntity(parent, name));
	}
	
	public void testGetByName() {
		addTag(null, "test1");
		addTag(1L, "test2");
		addTag(2L, "test3");
		TagEntity tag = getDao().getTagDao().getByName(null, "test1");
		assertNotNull(tag);
		assertEquals("test1", tag.getName());
	}	
	
	public void testSelectByParent() {
		addTag(null, "test1");
		addTag(1L, "test2");
		addTag(2L, "test3");
		List<TagEntity> tags = getDao().getTagDao().selectByParent(null);
		assertNotNull(tags);
		assertEquals(1, tags.size());
		assertEquals("test1", tags.get(0).getName());
	}	
	
}
