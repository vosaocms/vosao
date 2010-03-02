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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.business.SetupBean;
import org.vosao.business.impl.SetupBeanImpl;
import org.vosao.dao.cache.CacheStat;
import org.vosao.dao.cache.EntityCache;
import org.vosao.dao.cache.QueryCache;
import org.vosao.entity.ConfigEntity;
import org.vosao.servlet.SessionCleanTaskServlet;

import com.google.appengine.api.labs.taskqueue.Queue;


public class InitFilter extends AbstractFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(SiteFilter.class);

    private static final String SETUP_URL = "/setup";
    private static final String HOT_CRON_URL = "/cron/hot";
    private static final String SESSION_CLEAN_CRON_URL = "/cron/session_clean";
    
    private int localHits;
    private int cacheHits;
    private CacheStat entityStat;
    private CacheStat queryStat;    
    
    public InitFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        String url = httpRequest.getServletPath();
        if (url.equals(SETUP_URL)) {
            ConfigEntity config = getDao().getConfigDao().getConfig();
            if (config == null || !SetupBeanImpl.VERSION.equals(
            		config.getVersion())) {
            	SetupBean setupBean = (SetupBean)getSpringBean("setupBean");
            	setupBean.clear();
            	setupBean.setup();
            	logger.info("Setup was successfully completed.");
            }
        	httpResponse.sendRedirect("/");
        	return;
        }
        if (url.equals(HOT_CRON_URL)) {
        	logger.info(logCacheStat());
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
        //startProfile();
        chain.doFilter(request, response);
        //endProfile(httpRequest.getRequestURL().toString());
        getBusiness().getSystemService().getCache().resetLocalCache();
    }
    
    private void startProfile() {
        localHits = getBusiness().getSystemService().getCache().getLocalHits();
        cacheHits = getBusiness().getSystemService().getCache().getCacheHits();
        entityStat = getDao().getEntityCache().getStat();
        queryStat = getDao().getQueryCache().getStat();
    }
    
    private void endProfile(String url) {
        logger.info(url);
        logger.info("local cache hits: " + (
                getBusiness().getSystemService().getCache().getLocalHits() - localHits));
        CacheStat entity = getDao().getEntityCache().getStat();
        CacheStat query = getDao().getQueryCache().getStat();
        logger.info("memcache hits: " + (
        		getBusiness().getSystemService().getCache().getCacheHits() - cacheHits));
        logger.info("dao calls: " + (entity.getCalls() - entityStat.getCalls() + 
        		query.getCalls() - queryStat.getCalls()));
    }
    
    private void writeContent(HttpServletResponse response, String content)
    		throws IOException {
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
    	response.getWriter().append("<html><body>" + content + "</body></html>");
    }
    
    private String logCacheStat() {
        return "<p>Entity cache: " + getEntityCache().getStat() +
                " Query cache: " + getQueryCache().getStat() + "</p>";
    }
    
    private EntityCache getEntityCache() {
    	return (EntityCache) getSpringBean("entityCache");
    }

    private QueryCache getQueryCache() {
    	return (QueryCache) getSpringBean("queryCache");
    }
    
}
