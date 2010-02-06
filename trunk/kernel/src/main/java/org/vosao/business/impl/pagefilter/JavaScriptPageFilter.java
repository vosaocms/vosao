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

package org.vosao.business.impl.pagefilter;

import org.apache.commons.lang.StringUtils;
import org.vosao.business.Business;
import org.vosao.entity.PluginEntity;

public class JavaScriptPageFilter extends AbstractPageFilter 
		implements PageFilter {

	public JavaScriptPageFilter(Business business) {
		super(business);
	}
	
	@Override
	public String apply(String page) {
		String tag = "<head>";
		if (page.indexOf("<HEAD>") != -1) {
			tag = "<HEAD>";
		}
		return StringUtils.replace(page, tag, getJavaScriptCode());
	}
	
	private String getJavaScriptCode() { 
		StringBuffer code = new StringBuffer("<head>\n" 
		    +  "<script src=\"/static/js/jquery.js\" type=\"text/javascript\"></script>\n"
		    +  "<script src=\"/static/js/jquery.form.js\" type=\"text/javascript\"></script>\n"
            +  "<script src=\"/static/js/jsonrpc.js\" type=\"text/javascript\"></script>\n"
            +  "<script src=\"/static/js/vosao.js\" type=\"text/javascript\"></script>\n"
            +  "<script src=\"http://api.recaptcha.net/js/recaptcha_ajax.js\" type=\"text/javascript\" ></script>\n");
		for (PluginEntity plugin : getBusiness().getDao().getPluginDao().select()) {
			if (!StringUtils.isEmpty(plugin.getPageHeader())) {
				code.append(plugin.getPageHeader());
			}
		}
		return code.toString();
	}

}
