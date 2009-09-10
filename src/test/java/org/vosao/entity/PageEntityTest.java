package org.vosao.entity;

import junit.framework.TestCase;

public class PageEntityTest extends TestCase {

	public void testGetParentFriendlyURL() {
		PageEntity page = new PageEntity();
		page.setFriendlyURL("/");
		assertEquals("", page.getParentFriendlyURL());
		assertEquals("", page.getPageFriendlyURL());
		page.setFriendlyURL("/rent");
		assertEquals("", page.getParentFriendlyURL());
		assertEquals("rent", page.getPageFriendlyURL());
		page.setFriendlyURL("/parent/page");
		assertEquals("/parent", page.getParentFriendlyURL());
		assertEquals("page", page.getPageFriendlyURL());
		page.setFriendlyURL("/parent/page/subpage");
		assertEquals("/parent/page", page.getParentFriendlyURL());
		assertEquals("subpage", page.getPageFriendlyURL());
	}

	public void testSetPageFriendlyURL() {
		PageEntity page = new PageEntity();
		page.setFriendlyURL("/");
		page.setPageFriendlyURL("test");
		assertEquals("/test", page.getFriendlyURL());
		page.setPageFriendlyURL("friend");
		assertEquals("/friend", page.getFriendlyURL());
		page.setFriendlyURL("/page/friend/first");
		page.setPageFriendlyURL("second");
		assertEquals("/page/friend/second", page.getFriendlyURL());
	}

}
