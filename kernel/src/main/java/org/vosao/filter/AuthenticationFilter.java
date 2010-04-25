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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.vosao.common.VosaoContext;
import org.vosao.entity.UserEntity;

/**
 * Check authorised and redirect to login. Inject current user into Vosao 
 * context.
 * @author Aleksandr Oleynik
 */
public class AuthenticationFilter extends AbstractFilter implements Filter {

    public static final String USER_SESSION_ATTR = "userEmail";
	public static final String ORIGINAL_VIEW_KEY = "originalViewKey";
	public static final String LOGIN_VIEW = "/login.jsp";
	public static final String CMS = "/cms";

	public AuthenticationFilter() {
		super();
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
        String url = httpRequest.getServletPath();
        VosaoContext ctx = VosaoContext.getInstance();
        String userEmail = (String)session.getAttribute(USER_SESSION_ATTR);
        UserEntity user = getDao().getUserDao().getByEmail(userEmail);
		if (user == null) {
			ctx.setUser(null);
			if (url.startsWith(CMS)) {
				String originalUrl = httpRequest.getRequestURI() 
					+ (httpRequest.getQueryString() == null ? "" : 
						"?" + httpRequest.getQueryString());
				session.setAttribute(ORIGINAL_VIEW_KEY, originalUrl);
				httpResponse.sendRedirect(httpRequest.getContextPath()
						+ LOGIN_VIEW);
				return;
			}
		}
		else {
			ctx.setUser(user);
			if (url.startsWith(CMS) && ctx.getUser().isSiteUser()) {
				httpResponse.sendRedirect("/");
				return;
			}			
		}
		chain.doFilter(request, response);
	}

}
