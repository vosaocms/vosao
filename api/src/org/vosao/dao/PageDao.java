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

import java.util.Date;
import java.util.List;

import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;

public interface PageDao extends BaseDao<PageEntity> {

	/**
	 * Select all children versions for parent page.
	 * @param parentUrl - parent page url.
	 * @return pages list.
	 */
	List<PageEntity> selectAllChildren(final String parentUrl);
	
	/**
	 * Select all children versions for parent page with publishDate in period
	 * between startDate and endDate.
	 * @param parentUrl - parent page url.
	 * @param startDate - period start date (inclusive).
	 * @param endDate - period end date (exclusive).
	 * @return pages list.
	 */
	List<PageEntity> selectAllChildren(final String parentUrl,
			final Date startDate, final Date endDate);

	/**
	 * Select pages latest versions by parent page.
	 * @param url
	 * @return
	 */
	List<PageEntity> getByParent(final String url);

	/**
	 * Select pages approved latest versions by parent page.
	 * @param url
	 * @return
	 */
	List<PageEntity> getByParentApproved(final String url);

	/**
	 * Select pages approved latest versions by parent page.
	 * @param url
	 * @param startDate - period start date (inclusive).
	 * @param endDate - period end date (exclusive).
	 * @return
	 */
	List<PageEntity> getByParentApproved(final String url, Date startDate,
			Date endDate);

	/**
	 * Get latest version approved page by url.
	 * @param url
	 * @return page
	 */
	PageEntity getByUrl(final String url);

	PageEntity getByUrlVersion(final String url, final Integer version);

	String getContent(final Long pageId, final String languageCode);

	ContentEntity setContent(final Long pageId, final String languageCode, 
			final String content);
	
	List<ContentEntity> getContents(final Long pageId);
	
	/**
	 * Selects all page's versions ordered by version.
	 * @param url - friendly url.
	 * @return - list of pages.
	 */
	List<PageEntity> selectByUrl(final String url);
	
	List<PageEntity> selectByTemplate(Long templateId);
	
	List<PageEntity> selectByStructure(Long structureId);

	List<PageEntity> selectByStructureTemplate(Long structureTemplateId);
	
	void removeVersion(Long id);
	
	List<PageEntity> getCurrentHourPublishedPages();

	List<PageEntity> getCurrentHourUnpublishedPages();
	
}
