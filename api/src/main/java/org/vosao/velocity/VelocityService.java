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

package org.vosao.velocity;

import java.util.Date;
import java.util.List;

import org.vosao.entity.PageEntity;
import org.vosao.service.vo.CommentVO;
import org.vosao.service.vo.FileVO;
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
	 * friendlyURL. Ordered by publishDate.
	 * @param path - friendly url.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path);

	/**
	 * Find approved last versions of children pages entity by parent pages
	 * friendlyURL. Ordered by publishDate.
	 * @param paths - friendly urls.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final List<String> paths);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL. Ordered by publishDate.
	 * @param path - friendly url.
	 * @param count - maximum list size.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, final int count);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL. Ordered by publishDate.
	 * @param path - friendly url.
	 * @param start - start page index. 0 - based.
	 * @param count - maximum list size.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, final int start, 
			final int count);

	/**
	 * Find approved last versions of children pages entity by parent pages
	 * friendlyURL. Ordered by publishDate.
	 * @param paths - friendly urls.
	 * @param start - start page index. 0 - based.
	 * @param count - maximum list size.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final List<String> paths, final int start, 
			final int count);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL and publish date.
	 * @param path - friendly url.
	 * @param publishDate - publishDate.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, 
			final Date publishDate);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL and publish date in period between startDate end endDate.
	 * @param path - friendly url.
	 * @param startDate - period start publish date (inclusive).
	 * @param endDate - period end publish date (exclusive).
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildren(final String path, 
			final Date startDate, final Date endDate);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL and publish date in period adjusted by year and month.
	 * @param path - friendly url.
	 * @param year - year of period.
	 * @param month - month of period.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildrenMonth(final String path, 
			final int year, final int month);

	/**
	 * Find approved last versions of children pages entity by parent pages
	 * friendlyURL and publish date in period adjusted by year and month.
	 * @param paths - friendly urls.
	 * @param year - year of period.
	 * @param month - month of period.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildrenMonth(final List<String> paths, 
			final int year, final int month);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL. Ordered by sortIndex.
	 * @param path - friendly url.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildrenOrdered(final String path);

	/**
	 * Find approved last versions of children pages entity by parent page
	 * friendlyURL. Ordered by sortIndex.
	 * @param path - friendly url.
	 * @param count - maximum list size.
	 * @return list of found pages.
	 */
	List<PageEntity> findPageChildrenOrdered(final String path, final int count);

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

	/**
	 * Find approved last version structured page field content by friendlyURL 
	 * and current language.
	 * @param path - page friendlyURL.
	 * @param field - structured page field.
	 * @return found content.
	 */
	String findStructureContent(String path, String field);

	/**
	 * Find approved last version structured page field content by friendlyURL 
	 * and specified language.
	 * @param path - page friendlyURL.
	 * @param field - structured page field.
	 * @param languageCode - two char language code. For example "ru".
	 * @return found content.
	 */
	String findStructureContent(String path, String field, String languageCode);
	
	TagVelocityService getTag();
	
	PicasaVelocityService getPicasa();
	
	/**
	 * Render structure page specified by path using structure template 
	 * specified by unique structureTemplateName.
	 * @param path - page path.
	 * @param structureTemplateName - structure template name.
	 * @return - rendered content.
	 */
	String renderStructureContent(String path, String structureTemplateName);
	
	/**
	 * Get all page's resources.
	 * @param url - page friendly url.
	 * @return - list of page's file resources.
	 */
	List<FileVO> getPageResources(String url);

	/**
	 * Get all resources by folder path.
	 * @param path - folder path.
	 * @return - list of file resources.
	 */
	List<FileVO> getResources(String path);
	
	List<CommentVO> getRecentComments(int limit);
}
