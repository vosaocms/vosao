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

package org.vosao.business;

import java.util.Date;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.PageRenderDecorator;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.UserEntity;
import org.vosao.velocity.VelocityService;


public interface PageBusiness {

	/**
	 * Security filtered dao version.
	 * @return found page.
	 */
	PageEntity getById(final Long id);
	
	/**
	 * Security filtered dao version.
	 * @return list of pages.
	 */
	List<PageEntity> select();

	/**
	 * Security filtered dao version.
	 * @return found page.
	 */
	PageEntity getByUrl(final String url);

	/**
	 * Security filtered dao version.
	 * @return list of pages.
	 */
	List<PageEntity> getByParent(final String url);

	/**
	 * Security filtered dao version.
	 */
	void remove(final List<Long> ids);

	/**
	 * Security filtered dao version.
	 */
	void removeVersion(Long id);

	/**
	 * Security filtered dao version.
	 */
	List<ContentEntity> getContents(final Long pageId);

	/**
	 * Security filtered dao version.
	 */
	List<PageEntity> selectByUrl(final String url);
	
	
	
	TreeItemDecorator<PageEntity> getTree(final List<PageEntity> pages);
		
	TreeItemDecorator<PageEntity> getTree();

	/**
	 * Render page with page bound template. With applied postProcessing and 
	 * using PageRenderDecorator.
	 * @param page - page to render
	 * @return rendered html.
	 */
	String render(final PageEntity page, final String languageCode);
	
	VelocityContext createContext(final String languageCode);
	
	PageRenderDecorator createPageRenderDecorator(final PageEntity page,
			final String languageCode);
	
	List<String> validateBeforeUpdate(final PageEntity page);
	
	ContentEntity getPageContent(final PageEntity page, 
			final String languageCode);
	
	/**
	 * Add new version of specified page.
	 * @param oldPage - page to create version from.
	 */
	PageEntity addVersion(final PageEntity oldPage, final Integer version, 
			final String versionTitle, final UserEntity user);

	boolean canChangeContent(String url, String languageCode);
	
	/**
	 * Save page content and update search index.
	 * @param page
	 * @param content
	 * @param language
	 */
	void saveContent(PageEntity page, String language, String content, 
			boolean oldSearchable, boolean searchable);
	
	/**
	 * Get next sort index for new page.
	 * @param friendlyURL - new page url.
	 */
	Integer getNextSortIndex(final String friendlyURL);
	
	/**
	 * Move page down in sort order.
	 * @param page
	 */
	void moveDown(PageEntity page);

	/**
	 * Move page up in sort order.
	 * @param page
	 */
	void moveUp(PageEntity page);
	
	/**
	 * Place page after refPage in sort order.
	 * @param page
	 * @param refPage
	 */
	void moveAfter(PageEntity page, PageEntity refPage);

	/**
	 * Place page before refPage in sort order.
	 * @param page
	 * @param refPage
	 */
	void moveBefore(PageEntity page, PageEntity refPage);

	VelocityService getVelocityService();

	/**
	 * Remove page by URL with all subpages and page resouces.
	 */
	void remove(String pageURL);

	/**
	 * Check for free url and if not then add suffix number.
	 * @param url - url to check
	 * @return - free page friendly URL.
	 */
	String makeUniquePageURL(String url);

	/**
	 * Move page to new friendlyURL. Also change friendlyURL recursively 
	 * for all children pages.
	 * @param page - page to change.
	 * @param friendlyURL - new friendlyURL. 
	 */
	void move(PageEntity page, String friendlyURL);
	
	/**
	 * Copy page with subpages to new parent URL.
	 */
	void copy(PageEntity page, String parentURL);
	
	void addVelocityTools(VelocityContext context);

}	
