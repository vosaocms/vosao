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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.ConfigBusiness;
import org.vosao.business.PageBusiness;
import org.vosao.entity.PageEntity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class PageRenderDecorator {

	private Log logger = LogFactory.getLog(PageRenderDecorator.class);
	
	private PageEntity page;
	private ConfigBusiness configBusiness;
	private PageBusiness pageBusiness;
	
	public PageRenderDecorator(PageEntity page,	ConfigBusiness configBusiness,
			PageBusiness pageBusiness) {
		super();
		this.page = page;
		this.configBusiness = configBusiness;
		this.pageBusiness = pageBusiness;
	}

	public PageEntity getPage() {
		return page;
	}
	
	public void setPage(PageEntity page) {
		this.page = page;
	}
	
	public String getId() {
		return page.getId();
	}

	public String getContent() {
		if (isCommentsEnabled()) {
			return page.getContent() + createCommentsHtml();
		}
		return page.getContent();
	}
	
	private String createCommentsHtml() {
		String commentsTemplate = getConfigBusiness().getConfig()
				.getCommentsTemplate();
		if (StringUtil.isEmpty(commentsTemplate)) {
			logger.error("comments template is empty");
			return "comments template is empty";
		}
		return getPageBusiness().render(commentsTemplate, page);
	}

	public String getTitle() {
		return page.getTitle();
	}

	public String getFriendlyUrl() {
		return page.getFriendlyURL();
	}

	public String getParent() {
		return page.getParent();
	}
	
	public String getTemplate() {
		return page.getTemplate();
	}
	
	public Date getPublishDate() {
		return page.getPublishDate();
	}
	
	public boolean isCommentsEnabled() {
		return page.isCommentsEnabled();
	}

	public ConfigBusiness getConfigBusiness() {
		return configBusiness;
	}

	public PageBusiness getPageBusiness() {
		return pageBusiness;
	}
	
}
