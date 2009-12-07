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
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vosao.entity.LanguageEntity;


public class LanguageFilter extends AbstractFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(LanguageFilter.class);

    public LanguageFilter() {
    	super();
    }
  
    public void doFilter(ServletRequest request, ServletResponse response, 
    		FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	if (getBusiness().getLanguage(httpRequest) == null) {
    		String language = request.getLocale().getLanguage();
    		logger.info("Initial language set " + language);
    		getBusiness().setLanguage(language, httpRequest);
    	}
    	if (httpRequest.getParameter("language") != null) {
    		String languageCode = httpRequest.getParameter("language");
    		LanguageEntity language = getDao().getLanguageDao().getByCode(
    				languageCode);
    		if (language != null) {
    			logger.info("Set language " + language + " by parameter");
    			getBusiness().setLanguage(languageCode, httpRequest);
    		}
    	}
        chain.doFilter(request, response);
    }
    
}
