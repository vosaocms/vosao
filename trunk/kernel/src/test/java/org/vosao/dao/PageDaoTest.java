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

import org.vosao.dao.tool.PageTool;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;

public class PageDaoTest extends AbstractDaoTest {

	private PageTool pageTool;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        pageTool = new PageTool(getDao());
	}    
	
	public void testSave() {
		pageTool.addPage("title", "/url");
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(1, pages.size());
		PageEntity page1 = pages.get(0);
		assertEquals("title", page1.getTitle());
	}	
	
	public void testGetById() {
		PageEntity page = pageTool.addPage("title","/url");
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		assertEquals(page.getTitle(), page2.getTitle());
		assertEquals(page.getFriendlyURL(), page2.getFriendlyURL());
		assertEquals(page.getParentUrl(), page2.getParentUrl());
	}	

	public void testSelect() {
		pageTool.addPage("title1", "/url1");
		pageTool.addPage("title2", "/url2");
		pageTool.addPage("title3", "/url3");
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(3, pages.size());
	}	
	
	public void testUpdate() {
		PageEntity page = pageTool.addPage("title1", "/url1");
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		page2.setTitle("update");
		getDao().getPageDao().save(page2);
		PageEntity page3 = getDao().getPageDao().getById(page.getId());
		assertEquals("update", page3.getTitle());
	}
	
	public void testResultList() {
		pageTool.addPage("title1", "/url1");
		pageTool.addPage("title2", "/url2");
		pageTool.addPage("title3", "/url3");
		List<PageEntity> pages = getDao().getPageDao().select();
		PageEntity page = new PageEntity("title", "/url", null);
		pages.add(page);
		assertEquals(4, pages.size());
	}

	public void testGetByParent() {
		pageTool.addPage("root", "/");
		PageEntity page = pageTool.addPage("title1", "/url1");
		assertEquals("/", page.getParentUrl());
		page = pageTool.addPage("title2", "/url1/2");
		assertEquals("/url1", page.getParentUrl());
		page = pageTool.addPage("title3", "/url1/3");
		assertEquals("/url1", page.getParentUrl());
		List<PageEntity> pages = getDao().getPageDao().getByParent("/");
		assertEquals(1, pages.size());
		pages = getDao().getPageDao().getByParent("/url1");
		assertEquals(2, pages.size());
	}	
	
	public void testGetByUrl() {
		PageEntity root = pageTool.addPage("root", "/");
		pageTool.addPage("title1", "/url1");
		pageTool.addPage("title2", "/url2");
		pageTool.addPage("title3", "/url3");
		PageEntity page = getDao().getPageDao().getByUrl("/url3");
		assertNotNull(page);
		assertEquals("title3", page.getTitle());
	}	

	public void testContent() {
		PageEntity root = pageTool.addPage("root", "/");
		getDao().getPageDao().setContent(root.getId(), "en", "english");
		getDao().getPageDao().setContent(root.getId(), "ru", "russian");
		getDao().getPageDao().setContent(root.getId(), "uk", "ukranian");
		String c = getDao().getPageDao().getContent(root.getId(), "en");
		assertEquals("english", c);
		c = getDao().getPageDao().getContent(root.getId(), "ru");
		assertEquals("russian", c);
		c = getDao().getPageDao().getContent(root.getId(), "uk");
		assertEquals("ukranian", c);
		List<ContentEntity> list = getDao().getContentDao().select(
				PageEntity.class.getName(), root.getId());
		assertEquals(3, list.size());
	}
	
}
