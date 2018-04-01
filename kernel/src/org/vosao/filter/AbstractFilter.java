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

package org.vosao.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.Business;
import org.vosao.business.mq.MessageQueue;
import org.vosao.common.VosaoContext;
import org.vosao.dao.Dao;
import org.vosao.global.SystemService;

/**
 * @author Alexander Oleynik
 */
public abstract class AbstractFilter {
	
    protected static final Log logger = LogFactory.getLog(AbstractFilter.class);

    private FilterConfig config;
	private ServletContext servletContext;

	public AbstractFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		servletContext = config.getServletContext();
	}
	
	public void destroy() {
	}
	
	protected Dao getDao() {
		return getBusiness().getDao();
	}

	protected Business getBusiness() {
		return VosaoContext.getInstance().getBusiness();
	}
	
	protected MessageQueue getMessageQueue() {
		return VosaoContext.getInstance().getMessageQueue();
	}

	protected SystemService getSystemService() {
		return getBusiness().getSystemService();
	}
	
	protected boolean isLoggedIn(final HttpServletRequest request) {
		return VosaoContext.getInstance().getSession().getString(
				AuthenticationFilter.USER_SESSION_ATTR) != null;
	}

}
