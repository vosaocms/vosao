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

package org.vosao.dao.tool;

import java.util.Date;

import org.vosao.dao.Dao;
import org.vosao.entity.CommentEntity;
import org.vosao.entity.PageEntity;

public class CommentTool {

	private Dao dao;
	
	public CommentTool(Dao aDao) {
		dao = aDao;
	}
	
	public CommentEntity addComment(final String name, final String content, 
			final PageEntity page) {
		CommentEntity comment = new CommentEntity(name, content, new Date(), 
				page.getFriendlyURL());
		dao.getCommentDao().save(comment);
		return comment;
	}
	
	public CommentEntity addComment(final String name, final String content, 
			final PageEntity page, final boolean disabled) {
		CommentEntity comment = new CommentEntity(name, content, new Date(), 
				page.getFriendlyURL());
		comment.setDisabled(disabled);
		dao.getCommentDao().save(comment);
		return comment;
	}
	
}
