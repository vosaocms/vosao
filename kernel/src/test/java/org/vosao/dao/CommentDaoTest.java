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
import java.util.Date;
import java.util.List;

import org.vosao.entity.CommentEntity;
import org.vosao.entity.PageEntity;

public class CommentDaoTest extends AbstractDaoTest {

	private PageEntity addPage(final String name) {
		PageEntity page = new PageEntity(name, "/" + name);
		getDao().getPageDao().save(page);
		return page;
	}

	private CommentEntity addComment(final String name, final String content, 
			final PageEntity page) {
		CommentEntity comment = new CommentEntity(name, content, new Date(), 
				page.getFriendlyURL());
		getDao().getCommentDao().save(comment);
		return comment;
	}
	
	private CommentEntity addComment(final String name, final String content, 
			final PageEntity page, final boolean disabled) {
		CommentEntity comment = new CommentEntity(name, content, new Date(), 
				page.getFriendlyURL());
		comment.setDisabled(disabled);
		getDao().getCommentDao().save(comment);
		return comment;
	}
	
	public void testSave() {
		PageEntity page = addPage("test");
		CommentEntity comment = addComment("alex", "content", page);
		CommentEntity comment2 = getDao().getCommentDao().getById(
				comment.getId());
		assertNotNull(comment2);
		assertEquals("alex", comment2.getName());
		assertEquals("content", comment2.getContent());
	}	
	
	public void testUpdate() {
		PageEntity page = addPage("test");
		CommentEntity comment = addComment("alex", "content", page);
		CommentEntity comment2 = getDao().getCommentDao().getById(
				comment.getId());
		assertNotNull(comment2);
		assertEquals("alex", comment2.getName());
		assertEquals("content", comment2.getContent());
		comment2.setName("yuri");
		getDao().getCommentDao().save(comment2);
		CommentEntity comment3 = getDao().getCommentDao().getById(
				comment.getId());
		assertNotNull(comment3);
		assertEquals("yuri", comment3.getName());
	}
	
	public void testDelete() {
		PageEntity page = addPage("test");
		PageEntity page2 = addPage("test2");
		addComment("alex", "content1", page);
		addComment("yuri", "content2", page2);
		CommentEntity comment = addComment("roma", "content3", page);
		getDao().getCommentDao().remove(comment.getId());
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL());
		assertEquals(1, list.size());
		assertEquals("alex", list.get(0).getName());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL());
		assertEquals(1, list.size());
	}
	
	public void testGetByPage() {
		PageEntity page = addPage("test");
		PageEntity page2 = addPage("test2");
		addComment("alex", "content1", page);
		addComment("yuri", "content2", page2);
		addComment("roma", "content3", page);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL());
		assertEquals(2, list.size());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL());
		assertEquals(1, list.size());
	}	
	
	public void testGetByPage2() {
		PageEntity page = addPage("test");
		PageEntity page2 = addPage("test2");
		addComment("alex", "content1", page);
		addComment("yuri", "content2", page2);
		addComment("roma", "content3", page);
		addComment("roma1", "content4", page, true);
		addComment("roma2", "content5", page2, true);
		addComment("roma3", "content6", page, true);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL(), false);
		assertEquals(2, list.size());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL(), false);
		assertEquals(1, list.size());
	}	
	
	public void testGetById()  {
		PageEntity page = addPage("test");
		CommentEntity c = addComment("alex", "content1", page);
		CommentEntity c2 = getDao().getCommentDao().getById(null);
		assertNull(c2);
		c2 = getDao().getCommentDao().getById(c.getId());
		assertNotNull(c2);
		assertEquals(c.getId(), c2.getId());
	}

	public void testDisableEnable() {
		PageEntity page = addPage("test");
		CommentEntity alex = addComment("alex", "content1", page);
		CommentEntity roma = addComment("roma", "content3", page);
		CommentEntity roma1 = addComment("roma1", "content4", page);
		CommentEntity roma3 = addComment("roma3", "content6", page);
		List<String> ids = new ArrayList<String>();
		ids.add(alex.getId());
		ids.add(roma.getId());
		getDao().getCommentDao().disable(ids);
		CommentEntity r = getDao().getCommentDao().getById(alex.getId());
		assertTrue(r.isDisabled());
		r = getDao().getCommentDao().getById(roma.getId());
		assertTrue(r.isDisabled());
		r = getDao().getCommentDao().getById(roma1.getId());
		assertFalse(r.isDisabled());
		r = getDao().getCommentDao().getById(roma3.getId());
		assertFalse(r.isDisabled());
		getDao().getCommentDao().enable(ids);
		r = getDao().getCommentDao().getById(alex.getId());
		assertFalse(r.isDisabled());
		r = getDao().getCommentDao().getById(roma.getId());
		assertFalse(r.isDisabled());
	}	
	
}
