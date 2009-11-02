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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jabsorb.JSONRPCBridge;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;
import org.vosao.service.BackService;
import org.vosao.service.FrontService;

public class ServiceFilter implements Filter {

	FilterConfig config = null;
	ServletContext servletContext = null;

	public ServiceFilter() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		config = filterConfig;
		servletContext = config.getServletContext();
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
        boolean backService = isLoggedIn(httpRequest);
		enableFrontService(session);
        if (backService) {
        	enableBackService(session);
        }
        chain.doFilter(request, response);
		disableFrontService(session);
        if (backService) {
			disableBackService(session);
		}
	}

	private void enableBackService(HttpSession session) {
		JSONRPCBridge bridge = getJSONRPCBridge(session);
		getBackService().register(bridge);
	}

	private void enableFrontService(HttpSession session) {
		JSONRPCBridge bridge = getJSONRPCBridge(session);
		getFrontService().register(bridge);
	}
	
	private void disableBackService(HttpSession session) {
		JSONRPCBridge bridge = getJSONRPCBridge(session);
		getBackService().unregister(bridge);
	}

	private void disableFrontService(HttpSession session) {
		JSONRPCBridge bridge = getJSONRPCBridge(session);
		getFrontService().unregister(bridge);
	}

	private JSONRPCBridge getJSONRPCBridge(HttpSession session) {
		if (session.getAttribute("JSONRPCBridge") == null) {
			session.setAttribute("JSONRPCBridge", new JSONRPCBridge());
		}
		return (JSONRPCBridge) session.getAttribute("JSONRPCBridge");
	}

	private boolean isLoggedIn(final HttpServletRequest request) {
		Business business = (Business) WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext).getBean(
						"business");
		return business.getUserPreferences(request).isLoggedIn();
	}

	private FrontService getFrontService() {
		return (FrontService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext).getBean(
						"frontService");
	}

	private BackService getBackService() {
		return (BackService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext).getBean(
						"backService");
	}
	
}
