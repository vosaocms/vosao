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

package org.vosao.business.impl;

import java.util.Date;

import org.vosao.entity.PageEntity;

public interface PageRenderDecorator {

	PageEntity getPage();

	void setPage(PageEntity page);

	/**
	 * Get page id.
	 * @return page id.
	 */
	String getId();

	/**
	 * Get page content processed by velocity. 
	 * @return page content.
	 */
	String getContent();

	/**
	 * Get comments block for page. 
	 * @return comments block when comments are enabled for page.
	 */
	String getComments();

	/**
	 * Get page title.
	 * @return page title.
	 */
	String getTitle();

	/**
	 * Get page friendlyURL.
	 * @return page friendlyURL.
	 */
	String getFriendlyUrl();

	/**
	 * Get parent friendlyURL.
	 * @return parent friendlyURL. For root page returns empty page.
	 */
	String getParentUrl();

	/**
	 * Get template id for page.
	 * @return template id.
	 */
	String getTemplate();

	/**
	 * Get page publish date.
	 * @return publish date.
	 */
	Date getPublishDate();

	/**
	 * Get page publish date in dd.mm.yyyy format.
	 * @return publish date in dd.mm.yyyy format
	 */
	String getPublishDateString();

	/**
	 * Comments enabled for page.
	 * @return comments enabled or page flag.
	 */
	boolean isCommentsEnabled();

	/**
	 * Get page version.
	 * @return page version.
	 */
	Integer getVersion();

	/**
	 * Get version title.
	 * @return version title.
	 */
	String getVersionTitle();

    /**
     * Get page state.
	 * @return page state.
     */
	String getState();

	/**
	 * Get page created user email.
	 * @return page created user email.
	 */
	String getCreateUserEmail();

	/**
	 * Get page created date.
	 * @return page created date in dd.mm.yyyy format.
	 */
	String getCreateDate();

	/**
	 * Get modify user email.
	 * @return modify uer email.
	 */
	String getModUserEmail();

	/**
	 * Get page modification date.
	 * @return page modification date in dd.mm.yyyy format.
	 */
	String getModDate();

}