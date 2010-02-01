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

import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;

public interface PageDao extends BaseDao<String, PageEntity> {

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
	 * Get latest version approved page by url.
	 * @param url
	 * @return page
	 */
	PageEntity getByUrl(final String url);

	PageEntity getByUrlVersion(final String url, final Integer version);

	String getContent(final String pageId, final String languageCode);

	void setContent(final String pageId, final String languageCode, 
			final String content);
	
	ContentDao getContentDao();

	void setContentDao(ContentDao bean);
	
	List<ContentEntity> getContents(final String pageId);
	
	/**
	 * Selects all page's versions ordered by version.
	 * @param url - friendly url.
	 * @return - list of pages.
	 */
	List<PageEntity> selectByUrl(final String url);
	
	List<PageEntity> selectByTemplate(String templateId);
	
	List<PageEntity> selectByStructure(String structureId);

	List<PageEntity> selectByStructureTemplate(String structureTemplateId);
	
}
