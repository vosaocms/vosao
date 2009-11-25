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

import java.util.List;

import org.apache.velocity.VelocityContext;
import org.vosao.business.decorators.TreeItemDecorator;
import org.vosao.business.impl.PageRenderDecorator;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.UserEntity;


public interface PageBusiness {

	ConfigBusiness getConfigBusiness();
	void setConfigBusiness(ConfigBusiness bean);
	
	MessageBusiness getMessageBusiness();
	void setMessageBusiness(MessageBusiness bean);
	
	ContentPermissionBusiness getContentPermissionBusiness();
	void setContentPermissionBusiness(ContentPermissionBusiness bean);

	/**
	 * Security filtered dao version.
	 * @return found page.
	 */
	PageEntity getById(final String id);
	
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
	void remove(final List<String> ids);

	/**
	 * Security filtered dao version.
	 */
	List<ContentEntity> getContents(final String pageId);

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
	
}
