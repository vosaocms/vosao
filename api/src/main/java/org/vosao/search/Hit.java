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

package org.vosao.search;

import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;

public class Hit {

	private String content;
	private String title;
	private String localTitle;
	private String url;
	
	public Hit(PageEntity page, String aContent, String language) {
		super();
		title = page.getTitle();
		localTitle = page.getLocalTitle(language);
		url = page.getFriendlyURL() + "?lanaguge=" + language;
		content = aContent;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocalTitle() {
		return localTitle;
	}

	public void setLocalTitle(String localTitle) {
		this.localTitle = localTitle;
	}
	
}
