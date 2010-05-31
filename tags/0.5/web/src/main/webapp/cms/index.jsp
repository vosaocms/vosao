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
<%@ page import="org.vosao.common.VosaoContext" %>
<%@ page import="org.vosao.entity.UserEntity" %>
<%@ page import="java.util.Enumeration" %>
<% 
    UserEntity user = VosaoContext.getInstance().getUser();
%>
<html>
<head>
    <title><fmt:message key="index.title"/></title>
</head>
<body>

<div id="main-panel">
    <div>
        <img src="/static/images/pages.png" />
        <a href="/cms/pages.jsp">
            <fmt:message key="index.content_pages"/>
        </a>
        <p><fmt:message key="index.content_pages.description"/></p>
    </div>
<% if (user.isAdmin()) { %>
    <div>
        <img src="/static/images/templates.png" />
        <a href="/cms/templates.jsp">
            <fmt:message key="index.design_templates"/>
        </a>
        <p><fmt:message key="index.design_templates.description"/></p>
    </div>
<% } %>    
    <div>
        <img src="/static/images/folders.png" />
        <a href="/cms/folders.jsp">
            <fmt:message key="index.file_resources_storage"/>
        </a>
        <p><fmt:message key="index.file_resources_storage.description"/></p>
    </div>
<% if (user.isAdmin()) { %>
    <div>
        <img src="/static/images/config.png" />
        <a href="/cms/config">
            <fmt:message key="index.site_configuration"/>
        </a>
        <p><fmt:message key="index.site_configuration.description"/></p>
    </div>
    <div>
        <img src="/static/images/plugins.png" />
        <a href="/cms/plugins"><fmt:message key="index.plugins"/></a>
        <p><fmt:message key="index.plugins.description"/></p>
    </div>
<% } %>    
</div>

</body>
</html>
