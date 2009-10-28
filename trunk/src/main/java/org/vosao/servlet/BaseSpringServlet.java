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

package org.vosao.servlet;

import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;
import org.vosao.dao.Dao;

/**
 * Base spring servlet.
 * 
 * @author Aleksandr Oleynik
 */
public class BaseSpringServlet extends HttpServlet {

	/**
	 * Default constructor.
	 */
	public BaseSpringServlet() {
		super();
	}

	/**
	 * Getter for business Spring bean.
	 * 
	 * @return Business bean.
	 */
	public Business getBusiness() {
		return (Business) getSpringBean("business");
	}

	/**
	 * Getter for dao Spring bean.
	 * 
	 * @return Dao bean.
	 */
	public Dao getDao() {
		return (Dao) getSpringBean("dao");
	}
	
	public Object getSpringBean(final String name) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(
				getServletContext()).getBean(name);
	}

}
