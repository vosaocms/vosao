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

package org.vosao.velocity.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.SetupBean;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.UserEntity;
import org.vosao.utils.FolderUtil;
import org.vosao.utils.StrUtil;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class VosaoTool {

	private static final Log logger = LogFactory.getLog(VosaoTool.class);
	
	private PageEntity page;
	
	public VosaoTool() {
	}
	
	public String getTextContent(String content, int start, int size) {
		String text = StrUtil.extractTextFromHTML(content);
		if (text.length() <= start) {
			return "";
		}
		int end = start + size;
		if (end > text.length()) {
			end = text.length();
		}
		return text.substring(start, end);
	}

	public List reverse(List list) {
		Collections.reverse(list);
		return list;
	}
	
	public boolean isLoggedIn() {
		return VosaoContext.getInstance().getUser() != null;
	}
	
	public UserEntity getUser() {
		return VosaoContext.getInstance().getUser();
	}
	
	public String getFullVersion() {
		return SetupBean.FULLVERSION;
	}
	
	public void setSessionAttribute(String name, Object value) {
		VosaoContext.getInstance().getRequest().getSession().setAttribute(
				name, value);
	}
	
	public String getRestParam(int index) {
		if (index < 1) {
			throw new IllegalArgumentException();
		}
		if (page != null) {
			String url = VosaoContext.getInstance().getRequest().getServletPath();
			String paramsUrl = url.substring(page.getFriendlyURL().length(), 
					url.length());
			String[] params = FolderUtil.getPathChain(paramsUrl);
			if (index <= params.length) {
				return params[index - 1];
			}
		}
		else {
			logger.info("Page is null");
		}
		return null;
	}

	public void setPage(PageEntity page) {
		this.page = page;
	}
}
