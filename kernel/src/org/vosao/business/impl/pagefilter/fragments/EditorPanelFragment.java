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

import org.vosao.business.Business;
import org.vosao.business.impl.pagefilter.ContentFragment;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;

public class EditorPanelFragment implements ContentFragment {

	@Override
	public String get(Business business, PageEntity page) {
		if (VosaoContext.getInstance().getUser() != null 
			&& VosaoContext.getInstance().getUser().isEditor()) {
			StringBuffer code = new StringBuffer( 
				"<div id=\"editor-panel\"" 
				+	"style=\"border-bottom: 1px solid #a5c4d5;"
				+ "padding: 4px; position: fixed; left: 0; top: 0; width: 100%;"
				+ "background-color: white; opacity:0.2; z-index: 1;\">"
				+ "<div style=\"float:left\">"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms\">Vosao</a> CMS"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms#page/content/");
			code.append(page.getId()).append("\">Edit page</a>"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms#pages\">Content</a>"
				+ "<a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms#folders\">Resources</a>"
				+ "</div>"
				+ "<div style=\"float:right;margin-right:10px;\">"
				+ VosaoContext.getInstance().getUser().getEmail()
				+ " | <a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/cms#profile\">Profile</a>" 
				+ " | <a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"http://code.google.com/p/vosao/issues/list\">Support</a>"
				+ " | <a style=\"padding: 2px 4px; margin-left: 2px;\" href=\"/_ah/logout\">Logout</a>"
				+ " | <a href=\"#\" onclick=\"hidePanel()\">Hide</a> "
				+ "</div>"
				+ "<span style=\"clear:both\">&#160;</span>"
				+ "</div>"
				+ "<div id=\"editor-pin\""
				+ "    style=\"border: 1px solid silver;"
				+ "    padding: 4px; position: fixed; left: 0; bottom: 0; width: 76px;"
				+ "    background-color: white; z-index: 1;\">"
				+ "  <a href=\"#\" onclick=\"showPanel()\">Show panel</a>"
				+ "</div>"
				+ "<script type=\"text/javascript\">"
				+ " $(function() {"
				+ "   if ($.cookie('hideEditorPanel')) hidePanel();"
				+ "   $('#editor-panel').hover(function(){ $('#editor-panel').css('opacity','1.0'); }, "
				+ "      function(){ $('#editor-panel').css('opacity','0.1'); });"
				+ " });"
				+ " function hidePanel() {"
				+ "   $('#editor-panel').hide();"
				+ "   $('#editor-pin').show();"
				+ "   $.cookie('hideEditorPanel', 'yes');"
				+ " }"
				+ " function showPanel() {"
				+ "   $('#editor-panel').show();"
				+ "   $('#editor-pin').hide();"
				+ "   $.cookie('hideEditorPanel', null);"
				+ " }"
				+ "</script>");
			return code.toString();
		}
		return "";
	}

}
