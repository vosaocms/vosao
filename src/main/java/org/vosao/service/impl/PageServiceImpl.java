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

package org.vosao.service.impl;

import org.vosao.entity.PageEntity;
import org.vosao.service.PageService;
import org.vosao.service.ServiceResponse;

public class PageServiceImpl extends AbstractServiceImpl 
		implements PageService {

	@Override
	public ServiceResponse updateContent(String pageId, String content) {
		PageEntity page = getDao().getPageDao().getById(pageId);
		if (page != null) {
			page.setContent(content);
			getDao().getPageDao().save(page);
			return ServiceResponse.createSuccessResponse(
					"Page was successfully updated");
		}
		else {
			return ServiceResponse.createErrorResponse("Page not found " 
					+ pageId);
		}
	}

}
