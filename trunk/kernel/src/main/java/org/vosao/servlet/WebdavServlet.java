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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bradmcevoy.http.HttpManager;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Response;
import com.bradmcevoy.http.ServletResponse;

public class WebdavServlet extends BaseSpringServlet {
	    
	    ServletConfig config;
	    HttpManager httpManager;
	    
	    private static final ThreadLocal<HttpServletRequest> originalRequest = 
	    		new ThreadLocal<HttpServletRequest>();
	    private static final ThreadLocal<HttpServletResponse> originalResponse = 
	    		new ThreadLocal<HttpServletResponse>();

	    public static HttpServletRequest request() {
	        return originalRequest.get();
	    }
	    
	    public static HttpServletResponse response() {
	        return originalResponse.get();
	    }
	    
	    public static void forward(String url) {
	        try {
	            request().getRequestDispatcher(url).forward(originalRequest.get(),
	            		originalResponse.get());
	        } catch (IOException ex) {
	            throw new RuntimeException(ex);
	        } catch (ServletException ex) {
	            throw new RuntimeException(ex);
	        }
	    }
	    
	    public void init(ServletConfig config) throws ServletException {
	        try {
	            this.config = config;
	            httpManager = (HttpManager) getSpringBean("milton.http.manager");
	        } catch (Throwable ex) {
	            logger.error("Exception starting milton servlet " + ex.getMessage());
	            throw new RuntimeException(ex);
	        }        
	    }
	    
	    public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
	        HttpServletRequest req = (HttpServletRequest) servletRequest;
	        HttpServletResponse resp = (HttpServletResponse) servletResponse;
	        try {
	            originalRequest.set(req);
	            originalResponse.set(resp);
	            Request request = new WebdavServletRequest(req);
	            Response response = new ServletResponse(resp);
	            httpManager.process(request, response);
	        } finally {
	            originalRequest.remove();
	            originalResponse.remove();
	            servletResponse.getOutputStream().flush();            
	            servletResponse.flushBuffer();
	        }
	    }

	    public String getServletInfo() {
	        return "WebdavServlet";
	    }

	    public ServletConfig getServletConfig() {
	        return config;
	    }

	    public void destroy() {
	        
	    }
	}


