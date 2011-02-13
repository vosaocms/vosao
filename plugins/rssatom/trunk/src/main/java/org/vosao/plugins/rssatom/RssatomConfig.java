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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.PluginEntity;
import org.vosao.entity.helper.PluginHelper;
import org.vosao.entity.helper.PluginParameter;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class RssatomConfig {

	protected static final Log logger = LogFactory.getLog(RssatomConfig.class);
	
	private int items;
	private int itemSize;
	private String pages;
	private String title;
	private String rssTemplate;
	private String atomTemplate;
	private String rewriteUrl;

	private RssatomConfig() {
		items = 10;
		itemSize = 300;
		pages = "/";
		title = "";
	}
	
	public RssatomConfig(PluginEntity plugin) {
		this();
		Map<String, PluginParameter> params = PluginHelper.parseParameters(
				plugin);
		try {
			int newItems = params.get("items").getValueInteger();
			if (newItems > 0) {
				items = newItems;
			}
		}
		catch (Exception e) {
			logger.error("items parameter: " + e.getMessage());
		}
		try {
			int newItemSize = params.get("itemSize").getValueInteger();
			if (newItemSize > 0) {
				itemSize = newItemSize;
			}
		}
		catch (Exception e) {
			logger.error("itemSize parameter: " + e.getMessage());
		}
		try {
			String newPages = params.get("pages").getValue();
			if (StringUtils.isNotEmpty(newPages)) {
				pages = newPages;
			}
		}
		catch (Exception e) {
			logger.error("pages parameter: " + e.getMessage());
		}
		try {
			title = params.get("title").getValue();
		}
		catch (Exception e) {
			logger.error("title parameter: " + e.getMessage());
		}
		try {
			rssTemplate = params.get("rssTemplate").getValue();
		}
		catch (Exception e) {
			logger.error("rssTemplate parameter: " + e.getMessage());
		}
		try {
			atomTemplate = params.get("atomTemplate").getValue();
		}
		catch (Exception e) {
			logger.error("atomTemplate parameter: " + e.getMessage());
		}
		try {
			rewriteUrl = params.get("rewriteUrl").getValue();
		}
		catch (Exception e) {
			logger.error("rewriteUrl parameter: " + e.getMessage());
		}
	}

	public int getItems() {
		return items;
	}

	public String getPages() {
		return pages == null ? "" : pages;
	}
	
	public List<String> getPagesList() {
		return Arrays.asList(getPages().replace(" ", "").split(","));
	}

	public String getRssTemplate() {
		return rssTemplate;
	}

	public String getAtomTemplate() {
		return atomTemplate;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public void setRssTemplate(String rssTemplate) {
		this.rssTemplate = rssTemplate;
	}

	public void setAtomTemplate(String atomTemplate) {
		this.atomTemplate = atomTemplate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getItemSize() {
		return itemSize;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public String getRewriteUrl() {
		return rewriteUrl;
	}

	public void setRewriteUrl(String rewriteUrl) {
		this.rewriteUrl = rewriteUrl;
	}
	
}
