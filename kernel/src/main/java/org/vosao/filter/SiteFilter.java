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
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.CurrentUser;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.common.AccessDeniedException;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.SeoUrlEntity;

/**
 * @author Alexander Oleynik
 */
public class SiteFilter extends AbstractFilter implements Filter {
    
    private static final Log log = LogFactory.getLog(SiteFilter.class);

    public static final String[] SKIP_URLS = {
		"/_ah",
		"/cms",
		"/static",
		"/login.jsp",
		"/file",
		"/setup",
		"/update",
		"/JSON-RPC"};
    
    public SiteFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath();
        if (processPluginServlet(request, response)) {
        	return;
        }
        if (isSkipUrl(url)) {
            chain.doFilter(request, response);
            return;
        }
        SeoUrlEntity seoUrl = getDao().getSeoUrlDao().getByFrom(url);
        if (seoUrl != null) {
            httpResponse.sendRedirect(seoUrl.getToLink());
            return;
        }
    	try {
    		if (getDao().getGroupDao().getGuestsGroup() == null) {
    			httpResponse.sendRedirect("/setup");
    			return;
    		}
    		PageEntity page = getPage(url, httpRequest);
    		if (page != null) {
    			renderPage(httpRequest, httpResponse, page);
    			return;
    		}
    		if (url.equals("/")) {
    			showNoApprovedContent(httpResponse);
    			return;
    		}
    		httpResponse.sendRedirect("/");
    	}
    	catch (AccessDeniedException e) {
    		ConfigEntity config = getBusiness().getConfigBusiness().getConfig();
    		if (StringUtils.isEmpty(config.getSiteUserLoginUrl())) {
    			renderMessage(httpResponse, 
    				"<h1>Access denied</h1>" +
    				"<h3>You need to login before access this page</h3>" +
    				"<h4>Unfortunately webmaster forgot to configure Site user login page Url.</h4>");
    		}
    		else {
    			HttpSession session = httpRequest.getSession(true);
    			session.setAttribute(AuthenticationFilter.ORIGINAL_VIEW_KEY, 
    					httpRequest.getRequestURI());
    			httpResponse.sendRedirect(httpRequest.getContextPath()
    					+ config.getSiteUserLoginUrl());
    		}
    	}
    }

	private void showNoApprovedContent(HttpServletResponse httpResponse) 
    		throws IOException {
    	renderMessage(httpResponse, "<h3>Sorry, but there is no approved content" +
    			" available for this page.</h3><p>Vosao CMS "+ 
    			SetupBeanImpl.FULLVERSION +"</p>");
    }

	public static boolean isSkipUrl(final String url) {
    	for (String u : SKIP_URLS) {
    		if (url.startsWith(u)) {
    			return true;
    		}
    	}
    	return false;
    }

    private void renderPage(HttpServletRequest request, 
    		HttpServletResponse response, final PageEntity page) 
    		throws IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	Writer out = response.getWriter();
    	String language = getBusiness().getLanguage(request);
    	String content = getBusiness().getPageBusiness().render(page, language);
    	out.write(content);
    }
    
    private Integer getVersion(HttpServletRequest request) {
    	try {
    		return Integer.valueOf(request.getParameter("version"));
    	}
    	catch (NumberFormatException e) {
    		return null;
    	}
    }
    
    private PageEntity getPage(String url, HttpServletRequest request) 
    		throws AccessDeniedException {
    	Integer version = getVersion(request);
    	PageEntity page;
    	if (getBusiness().getContentPermissionBusiness().getPermission(url, 
    			CurrentUser.getInstance()).isDenied()) {
    		throw new AccessDeniedException();
    	}
    	if (version == null) {
            page = getDao().getPageDao().getByUrl(url);
    	}
    	else {
            page = getDao().getPageDao().getByUrlVersion(url, version);
    	}
    	return page;
    }
    
    private void renderMessage(HttpServletResponse response, 
    		final String msg) throws IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	Writer out = response.getWriter();
    	out.write(msg);
    }

    private boolean processPluginServlet(ServletRequest request,
			ServletResponse response) throws ServletException, IOException {
		HttpServlet servlet = getBusiness().getPluginBusiness().getPluginServlet(
				(HttpServletRequest)request);
		if (servlet != null) {
			servlet.service(request, response);
			return true;
		}
		return false;
	}
    
}
