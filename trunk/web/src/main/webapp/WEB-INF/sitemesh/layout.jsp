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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ page import="org.vosao.business.CurrentUser" %>
<%@ page import="org.vosao.entity.UserEntity" %>
<% 
    UserEntity user = CurrentUser.getInstance();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="expires" content="0" />
    <title>Vosao CMS - <decorator:title default="default title" /></title>

    <link rel="stylesheet" href="/static/css/jquery-ui/redmond/jquery-ui.css" type="text/css" />

    <script src="/static/js/jquery.js" language="javascript"></script>
    <script src="/static/js/jquery.cookie.js" language="javascript"></script>
    <script src="/static/js/jquery-ui.js" language="javascript"></script>
    <script src="/static/js/jquery.xmldom.js" language="javascript"></script>
    <script src="/static/js/jsonrpc.js" language="javascript"></script>
    <script src="/static/js/vosao.js" language="javascript"></script>
    <script src="/static/js/cms.js" language="javascript"></script>
    <script src="/static/js/back-services.js" language="javascript"></script>

    <link rel="stylesheet" href="/static/css/style.css" type="text/css" />

    <script language="javascript">

    function onLogout() {
        Vosao.jsonrpc.loginFrontService.logout(function (r, e) {
            if (Vosao.serviceFailed(e)) return;
            if (r.result == 'success') {
                location.href = '/';
            }
            else {
                Vosao.showServiceMessages(r);
            }
        });
    }
    
    </script>

    <decorator:head />

    
</head>

<body>

<div id="topbar">

    <div id="leftmenu">
        <a href="/cms">Vosao</a> CMS
        <a href="/cms/pages.jsp">Content</a>
<% if (user.isAdmin()) { %>
        <a href="/cms/templates.jsp">Templates</a>
<% } %>        
        <a href="/cms/folders.jsp">Resources</a>
<% if (user.isAdmin()) { %>
        <a href="/cms/config">Configuration</a>
        <a href="/cms/plugins">Plugins</a>
<% } %>        
    </div>
    <div id="rightmenu">
        <%= user.getEmail() %> 
        | <a href="/cms/profile.jsp">Profile</a> 
        | <a href="#" onclick="onLogout()">Logout</a>
        | <a href="http://code.google.com/p/vosao/issues/list">Support</a>    
    </div>
    <span class="clear">&#160;</span>
</div>

<div id="wrapper">
    <decorator:body />
    <div class="messages"> </div>
</div>

</body>

</html>
