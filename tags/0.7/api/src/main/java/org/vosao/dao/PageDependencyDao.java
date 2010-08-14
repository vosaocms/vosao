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

import org.vosao.entity.PageDependencyEntity;

/**
 * @author Alexander Oleynik
 */
public interface PageDependencyDao extends BaseDao<PageDependencyEntity> {

	/**
	 * Get dependencies by page URL.
	 * @param pageUrl - page friendlyURL.
	 * @return found PageDependencys.
	 */
	List<PageDependencyEntity> selectByPage(final String pageUrl);
	
	/**
	 * Get dependencies by dependency URL.
	 * @param pageUrl - dependency page friendlyURL.
	 * @return found PageDependencys.
	 */
	List<PageDependencyEntity> selectByDependency(final String pageUrl);

	PageDependencyEntity getByPageAndDependency(String pageUrl, 
			String dependency);
	
}
