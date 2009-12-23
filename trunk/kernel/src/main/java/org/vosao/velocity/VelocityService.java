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

package org.vosao.velocity;

import java.util.Date;
import java.util.List;

import org.vosao.entity.PageEntity;
import org.vosao.service.vo.CommentVO;
import org.vosao.service.vo.UserVO;

/**
 * @author Alexander Oleynik
 */
public interface VelocityService {

	/**
	 * Find approved last version of page entity by friendlyURL.
	 * @param path - firnedly url.
	 * @return found page.
	 */
	PageEntity findPage(final String path);
	
	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL.
	 * @param path - friendly url.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL.
	 * @param path - friendly url.
	 * @param count - maximum list size.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, final int count);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL and publish date.
	 * @param path - firnedly url.
	 * @param publishDate - publishDate.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, 
			final Date publishDate);

	List<CommentVO> getCommentsByPage(final String pageUrl);
	
	/**
	 * Find approved last version page content by friendlyURL and language.
	 * @param path - page friendlyURL.
	 * @param languageCode - content language code.
	 * @return found content.
	 */
	String findContent(final String path, final String languageCode);

	/**
	 * Find approved last version page content by friendlyURL and current 
	 * language.
	 * @param path - page friendlyURL.
	 * @return found content.
	 */
	String findContent(final String path);

	/**
	 * Find approved last versions of children pages content by parent page 
	 * friendlyURL and language code.
	 * @param path - firnedly url.
	 * @param languageCode - language code.
	 * @return list of found contents.
	 */
	List<String> findChildrenContent(final String path, 
			final String languageCode);

	/**
	 * Find approved last versions of children pages content by parent page 
	 * friendlyURL and current language code.
	 * @param path - firnedly url.
	 * @return list of found contents.
	 */
	List<String> findChildrenContent(final String path);
	
	UserVO findUser(final String email);
	
}
