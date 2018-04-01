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

package org.vosao.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.vosao.dao.tool.PageTool;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.enums.PageState;
import org.vosao.utils.DateUtil;
import org.vosao.utils.StrUtil;

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
		ContentEntity ruContent = getDao().getPageDao().setContent(
				root.getId(), "ru", "russian");
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
		getDao().getContentDao().removeById(ruContent.getParentClass(), 
				root.getId());
		list = getDao().getContentDao().select(
				PageEntity.class.getName(), root.getId());
		assertEquals(0, list.size());
	}
	
	public void testGetByParentApproved() {
		pageTool.addPage("root", "/");
		pageTool.addPage("title1", "/url1");
		pageTool.addPage("about1", "/url1/about1");
		pageTool.addPage("about2", "/url1/about2");
		pageTool.addPage("about3", "/url1/about3", PageState.EDIT);
		pageTool.addPage("title2", "/url2");
		pageTool.addPage("title3", "/url3", PageState.EDIT);
		List<PageEntity> list = getDao().getPageDao().getByParentApproved("/");
		assertEquals(2, list.size());
		list = getDao().getPageDao().getByParentApproved("/url1");
		assertEquals(2, list.size());
	}

	public void testGetByUrlVersion() {
		pageTool.addPage("root", "/");
		pageTool.addPage("title1", "/url1");
		pageTool.addPage("about1", "/url1/about1");
		pageTool.addPage("about2", "/url1/about2");
		pageTool.addPage("about3", "/url1/about3", PageState.EDIT);
		pageTool.addPage("title2", "/url2");
		pageTool.addPage("title3", "/url3", PageState.EDIT);
		PageEntity p = pageTool.addPage("about2", "/url1/about2");
		p.setVersion(2);
		p.setState(PageState.EDIT);
		getDao().getPageDao().save(p);
		p = pageTool.addPage("about2", "/url1/about2");
		p.setVersion(3);
		getDao().getPageDao().save(p);
		p = getDao().getPageDao().getByUrlVersion("/url1/about2", 1);
		assertNotNull(p);
		assertEquals(new Integer(1), p.getVersion());
		p = getDao().getPageDao().getByUrlVersion("/url1/about2", 2);
		assertNotNull(p);
		assertEquals(new Integer(2), p.getVersion());
		p = getDao().getPageDao().getByUrlVersion("/url1/about2", 3);
		assertNotNull(p);
		assertEquals(new Integer(3), p.getVersion());
		p = getDao().getPageDao().getByUrlVersion("/url1/about2", 4);
		assertNull(p);
		p = getDao().getPageDao().getByUrlVersion(null, 4);
		assertNull(p);
		p = getDao().getPageDao().getByUrlVersion("/", null);
		assertNull(p);
		p = getDao().getPageDao().getByUrlVersion(null, null);
		assertNull(p);
	}
	
	public void testRemove() {
		PageEntity root = pageTool.addPage("root", "/");
		pageTool.addPage("test", "/test");
		pageTool.addPage("test2", "/test2");
		pageTool.addPage("megatest", "/test/mega");
		PageEntity root2 = pageTool.addPage("root", "/");
		root2.setVersion(2);
		root2.setState(PageState.EDIT);
		getDao().getPageDao().save(root2);
		PageEntity root3 = pageTool.addPage("root", "/");
		root2.setVersion(3);
		root2.setState(PageState.EDIT);
		getDao().getPageDao().save(root3);
		getDao().getPageDao().remove(root.getId());
		assertEquals(0, getDao().getPageDao().select().size());
	}

	public void testRemoveVersion() {
		PageEntity root = pageTool.addPage("root", "/");
		PageEntity root2 = pageTool.addPage("root", "/");
		root2.setVersion(2);
		root2.setState(PageState.EDIT);
		getDao().getPageDao().save(root2);
		PageEntity root3 = pageTool.addPage("root", "/");
		root3.setVersion(3);
		root3.setState(PageState.EDIT);
		getDao().getPageDao().save(root3);
		getDao().getPageDao().removeVersion(root3.getId());
		assertEquals(2, getDao().getPageDao().select().size());
	}

	private void addPage(String title, String url, Long templateId) {
		PageEntity page = new PageEntity(title, url, templateId, new Date());
		getDao().getPageDao().save(page);
	}
	
	public void testSelectByTemplate() {
		addPage("root", "/", 1L);
		addPage("test", "/test", 1L);
		addPage("test2", "/test2", 2L);
		addPage("megatest", "/test/mega", 3L);
		assertEquals(2, getDao().getPageDao().selectByTemplate(1L).size());
		assertEquals(1, getDao().getPageDao().selectByTemplate(3L).size());
	}
	
	private void addPageStructure(String title, String url, Long structureId) {
		PageEntity page = new PageEntity(title, url);
		page.setStructureId(structureId);
		getDao().getPageDao().save(page);
	}
	
	public void testSelectByStructure() {
		addPageStructure("root", "/", 1L);
		addPageStructure("test", "/test", 1L);
		addPageStructure("test2", "/test2", 2L);
		addPageStructure("megatest", "/test/mega", 3L);
		assertEquals(2, getDao().getPageDao().selectByStructure(1L).size());
		assertEquals(1, getDao().getPageDao().selectByStructure(3L).size());
	}
	
	private void addPageStructureTemplate(String title, String url, 
			Long structureTemplateId) {
		PageEntity page = new PageEntity(title, url);
		page.setStructureTemplateId(structureTemplateId);
		getDao().getPageDao().save(page);
	}
	
	public void testSelectByStructureTemplate() {
		addPageStructureTemplate("root", "/", 1L);
		addPageStructureTemplate("test", "/test", 1L);
		addPageStructureTemplate("test2", "/test2", 2L);
		addPageStructureTemplate("megatest", "/test/mega", 3L);
		assertEquals(2, getDao().getPageDao().selectByStructureTemplate(1L).size());
		assertEquals(1, getDao().getPageDao().selectByStructureTemplate(3L).size());
	}

	private PageEntity addPage(String title, Date publishDate) {
		PageEntity page = new PageEntity(title, "/" + title);
		page.setPublishDate(publishDate);
		page.setParentUrl("/");
		page.setState(PageState.APPROVED);
		return getDao().getPageDao().save(page);
	}
	
	public void testGetByParentApprovedDate() {
		try {
			PageEntity page1 = addPage("test1", DateUtil.toDate("01.01.2010"));
			PageEntity page2 = addPage("test2", DateUtil.toDate("10.01.2010"));
			PageEntity page3 = addPage("test3", DateUtil.toDate("01.03.2010"));
			Date start = DateUtil.toDate("01.01.2010");
			Date end = DateUtil.toDate("01.02.2010");
			List<PageEntity> list = getDao().getPageDao().getByParentApproved(
					"/", start, end);
			assertEquals(2, list.size());
		}
		catch (ParseException e) {
		}
	}
	
	public void testGetCurrentHourPublishedPages() throws ParseException {
		Date dt = DateUtils.addMinutes(new Date(), -15);
		Date dt2 = DateUtils.addHours(new Date(), 1);
		Date dt3 = DateUtils.addMinutes(new Date(), -30);
		PageEntity page1 = addPage("test1", dt);
		PageEntity page2 = addPage("test2", dt2);
		PageEntity page3 = addPage("test3", dt3);
		List<PageEntity> list = getDao().getPageDao().getCurrentHourPublishedPages();
		assertEquals(2, list.size());
	}
}
