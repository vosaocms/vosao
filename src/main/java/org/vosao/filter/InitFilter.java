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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vosao.business.Business;
import org.vosao.business.SetupBean;


public class InitFilter implements Filter {
    
    private static final Log log = LogFactory.getLog(SiteFilter.class);

    //private static final String SESSION_INITURL_PARAM = "initUrl";
    //private static final String INIT_URL = "/init";
    private static final String INIT_CRON_URL = "/initCron";
    
    private FilterConfig config = null;
    private ServletContext servletContext = null;
  
    private Business business;
    
    public InitFilter() {
    }
  
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        servletContext = config.getServletContext();
    }
    
    private void prepare() {
        business = (Business)WebApplicationContextUtils
    		.getRequiredWebApplicationContext(servletContext)
    		.getBean("business");
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, 
        FilterChain chain) throws IOException, ServletException {
        
    	prepare();
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
    	//HttpSession session = httpRequest.getSession(true); 
        String url = httpRequest.getServletPath();
        if (!business.isInitialized() && url.equals(INIT_CRON_URL)) {
            SetupBean setupBean = (SetupBean)WebApplicationContextUtils
            	.getRequiredWebApplicationContext(servletContext)
            	.getBean("setupBean");
        	setupBean.setup();
        	business.setInitialized(true);
        	writeOk(httpResponse);
        	return;
        }
        if (url.equals(INIT_CRON_URL)) {
        	writeOk(httpResponse);
        	return;
        }
        /*if (!business.isInitialized() && !url.equals(INIT_URL)) {
        	session.setAttribute(SESSION_INITURL_PARAM, url);
        	httpResponse.sendRedirect(INIT_URL);
        	return;
        }
        if (!business.isInitialized() && url.equals(INIT_URL)) {
        	String initUrl = "/";
        	if (session.getAttribute(SESSION_INITURL_PARAM) != null) {
            	initUrl = (String) session.getAttribute(SESSION_INITURL_PARAM);
        	}
            SetupBean setupBean = (SetupBean)WebApplicationContextUtils
            	.getRequiredWebApplicationContext(servletContext)
            	.getBean("setupBean");
        	setupBean.setup();
        	business.setInitialized(true);        	
        	httpResponse.sendRedirect(initUrl);
        	return;
        }*/
        chain.doFilter(request, response);
    }
    
    public void destroy() {
    }
    
    private void writeOk(HttpServletResponse response) throws IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().append("<html><body>Setup successfully completed.</body></html>");
    }
}
