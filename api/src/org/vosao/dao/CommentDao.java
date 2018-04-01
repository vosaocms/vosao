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

import java.util.List;

import org.vosao.entity.CommentEntity;

/**
 * @author Alexander Oleynik
 */
public interface CommentDao extends BaseDao<CommentEntity> {

	/**
	 * Get comments by page's friendlyURL.
	 * @param pageUrl - page friendlyURL.
	 * @return found comments ordered by publishDate desc.
	 */
	List<CommentEntity> getByPage(final String pageUrl);

	/**
	 * Get comments by page's friendlyURL and disabled flag value.
	 * @param pageUrl - page friendlyURL.
	 * @param disabled - disabled flag.
	 * @return found comments ordered by publishDate desc.
	 */
	List<CommentEntity> getByPage(final String pageUrl, boolean disabled);
	
	/**
	 * Get comments by page's friendlyURL and ordering by publishDate asc or desc.
	 * @param pageUrl - page friendlyURL.
	 * @param disabled - disabled flag.
	 * @param ascdesc - ordering
	 * @return found comments ordered by publishDate asc or desc.
	 */
	List<CommentEntity> getByPage(final String pageUrl, boolean disabled, String ascdesc);

	void enable(final List<Long> ids);
	
	void disable(final List<Long> ids);
	
	void removeByPage(final String url);
	
	List<CommentEntity> getRecent(int limit);
}
