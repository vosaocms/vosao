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
<html>
<head>
    <title><fmt:message key="pages" /></title>
    <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />
    <script src="/static/js/cms/pages.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/pages.css" type="text/css" />
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
    <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
        <li class="ui-corner-top ui-tabs-selected ui-state-active">
            <a href="#tab-1"><fmt:message key="pages" /></a>
        </li>
        <li id="structuresTab" class="ui-corner-top ui-state-default">
            <a href="structures.jsp"><fmt:message key="structures" /></a>
        </li>
    </ul>
    <div id="tab-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
        <!-- div class="outline-link">
            <a href="outline.jsp">Outline editor</a>
        </div-->
        <ul id="pages-tree"><img src="/static/images/ajax-loader.gif" /></ul>
    </div>
</div>

<div id="page-dialog" style="display:none" title="New page">
  <form id="pageForm">
    <div class="form-row">
        <label><fmt:message key="pages.page_title" /></label>
        <input id="title"/>
    </div>
    <div class="form-row">
        <label><fmt:message key="pages.page_url" /></label>
        <span id="parentURL"></span> <input id="url"/>
    </div>
    <div id="pageMessages"></div>
    <div class="buttons-dlg">
        <input id="saveDlgButton" type="submit" value="<fmt:message key="save" />" />
        <input id="cancelDlgButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>  
</div>


</body>
</html>