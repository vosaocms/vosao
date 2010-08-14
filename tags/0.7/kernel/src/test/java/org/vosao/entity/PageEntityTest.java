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

package org.vosao.entity;

import junit.framework.TestCase;

public class PageEntityTest extends TestCase {

	public void testGetParentFriendlyURL() {
		PageEntity page = new PageEntity();
		page.setFriendlyURL("/");
		assertEquals("", page.getParentFriendlyURL());
		assertEquals("", page.getPageFriendlyURL());
		page.setFriendlyURL("/rent");
		assertEquals("/", page.getParentFriendlyURL());
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

	public void testGetAncestorsURL() {
		PageEntity page = new PageEntity("test", "/");
		assertEquals(1, page.getAncestorsURL().size());
		assertEquals("/", page.getAncestorsURL().get(0));
		page = new PageEntity("test2", "/test");
		assertEquals(1, page.getAncestorsURL().size());
		assertEquals("/test", page.getAncestorsURL().get(0));
		page = new PageEntity("test3", "/test/more/than/this");
		assertEquals(4, page.getAncestorsURL().size());
		assertEquals("/test", page.getAncestorsURL().get(0));
		assertEquals("/test/more", page.getAncestorsURL().get(1));
		assertEquals("/test/more/than", page.getAncestorsURL().get(2));
		assertEquals("/test/more/than/this", page.getAncestorsURL().get(3));
	}
	
}
