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
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="org.vosao.business.CurrentUser" %>
<%@ page import="org.vosao.entity.UserEntity" %>
<% 
    UserEntity user = CurrentUser.getInstance();
%>
<html>
<head>
    <title>VOSAO CMS</title>
</head>
<body>

<div id="main-panel">
    <div>
        <img src="/static/images/document_edit.png" />
        <a href="/cms/pages.jsp">Content pages</a>
        <p>Here you can edit site content. All content viewed as a tree of pages.
           You can change various page properties including design template 
           binding. 
        </p>
    </div>
<% if (user.isAdmin()) { %>
    <div>
        <img src="/static/images/file_edit.png" />
        <a href="/cms/templates.jsp">Design templates</a>
        <p>Here you can edit design templates. Site can have several design 
           templates. For every page you can select separate template.</p>
    </div>
<% } %>    
    <div>
        <img src="/static/images/diskette.png" />
        <a href="/cms/folders.jsp">File resources storage</a>
        <p>Here you can edit site resources. Resource could be any file
           including those used in design templates or referenced from pages</p>
    </div>
<% if (user.isAdmin()) { %>
    <div>
        <img src="/static/images/computer.png" />
        <a href="/cms/config.jsp">Site configuration</a>
        <p>Here you can change site configuration. Site domain, email,
           Google Analytics Id, comments template, comments email.</p>
    </div>
    <div>
        <img src="/static/images/shoppingcart.png" />
        <a href="/cms/plugins">Plugins</a>
        <p>Various plugins configuration. Forms. SEO Urls</p>
    </div>
<% } %>    
</div>

</body>
</html>
