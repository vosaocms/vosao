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

package org.vosao.plugins.rssatom;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.Business;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.PluginEntity;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class RssTool {

	private final String PAGE_TEMPLATE = "$page.content";
	
	private Business business;
	
	public RssTool(Business aBusiness) {
		business = aBusiness;
	}
	
	private Business getBusiness() {
		return business;
	}
	
	private PluginEntity getPlugin() {
		return getBusiness().getDao().getPluginDao().getByName("rssatom");
	}
	
	public String getDescription(PageEntity page) {
		RssatomConfig rssatomConfig = new RssatomConfig(getPlugin());
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		String lang = StringUtils.isEmpty(config.getDefaultLanguage()) ? 
				"en" : config.getDefaultLanguage();
		page.setSkipPostProcessing(true);
		String content = StrUtil.extractTextFromHTML(
				getBusiness().getPageBusiness().render(page, PAGE_TEMPLATE, lang));
		if (StringUtils.isEmpty(content)) {
			return "";
		}
		int end = content.length() > rssatomConfig.getItemSize() ? 
				rssatomConfig.getItemSize() : content.length();
		return content.substring(0, end - 1);
	}

	public String getUUID(PageEntity page) {
		return (new UUID(page.getCreateDate().getTime(), 
				page.getModDate().getTime())).toString();
	}

	public String getUUID() {
		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
		return (new UUID(config.getCreateDate().getTime(), 
				config.getModDate().getTime())).toString();
	}

}
