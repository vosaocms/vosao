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

import java.util.ArrayList;
import java.util.List;

import org.vosao.entity.StructureEntity;
import org.vosao.entity.StructureTemplateEntity;
import org.vosao.enums.StructureTemplateType;

public class StructureDaoTest extends AbstractDaoTest {

	private StructureEntity addStructure(String title, String content) {
		StructureEntity s = getDao().getStructureDao().save(
				new StructureEntity(title, content));
		getDao().getStructureTemplateDao().save(new StructureTemplateEntity(
				title, s.getId(), StructureTemplateType.VELOCITY, content));
		return s;
	}
	
	public void testGetByTitle() {
		addStructure("sitemap", "file1");
		addStructure("black", "file2");
		addStructure("super", "file1");
		StructureEntity s = getDao().getStructureDao().getByTitle("sitemap");
		assertNotNull(s);
		assertEquals("file1", s.getContent());
		s = getDao().getStructureDao().getByTitle(null);
		assertNull(s);
		s = getDao().getStructureDao().getByTitle("megahit");
		assertNull(s);
		StructureTemplateEntity st = getDao().getStructureTemplateDao()
				.getByTitle("sitemap");
		assertNotNull(st);
	}	

	public void testRemove() {
		StructureEntity s1 = addStructure("sitemap", "file1");
		StructureEntity s2 = addStructure("black", "file2");
		StructureEntity s3 = addStructure("super", "file1");
		List<Long> ids = new ArrayList<Long>();
		ids.add(s1.getId());
		ids.add(s2.getId());
		ids.add(s3.getId());
		getDao().getStructureDao().remove(ids);
		assertEquals(0, getDao().getStructureDao().select().size());
		assertEquals(0, getDao().getStructureTemplateDao().select().size());
	}	
	
	
}
