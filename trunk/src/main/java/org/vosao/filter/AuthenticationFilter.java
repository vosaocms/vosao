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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;


public class AuthenticationFilter implements Filter {
    
    public static final String ORIGINAL_VIEW_KEY = "originalViewKey";
    public static final String LOGIN_VIEW = "/login";
  
    FilterConfig config = null;
    ServletContext servletContext = null;
  
    public AuthenticationFilter() {
    }
  
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        servletContext = config.getServletContext();
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, 
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpSession session = httpRequest.getSession();
        if (!isLoggedIn(httpRequest)) {
          session.setAttribute(ORIGINAL_VIEW_KEY, httpRequest.getRequestURI());
          httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_VIEW);
        }
        else {
          chain.doFilter(request, response);
        }
    }
    
    private boolean isLoggedIn(final HttpServletRequest request) {
        Business business = (Business)WebApplicationContextUtils
        	.getRequiredWebApplicationContext(servletContext)
        	.getBean("business");
    	return business.getUserPreferences(request).isLoggedIn();
    }
  
    public void destroy() {
    }

}
