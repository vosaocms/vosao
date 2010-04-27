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

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.servlet.SessionCleanTaskServlet;

import com.google.appengine.api.labs.taskqueue.Queue;

/**
 * Execute plugin cron tasks.
 * @author Alexander Oleynik
 *
 */
public class PluginCronFilter extends AbstractFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(PluginCronFilter.class);

    private static final String PLUGIN_CRON_URL = "/_ah/cron/plugin";
    private static final String SESSION_CLEAN_CRON_URL = "/_ah/cron/session_clean";
    
    public PluginCronFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	Date now = new Date();
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath();
        if (url.equals(PLUGIN_CRON_URL)) {
        	getBusiness().getPluginBusiness().cronSchedule(now);
        	writeContent(httpResponse, "<h4>OK</h4>");
        	return;
        }
        if (url.equals(SESSION_CLEAN_CRON_URL)) {
    		Queue queue = getBusiness().getSystemService().getQueue(
    				"session-clean");
    		queue.add(url(SessionCleanTaskServlet.SESSION_CLEAN_TASK_URL));
    		logger.info("Added new session clean task");
        	writeContent(httpResponse, "<h4>OK</h4>");
        	return;
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
