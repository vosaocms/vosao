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

package org.vosao.business.impl.pagefilter.fragments;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.Business;
import org.vosao.business.impl.pagefilter.ContentFragment;
import org.vosao.entity.ConfigEntity;
import org.vosao.entity.PageEntity;

public class GoogleAnalyticsFragment implements ContentFragment {

	@Override
	public String get(Business business, PageEntity page) {
		ConfigEntity config = business.getConfigBusiness().getConfig();
		if (!StringUtils.isEmpty(config.getGoogleAnalyticsId())) {
			String id = config.getGoogleAnalyticsId();
			return getGoogleAnalyticsCode(id);
		}
		return "";
	}
	
	private static String getGoogleAnalyticsCode(final String id) { 
		return  
		"<!-- Google Analytics -->\n"
		+ "<script type=\"text/javascript\">\n"
	    + "var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\n"
        + "document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n"
	    + "</script>\n"
	    + "<script type=\"text/javascript\">\n"
	    + "var pageTracker = _gat._getTracker(\"" + id +"\");\n"
	    + "pageTracker._trackPageview();\n"
	    + "</script>\n";
	}

}
