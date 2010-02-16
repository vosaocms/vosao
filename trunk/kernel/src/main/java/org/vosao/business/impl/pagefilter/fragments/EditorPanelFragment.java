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

import org.vosao.business.Business;
import org.vosao.business.CurrentUser;
import org.vosao.business.impl.pagefilter.ContentFragment;
import org.vosao.entity.PageEntity;

public class EditorPanelFragment implements ContentFragment {

	@Override
	public String get(Business business, PageEntity page) {
		if (CurrentUser.getInstance() != null 
			&& CurrentUser.getInstance().isEditor()) {
			StringBuffer code = new StringBuffer( 
				"<div id=\"editor-panel\"" 
				+	"style=\"border-bottom: 1px solid #a5c4d5;"
				+ "padding: 4px; position: fixed; left: 0; top: 0; width: 100%;"
				+ "background-color: white; z-index: 1;\">"
				+ "<div style=\"float:left\">"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms\">Vosao</a> CMS"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms/page/content.jsp?id=");
			code.append(page.getId()).append(
				"&tab=1\">Edit page</a>"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms/pages.jsp\">Content</a>"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms/folders.jsp\">Resources</a>"
				+ "</div>"
				+ "<div style=\"float:right;margin-right:10px;\">"
				+ CurrentUser.getInstance().getEmail()
				+ " | <a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms/profile.jsp\">Profile</a>" 
				+ " | <a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"http://code.google.com/p/vosao/issues/list\">Support</a>"
				+ " | <a href=\"#\" onclick=\"$('#editor-panel').hide()\">Hide</a> "
				+ "</div>"
				+ "<span style=\"clear:both\">&#160;</span>"
				+ "</div>");
			return code.toString();
		}
		return "";
	}

}
