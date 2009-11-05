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

import org.vosao.entity.PageEntity;

public class PageDaoTest extends AbstractDaoTest {

	private PageEntity addPage(final String title, 
			final String url,final PageEntity parent) {
		PageEntity page = new PageEntity(title, url, 
				parent == null ? null : parent.getId());
		getDao().getPageDao().save(page);
		return page;
	}
	
	public void testSave() {
		addPage("title", "/url", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(1, pages.size());
		PageEntity page1 = pages.get(0);
		assertEquals("title", page1.getTitle());
	}	
	
	public void testGetById() {
		PageEntity page = addPage("title","/url",null);
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		assertEquals(page.getTitle(), page2.getTitle());
		assertEquals(page.getFriendlyURL(), page2.getFriendlyURL());
		assertEquals(page.getParent(), page2.getParent());
	}	

	public void testSelect() {
		addPage("title1", "/url1", null);
		addPage("title2", "/url2", null);
		addPage("title3", "/url3", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(3, pages.size());
	}	
	
	public void testUpdate() {
		PageEntity page = addPage("title1", "/url1", null);
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		page2.setTitle("update");
		getDao().getPageDao().save(page2);
		PageEntity page3 = getDao().getPageDao().getById(page.getId());
		assertEquals("update", page3.getTitle());
	}
	
	public void testResultList() {
		addPage("title1", "/url1", null);
		addPage("title2", "/url2", null);
		addPage("title3", "/url3", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		PageEntity page = new PageEntity("title", "/url", null);
		pages.add(page);
		assertEquals(4, pages.size());
	}

	public void testGetByParent() {
		PageEntity root = addPage("root", "/", null);
		addPage("title1", "/url1", root);
		addPage("title2", "/url2", null);
		addPage("title3", "/url3", root);
		List<PageEntity> pages = getDao().getPageDao().getByParent(root.getId());
		assertEquals(2, pages.size());
	}	
	
	public void testGetByUrl() {
		PageEntity root = addPage("root", "/", null);
		addPage("title1", "/url1", root);
		addPage("title2", "/url2", null);
		addPage("title3", "/url3", root);
		PageEntity page = getDao().getPageDao().getByUrl("/url3");
		assertNotNull(page);
		assertEquals("title3", page.getTitle());
	}	
	
}
