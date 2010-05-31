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
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vosao.common.VosaoContext;
import org.vosao.i18n.Messages;

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
    	HttpSession session = httpRequest.getSession(true);
    	VosaoContext ctx = VosaoContext.getInstance();
    	if (session.getAttribute(Messages.JSTL_FMT_LOCALE_KEY) == null) {
    		ctx.setLocale(request.getLocale());
    		session.setAttribute(Messages.JSTL_FMT_LOCALE_KEY, 
    				request.getLocale());
    	}
    	else {
    		ctx.setLocale((Locale)session.getAttribute(
    				Messages.JSTL_FMT_LOCALE_KEY));
    	}
    	if (httpRequest.getParameter("language") != null) {
    		String languageCode = httpRequest.getParameter("language");
   			Locale locale = new Locale(languageCode);
   			ctx.setLocale(locale);
       		session.setAttribute(Messages.JSTL_FMT_LOCALE_KEY, locale);
    	}
        chain.doFilter(request, response);
    }
    
}
