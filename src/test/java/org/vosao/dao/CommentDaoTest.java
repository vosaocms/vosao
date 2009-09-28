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

import java.util.Date;
import java.util.List;

import org.vosao.entity.CommentEntity;
import org.vosao.entity.PageEntity;

public class CommentDaoTest extends AbstractDaoTest {

	private PageEntity addPage(final String name) {
		PageEntity page = new PageEntity(name, name, "/" + name, null);
		getDao().getPageDao().save(page);
		return page;
	}

	private CommentEntity addComment(final String name, final String content, 
			final PageEntity page) {
		CommentEntity comment = new CommentEntity(name, content, new Date(), 
				page.getId());
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
				page.getId());
		assertEquals(1, list.size());
		assertEquals("alex", list.get(0).getName());
		list = getDao().getCommentDao().getByPage(page2.getId());
		assertEquals(1, list.size());
	}
	
	public void testGetByPage() {
		PageEntity page = addPage("test");
		PageEntity page2 = addPage("test2");
		addComment("alex", "content1", page);
		addComment("yuri", "content2", page2);
		addComment("roma", "content3", page);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getId());
		assertEquals(2, list.size());
		list = getDao().getCommentDao().getByPage(page2.getId());
		assertEquals(1, list.size());
	}	
	
	
}
