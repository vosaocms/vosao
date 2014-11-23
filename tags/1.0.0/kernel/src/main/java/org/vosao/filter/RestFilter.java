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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.rest.DummyRest;
import org.vosao.rest.RestManager;

/**
 * @author Alexander Oleynik
 */
public class RestFilter extends AbstractFilter implements Filter {

	private static final Log logger = LogFactory.getLog(RestFilter.class);
	
	private RestManager manager;
	
	public RestFilter() {
		super();
		manager = new RestManager();
		manager.addService("dummy", new DummyRest());
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		String result;
		try {
			result = manager.execute(httpRequest);
			logger.info("REST result: " + result);
			if (result != null) {
				httpResponse.setContentType("application/json");
				httpResponse.setStatus(200);
				httpResponse.getWriter().write(result);
			}
			else {
				httpResponse.setStatus(501);
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
			httpResponse.setContentType("text/plain");
			httpResponse.setStatus(500);
			httpResponse.getWriter().write(ExceptionUtils.getStackTrace(e));
		}
	}		
	
}
