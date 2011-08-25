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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.common.AccessDeniedException;
import org.vosao.common.VosaoContext;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;
import org.vosao.entity.SeoUrlEntity;
import org.vosao.entity.UserEntity;
import org.vosao.global.PageCacheItem;
import org.vosao.i18n.Messages;

/**
 * @author Alexander Oleynik
 */
public class SiteFilter extends AbstractFilter implements Filter {
    
    public static final String[] SKIP_URLS = {
		"/_ah",
		"/cms",
		"/static",
		"/login.vm",
		"/file",
		"/setup",
		"/update",
		"/json-rpc",
		"/favicon.ico",
		"/i18n.js"};
    
    private List<String> skipURLs;
    
    public SiteFilter() {
    	super();
    }
  
    @Override
    public void init(FilterConfig config) {
    	skipURLs = new ArrayList<String>();
    	for (String url : SKIP_URLS) {
    		skipURLs.add(url);
    	}
    	String skipURLParam = config.getInitParameter("skipURL");
    	if (!StringUtils.isEmpty(skipURLParam)) {
    		String[] urls = skipURLParam.split(",");
    		for (String url : urls) {
    			skipURLs.add(url);
    		}
    	}
    }
    
    @Override
    public void destroy() {
    	
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	VosaoContext ctx = VosaoContext.getInstance();
    	ctx.setSkipURLs(skipURLs);
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath().toLowerCase();
        if (url.startsWith("/_ah/plugin")) {
        	if (processPluginServlet(request, response)) {
        		return;
        	}
        }
        if (ctx.isSkipUrl(url)) {
            chain.doFilter(request, response);
            return;
        }
        if (url.endsWith("/") && url.length() > 1) {
        	httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        	httpResponse.setHeader("Location", url.substring(0, url.length() - 1));
        	httpResponse.setHeader("Connection", "close");
        	return;
        }
        if (!isLoggedIn(httpRequest) && servedFromCache(url, httpResponse)) {
        	return;
        }
        SeoUrlEntity seoUrl = getDao().getSeoUrlDao().getByFrom(url);
        if (seoUrl != null) {
        	httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        	httpResponse.setHeader("Location", seoUrl.getToLink());
        	httpResponse.setHeader("Connection", "close");
            return;
        }
    	try {
    		if (getDao().getGroupDao().getGuestsGroup() == null) {
    			httpResponse.sendRedirect("/setup");
    			return;
    		}
    		PageEntity page = getPage(url, httpRequest);
    		if (page != null) {
    			renderPage(httpRequest, httpResponse, page, url);
    			return;
    		}
    		if (url.equals("/")) {
    			showNoApprovedContent(httpResponse);
    			return;
    		}
    		ConfigEntity config = ctx.getConfig();
    		if (!StringUtils.isEmpty(config.getSite404Url())) {
        		page = getPage(config.getSite404Url(), httpRequest);
        		if (page != null) {
        			renderPage(httpRequest, httpResponse, page, url);
        		}
    		}
    		httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
    	}
    	catch (AccessDeniedException e) {
			HttpSession session = httpRequest.getSession(true);
            String userEmail = (String)session.getAttribute(
            		AuthenticationFilter.USER_SESSION_ATTR);
            UserEntity user = getDao().getUserDao().getByEmail(userEmail);
    		ConfigEntity config = ctx.getConfig();
    		if (user != null) {
    			renderMessage(httpResponse, Messages.get("access_denied_page"));
    			return;
    		}
    		if (StringUtils.isEmpty(config.getSiteUserLoginUrl())) {
    			renderMessage(httpResponse, Messages.get("login_not_configured"));
    			return;
    		}
   			session.setAttribute(AuthenticationFilter.ORIGINAL_VIEW_KEY, 
   					httpRequest.getRequestURI());
   			httpResponse.sendRedirect(httpRequest.getContextPath()
   					+ config.getSiteUserLoginUrl());
    	}
    }

	private boolean servedFromCache(String url,	HttpServletResponse response) 
			throws IOException {
		PageCacheItem page = getSystemService().getPageCache().get(url,
				getBusiness().getLanguage());
		if (page != null) {
	    	response.setContentType(page.getContentType());
	    	response.setCharacterEncoding("UTF-8");
	    	Writer out = response.getWriter();
	    	out.write(page.getContent());
			return true;
		}
		return false;
	}

	private void showNoApprovedContent(HttpServletResponse httpResponse) 
    		throws IOException {
    	renderMessage(httpResponse, Messages.get("not_approved_page", 
    			SetupBeanImpl.FULLVERSION));
    }

    private void renderPage(HttpServletRequest request, 
    		HttpServletResponse response, final PageEntity page, String url) 
    		throws IOException {
    	String contentType = StringUtils.isEmpty(page.getContentType()) ?
    			"text/html" : page.getContentType();
    	response.setContentType(contentType);
    	response.setCharacterEncoding("UTF-8");
    	Writer out = response.getWriter();
    	String language = getBusiness().getLanguage();
    	String content = getBusiness().getPageBusiness().render(page, language);
    	out.write(content);
    	if (!isLoggedIn(request) && page.isCached()) {
    		getSystemService().getPageCache().put(url, language, content, 
    				contentType);
    	}
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
    			VosaoContext.getInstance().getUser()).isDenied()) {
    		throw new AccessDeniedException();
    	}
    	if (version == null) {
            page = getDao().getPageDao().getByUrl(url);
            if (page == null) {
            	page = getBusiness().getPageBusiness().getRestPage(url);
            }
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
