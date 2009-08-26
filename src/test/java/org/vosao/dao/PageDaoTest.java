package org.vosao.dao;

import java.util.List;

import org.vosao.entity.PageEntity;

public class PageDaoTest extends AbstractDaoTest {

	private PageEntity addPage(final String title, final String content, 
			final String url,final PageEntity parent) {
		PageEntity page = new PageEntity(title, content, url, 
				parent == null ? null : parent.getId());
		getDao().getPageDao().save(page);
		return page;
	}
	
	public void testSave() {
		addPage("title", "content", "/url", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(1, pages.size());
		PageEntity page1 = pages.get(0);
		assertEquals("title", page1.getTitle());
	}	
	
	public void testGetById() {
		PageEntity page = addPage("title","content","/url",null);
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		assertEquals(page.getTitle(), page2.getTitle());
		assertEquals(page.getContent(), page2.getContent());
		assertEquals(page.getFriendlyURL(), page2.getFriendlyURL());
		assertEquals(page.getParent(), page2.getParent());
	}	

	public void testSelect() {
		addPage("title1", "content1", "/url1", null);
		addPage("title2", "content2", "/url2", null);
		addPage("title3", "content3", "/url3", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		assertEquals(3, pages.size());
	}	
	
	public void testUpdate() {
		PageEntity page = addPage("title1", "content1", "/url1", null);
		assertNotNull(page.getId());
		PageEntity page2 = getDao().getPageDao().getById(page.getId());
		page2.setTitle("update");
		getDao().getPageDao().save(page2);
		PageEntity page3 = getDao().getPageDao().getById(page.getId());
		assertEquals("update", page3.getTitle());
	}
	
	public void testResultList() {
		addPage("title1", "content1", "/url1", null);
		addPage("title2", "content2", "/url2", null);
		addPage("title3", "content3", "/url3", null);
		List<PageEntity> pages = getDao().getPageDao().select();
		PageEntity page = new PageEntity("title", "content", "/url", null);
		pages.add(page);
		assertEquals(4, pages.size());
	}

	public void testGetByParent() {
		PageEntity root = addPage("root", "root content1", "/", null);
		addPage("title1", "content1", "/url1", root);
		addPage("title2", "content2", "/url2", null);
		addPage("title3", "content3", "/url3", root);
		List<PageEntity> pages = getDao().getPageDao().getByParent(root.getId());
		assertEquals(2, pages.size());
	}	
	
	public void testGetByUrl() {
		PageEntity root = addPage("root", "root content1", "/", null);
		addPage("title1", "content1", "/url1", root);
		addPage("title2", "content2", "/url2", null);
		addPage("title3", "content3", "/url3", root);
		PageEntity page = getDao().getPageDao().getByUrl("/url3");
		assertNotNull(page);
		assertEquals("title3", page.getTitle());
	}	
	
}
