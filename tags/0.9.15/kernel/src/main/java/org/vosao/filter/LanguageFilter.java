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
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.vosao.common.VosaoContext;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class LanguageFilter extends AbstractFilter implements Filter {
    
    public LanguageFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	VosaoContext ctx = VosaoContext.getInstance();
    	if (ctx.getSession().getLocale() == null) {
    		ctx.setLocale(request.getLocale());
    		ctx.getSession().setLocale(request.getLocale());
    	}
    	else {
    		ctx.setLocale(ctx.getSession().getLocale());
    	}
    	if (httpRequest.getParameter("language") != null) {
    		String languageCode = httpRequest.getParameter("language");
   			Locale locale = getLocale(languageCode);
   			logger.info("Locale " + locale.getDisplayName());
   			ctx.setLocale(locale);
       		ctx.getSession().setLocale(locale);
    	}
        chain.doFilter(request, response);
    }
    
    public static Locale getLocale(String language) {
    	int undescore = language.indexOf("_");
    	if (undescore != -1) {
    		String[] codes = language.split("_");
    		if (codes.length > 1) {
        		return new Locale(codes[0], codes[1]);
    		}
    	}
    	return new Locale(language);
    }
    
}
