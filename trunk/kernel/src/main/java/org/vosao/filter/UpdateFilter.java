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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.SetupBean;
import org.vosao.update.UpdateException;
import org.vosao.update.UpdateManager;

/**
 * @author Alexander Oleynik
 */
public class UpdateFilter extends AbstractFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(UpdateFilter.class);

    private static final String UPDATE_URL = "/update";
    
    public UpdateFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath();
        if (url.equals(UPDATE_URL)) {
        	String msg = "<h2>Vosao CMS " + SetupBean.FULLVERSION + " Update</h2>";
        	try {
        		UpdateManager updateManager = new UpdateManager(getBusiness());
        		String updateMsg = updateManager.update();
        		if (StringUtils.isEmpty(updateMsg)) {
        			updateMsg = "Database is already updated.";
        		}
        		writeContent(httpResponse, msg + updateMsg);
            	return;
        	}
        	catch (UpdateException e) {
        		writeContent(httpResponse, msg 
        				+ "<p class=\"color:red;\">Errors during update! " 
        				+ e.getMessage() + "</p>");
        		return;
        	}
        }
        chain.doFilter(request, response);
    }
    
    private void writeContent(HttpServletResponse response, String content)
    		throws IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().append("<html><body>" + content + "</body></html>");
    }
    
}
