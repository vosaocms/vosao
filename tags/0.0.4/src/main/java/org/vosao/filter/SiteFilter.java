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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
		"/plugin",
		"/static",
		"/login.jsp",
		"/file",
		"/setup",
		"/update",
		"/hotCron",
		"/JSON-RPC"};
    
    public SiteFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath();
        if (isSkipUrl(url)) {
            chain.doFilter(request, response);
            return;
        }
        SeoUrlEntity seoUrl = getDao().getSeoUrlDao().getByFrom(url);
        if (seoUrl != null) {
            httpResponse.sendRedirect(seoUrl.getToLink());
            return;
        }
    	PageEntity page = getDao().getPageDao().getByUrl(url);
        if (page != null) {
        	renderPage(httpRequest, httpResponse, page);
        	return;
        }
        if (url.equals("/")) {
            httpResponse.sendRedirect("/setup");
            return;
        }
        httpResponse.sendRedirect("/");
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
    
}
