package org.vosao.dao;

import java.util.List;

import org.vosao.entity.PageEntity;

import com.google.appengine.api.datastore.Key;

public class PageDaoTest extends AbstractDaoTest {

	private PageEntity addPage(final String title, final String content, 
			final String url,final Key parent) {
		PageEntity page = new PageEntity(title, content, url, parent);
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
	
	
}
