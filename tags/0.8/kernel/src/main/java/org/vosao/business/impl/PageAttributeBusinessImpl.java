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

package org.vosao.business.impl;

import java.util.List;

import org.vosao.business.PageAttributeBusiness;
import org.vosao.business.page.impl.PageSetAttributeMessage;
import org.vosao.entity.PageAttributeEntity;
import org.vosao.entity.PageEntity;
import org.vosao.utils.FolderUtil;

/**
 * @author Alexander Oleynik
 */
public class PageAttributeBusinessImpl extends AbstractBusinessImpl 
	implements PageAttributeBusiness {

	@Override
	public List<PageAttributeEntity> getByPage(String pageUrl) {
		if (pageUrl.equals("/")) {
			return getDao().getPageAttributeDao().getByPage(pageUrl);
		}
		List<PageAttributeEntity> result = getDao().getPageAttributeDao()
				.getByPageInherited("/");
		String[] paths = FolderUtil.getPathChain(pageUrl);
		String url = "";
		for (String path : paths) {
			url = url + "/" + path;
			if (url.equals(pageUrl)) {
				result.addAll(getDao().getPageAttributeDao()
						.getByPage(url));
			}
			else {
				result.addAll(getDao().getPageAttributeDao()
						.getByPageInherited(url));
			}
		}
		return result;
	}

	@Override
	public PageAttributeEntity getByPage(String pageUrl, String name) {
		PageAttributeEntity result = getDao().getPageAttributeDao()
				.getByPageName("/", name);
		if (result != null) {
			return result;
		}
		String[] paths = FolderUtil.getPathChain(pageUrl);
		String url = "";
		for (String path : paths) {
			url = url + "/" + path;
			result = getDao().getPageAttributeDao().getByPageName(url, name);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public void setAttribute(PageEntity page, String name, String language,
			String value, boolean applyToChildren) {
		PageAttributeEntity attribute = getByPage(page.getFriendlyURL(), name);
		if (attribute == null) {
			logger.error("Attribute definition: " + name
					+ " not found for page " + page.getFriendlyURL());
			return;
		}
		page.setAttribute(name, language, value);
		getDao().getPageDao().save(page);
		if (applyToChildren && attribute.isInherited()) {
			getBusiness().getMessageQueue().publish(
					new PageSetAttributeMessage(page.getFriendlyURL(), name, 
							language, value));
		}
	}

	
}
