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

import java.util.List;

import org.vosao.entity.CommentEntity;

/**
 * @author Alexander Oleynik
 */
public interface CommentDao extends AbstractDao {

	void save(final CommentEntity page);
	
	CommentEntity getById(final String id);

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

	void remove(final String id);
	
	void remove(final List<String> ids);
	
	void enable(final List<String> ids);
	
	void disable(final List<String> ids);
}
