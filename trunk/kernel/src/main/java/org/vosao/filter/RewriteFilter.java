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
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.common.VosaoContext;

/**
 * @author Alexander Oleynik
 */
public class RewriteFilter extends AbstractFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(SiteFilter.class);

    public static class RewrittenRequestWrapper extends HttpServletRequestWrapper {

    	private String newURI;
    	private String newServletPath;
    	
        public RewrittenRequestWrapper(HttpServletRequest request) {
            super(request);
            newURI = VosaoContext.getInstance().getBusiness()
            		.getRewriteUrlBusiness().rewrite(request.getRequestURI());
            newServletPath = VosaoContext.getInstance().getBusiness()
    				.getRewriteUrlBusiness().rewrite(request.getServletPath());
        }
        
        @Override
        public String getRequestURI() {
            return newURI;
        }

        @Override
        public String getServletPath() {
            return newServletPath;
        }
        
    }
    
    public RewriteFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	if (isSkipUrl(httpRequest.getServletPath())) {
            chain.doFilter(request, response);
    	}
    	else {
    		HttpServletRequest newRequest = new RewrittenRequestWrapper(httpRequest);
    	    chain.doFilter(newRequest, response);
    	}
    }

    private boolean isSkipUrl(String url) {
    	for (String u : SiteFilter.SKIP_URLS) if (url.startsWith(u)) return true;
    	return false;
    }
    
}
