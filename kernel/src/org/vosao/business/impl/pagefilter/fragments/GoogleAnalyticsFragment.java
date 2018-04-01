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
		"\n<!-- Google Analytics -->\n"
		+ "<script type=\"text/javascript\">\n"
		+ "var _gaq = _gaq || [];\n"
		+ "_gaq.push(['_setAccount', '" + id + "']);\n"
		+ "_gaq.push(['_trackPageview']);\n"
		+ "_gaq.push(['_trackPageLoadTime']);\n"
		+ "(function() {\n"
		+ "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n"
		+ "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n"
		+ "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n"
		+ "})();\n"
	    + "</script>\n";
	}

}
