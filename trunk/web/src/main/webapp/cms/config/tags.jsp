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
    <title><fmt:message key="config.title" /></title>
    <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />
    <script src="/static/js/cms/config/tags.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-6" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <ul id="tags"></ul>
    <div class="buttons">
        <input id="addButton" type="button" value="<fmt:message key="add" />" />
    </div>
</div>

</div>

<div id="tag-dialog" style="display:none" title="<fmt:message key="config.tag_details" />">
  <form id="tagForm">
    <div class="messages"> </div>
    <div class="form-row">
        <label><fmt:message key="config.tag_name" /></label>
        <input id="tagName" type="text"/>
    </div>
    <div id="pages"></div>
    <div class="buttons-dlg">
        <input id="tagSaveDlgButton" type="submit" value="<fmt:message key="save" />" />
        <input id="tagDeleteDlgButton" type="button" value="<fmt:message key="delete" />" />
        <input id="tagCancelDlgButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>  
</div>

</body>
</html>