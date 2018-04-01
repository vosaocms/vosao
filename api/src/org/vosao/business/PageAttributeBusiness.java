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

package org.vosao.business;

import java.util.List;

import org.vosao.entity.PageAttributeEntity;
import org.vosao.entity.PageEntity;

public interface PageAttributeBusiness {
	
	/**
	 * Get all page attributes definitions including inherited from parent pages.
	 * @param pageUrl - page friendly URL.
	 * @return list of found page attributes definitions.
	 */
	List<PageAttributeEntity> getByPage(final String pageUrl);
	
	/**
	 * Get page attribute definition including inherited from parent pages.
	 * @param pageUrl - page friendly URL.
	 * @param name - attribute name.
	 * @return list of found page attributes definitions.
	 */
	PageAttributeEntity getByPage(final String pageUrl, final String name);

	void setAttribute(PageEntity page, String name, String language,
			String value, boolean applyToChildren);

	List<String> validateBeforeUpdate(PageAttributeEntity entity); 
	
}
