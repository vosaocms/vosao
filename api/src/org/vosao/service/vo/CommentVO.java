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

package org.vosao.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.vosao.entity.CommentEntity;
import org.vosao.utils.DateUtil;

/**
 * Value object to be returned from services.
 * @author Alexander Oleynik
 */
public class CommentVO {

    private CommentEntity comment;

	public CommentVO(final CommentEntity entity) {
		comment = entity;
	}

	public static List<CommentVO> create(List<CommentEntity> list) {
		List<CommentVO> result = new ArrayList<CommentVO>();
		for (CommentEntity comment : list) {
			result.add(new CommentVO(comment));
		}
		return result;
	}

	public Long getId() {
		return comment.getId();
	}

	public String getPageUrl() {
		return comment.getPageUrl();
	}

	public String getName() {
		return comment.getName();
	}

	public String getContent() {
		
		// keeping carriage return in HTML 
		return comment.getContent().replace("\n", "<br/>");
		
	}

	public String getPublishDate() {
		return DateUtil.toString(comment.getPublishDate());
	}

	public String getPublishDateTime() {
		return DateUtil.dateTimeToString(comment.getPublishDate());
	}

	public Date getDate() {
		return comment.getPublishDate();
	}

	public boolean isDisabled() {
		return comment.isDisabled();
	}
	
}
