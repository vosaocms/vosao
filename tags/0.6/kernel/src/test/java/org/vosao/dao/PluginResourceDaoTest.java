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

import org.vosao.entity.PluginResourceEntity;

public class PluginResourceDaoTest extends AbstractDaoTest {

	private PluginResourceEntity addPluginResource(String plugin, String url) {
		byte[] c = new byte[1];
		return getDao().getPluginResourceDao().save(
				new PluginResourceEntity(plugin, url, c));
	}
	
	public void testGetByUrl() {
		addPluginResource("sitemap", "file1");
		addPluginResource("black", "file2");
		addPluginResource("super", "file1");
		PluginResourceEntity s = getDao().getPluginResourceDao().getByUrl(
				"sitemap", "file1");
		assertNotNull(s);
		assertEquals("file1", s.getUrl());
		s = getDao().getPluginResourceDao().getByUrl(null, null);
		assertNull(s);
		s = getDao().getPluginResourceDao().getByUrl("sitemap", null);
		assertNull(s);
		s = getDao().getPluginResourceDao().getByUrl("megahit", "test");
		assertNull(s);
	}	
	
}
