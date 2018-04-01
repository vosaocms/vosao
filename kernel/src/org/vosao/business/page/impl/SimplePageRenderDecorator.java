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

package org.vosao.business.page.impl;

import org.apache.velocity.VelocityContext;
import org.vosao.business.PageBusiness;
import org.vosao.dao.Dao;
import org.vosao.entity.ContentEntity;
import org.vosao.entity.PageEntity;
import org.vosao.global.SystemService;
import org.vosao.i18n.Messages;

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
		String resultContent = contentEntity.getContent();
		if (isVelocityProcessing()) {
			VelocityContext context = getPageBusiness().createContext(
					getLanguageCode(), getPage());
			context.put("page", getPage());
			resultContent = getSystemService().render(resultContent, context);
		}
		if (isWikiProcessing()) {
			resultContent = getSystemService().renderWiki(resultContent, 
					getPage());
		}
		setContent(resultContent);
	}

}
