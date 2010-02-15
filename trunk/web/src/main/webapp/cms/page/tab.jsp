<%
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
%>

<%
    String parent = request.getParameter("parent");
    String query = "?id=" + request.getParameter("id") 
    		+ (parent == null ? "" : "&parent=" + parent);
%>
<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
    <li class="pageTab ui-corner-top ui-state-default">
        <a href="index.jsp<%= query %>">Page</a>
    </li>
    <li class="contentTab ui-corner-top ui-state-default">
        <a href="content.jsp<%= query %>">Content</a>
    </li>
    <li class="childrenTab ui-corner-top ui-state-default">
        <a href="children.jsp<%= query %>">Children pages</a>
    </li>
    <li class="commentsTab ui-corner-top ui-state-default">
        <a href="comments.jsp<%= query %>">Comments</a>
    </li>
    <li class="securityTab ui-corner-top ui-state-default">
        <a href="security.jsp<%= query %>">Security</a>
    </li>
</ul>
