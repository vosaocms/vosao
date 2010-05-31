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

package org.vosao.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;
import org.vosao.dao.Dao;

/**
 * @author Alexander Oleynik
 */
public abstract class AbstractFilter {
	
    protected static final Log logger = LogFactory.getLog(AbstractFilter.class);

    private FilterConfig config;
	private ServletContext servletContext;

	private Dao dao;
	private Business business;

	public AbstractFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		servletContext = config.getServletContext();
		dao = (Dao) getSpringBean("dao");
		business = (Business) getSpringBean("business");
	}
	
	public void destroy() {
	}
	
	protected Object getSpringBean(final String name) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(
				servletContext).getBean(name);
	}
	
	public Dao getDao() {
		return dao;
	}

	public Business getBusiness() {
		return business;
	}

}
