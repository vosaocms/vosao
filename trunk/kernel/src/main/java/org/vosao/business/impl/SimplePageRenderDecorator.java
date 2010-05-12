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

import org.apache.velocity.VelocityContext;
import org.vosao.business.PageBusiness;
import org.vosao.common.Messages;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.global.SystemService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class SimplePageRenderDecorator extends AbstractPageRenderDecorator {

	public SimplePageRenderDecorator(PageEntity page,	String languageCode, 
			Dao dao,
			PageBusiness pageBusiness, 
			SystemService systemService) {
		super();
		setPage(page);
		setLanguageCode(languageCode);
		setDao(dao);
		setPageBusiness(pageBusiness);
		setSystemService(systemService);
		prepareContent();
	}

	private void prepareContent() {
		ContentEntity contentEntity = getPageBusiness().getPageContent(
				getPage(), getLanguageCode());
		if (contentEntity == null) {
			String msg = Messages.get("content_not_found") + " " 
					+ getFriendlyURL() + " " + getLanguageCode();
			logger.error(msg);
			setContent(msg);
			return;
		}
		if (isVelocityProcessing()) {
			VelocityContext context = getPageBusiness().createContext(
					getLanguageCode());
			context.put("page", getPage());
			setContent(getSystemService().render(
					contentEntity.getContent(), context));
		}
		else {
			setContent(contentEntity.getContent());
		}
	}

}
