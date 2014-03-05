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

import org.vosao.business.impl.BusinessImpl;
import org.vosao.business.impl.mq.MessageQueueImpl;
import org.vosao.common.Session;
import org.vosao.common.VosaoContext;
import org.vosao.service.impl.BackServiceImpl;
import org.vosao.service.impl.FrontServiceImpl;

/**
 * Vosao context creation and request injection.
 * 
 * @author Alexander Oleynik
 *
 */
public class ContextFilter extends AbstractFilter implements Filter {
    
    public ContextFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	VosaoContext ctx = VosaoContext.getInstance();
    	ctx.setRequest((HttpServletRequest)request);
    	ctx.setResponse((HttpServletResponse)response);
    	ctx.setConfig(null);
    	if (ctx.getMessageQueue() == null) {
        	ctx.setMessageQueue(new MessageQueueImpl());
    	}
    	if (ctx.getBusiness() == null) {
        	ctx.setBusiness(new BusinessImpl());
    	}
    	if (ctx.getFrontService() == null) {
        	ctx.setFrontService(new FrontServiceImpl());
    	}
    	if (ctx.getBackService() == null) {
        	ctx.setBackService(new BackServiceImpl());
    	}
    	ctx.getPageRenderingContext().clear();
    	ctx.setSession(new Session((HttpServletRequest)request));
    	chain.doFilter(request, response);
    	ctx.getPageRenderingContext().clear();
    	ctx.getSession().save((HttpServletResponse)response);
    }
}
