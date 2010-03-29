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
import java.util.List;

import org.vosao.dao.tool.CommentTool;
import org.vosao.dao.tool.PageTool;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.PageEntity;

public class CommentDaoTest extends AbstractDaoTest {

	private PageTool pageTool;
	private CommentTool commentTool;
	
	@Override
    public void setUp() throws Exception {
        super.setUp();
        pageTool = new PageTool(getDao());
        commentTool = new CommentTool(getDao());
	}    


	public void testSave() {
		PageEntity page = pageTool.addPage("test");
		CommentEntity comment = commentTool.addComment("alex", "content", page);
		CommentEntity comment2 = getDao().getCommentDao().getById((Long)null);
		assertNull(comment2);
		List<CommentEntity> comments = getDao().getCommentDao().getById((List<Long>)null);
		assertEquals(0, comments.size());
		comment2 = getDao().getCommentDao().getById(0L);
		assertNull(comment2);
		comment2 = getDao().getCommentDao().getById(
				comment.getId());
		assertNotNull(comment2);
		assertEquals("alex", comment2.getName());
		assertEquals("content", comment2.getContent());
	}	
	
	public void testUpdate() {
		PageEntity page = pageTool.addPage("test");
		CommentEntity comment = commentTool.addComment("alex", "content", page);
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
		PageEntity page = pageTool.addPage("test");
		PageEntity page2 = pageTool.addPage("test2");
		commentTool.addComment("alex", "content1", page);
		commentTool.addComment("yuri", "content2", page2);
		CommentEntity comment = commentTool.addComment("roma", "content3", page);
		getDao().getCommentDao().remove(comment.getId());
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL());
		assertEquals(1, list.size());
		assertEquals("alex", list.get(0).getName());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL());
		assertEquals(1, list.size());
	}
	
	public void testGetByPage() {
		PageEntity page = pageTool.addPage("test");
		PageEntity page2 = pageTool.addPage("test2");
		commentTool.addComment("alex", "content1", page);
		commentTool.addComment("yuri", "content2", page2);
		commentTool.addComment("roma", "content3", page);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL());
		assertEquals(2, list.size());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL());
		assertEquals(1, list.size());
	}	
	
	public void testGetByPage2() {
		PageEntity page = pageTool.addPage("test");
		PageEntity page2 = pageTool.addPage("test2");
		commentTool.addComment("alex", "content1", page);
		commentTool.addComment("yuri", "content2", page2);
		commentTool.addComment("roma", "content3", page);
		commentTool.addComment("roma1", "content4", page, true);
		commentTool.addComment("roma2", "content5", page2, true);
		commentTool.addComment("roma3", "content6", page, true);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL(), false);
		assertEquals(2, list.size());
		list = getDao().getCommentDao().getByPage(page2.getFriendlyURL(), false);
		assertEquals(1, list.size());
	}	
	
	public void testGetById()  {
		PageEntity page = pageTool.addPage("test");
		CommentEntity c = commentTool.addComment("alex", "content1", page);
		CommentEntity c2 = getDao().getCommentDao().getById((Long)null);
		assertNull(c2);
		c2 = getDao().getCommentDao().getById(c.getId());
		assertNotNull(c2);
		assertEquals(c.getId(), c2.getId());
	}

	public void testDisableEnable() {
		PageEntity page = pageTool.addPage("test");
		CommentEntity alex = commentTool.addComment("alex", "content1", page);
		CommentEntity roma = commentTool.addComment("roma", "content3", page);
		CommentEntity roma1 = commentTool.addComment("roma1", "content4", page);
		CommentEntity roma3 = commentTool.addComment("roma3", "content6", page);
		List<Long> ids = new ArrayList<Long>();
		ids.add(null);
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

	public void testRemoveByPage() {
		PageEntity page = pageTool.addPage("test");
		commentTool.addComment("yuri", "content1", page);
		commentTool.addComment("roma", "content2", page, true);
		List<CommentEntity> list = getDao().getCommentDao().getByPage(
				page.getFriendlyURL(), false);
		assertEquals(1, list.size());
		getDao().getCommentDao().removeByPage(page.getFriendlyURL());
		list = getDao().getCommentDao().getByPage(page.getFriendlyURL(), false);
		assertEquals(0, list.size());
		list = getDao().getCommentDao().getByPage(page.getFriendlyURL(), true);
		assertEquals(0, list.size());
	}
}
