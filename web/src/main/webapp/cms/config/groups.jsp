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
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />
    <script src="/static/js/cms/config/groups.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-6" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div id="groups"> </div>
    <div class="buttons">
        <input id="addGroupButton" type="button" value="<fmt:message key="add" />" />
        <input id="removeGroupButton" type="button" value="<fmt:message key="remove" />" />
    </div>
</div>

</div>

<div id="group-dialog" style="display:none" title="<fmt:message key="config.group_details" />">
  <form id="groupForm">
    <div class="messages"> </div>
    <div class="form-row">
        <label><fmt:message key="config.group_name" /></label>
        <input id="groupName" type="text"/>
    </div>
    <div class="buttons-dlg">
        <input id="groupSaveDlgButton" type="submit" value="<fmt:message key="save" />" />
        <input id="groupCancelDlgButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>  
</div>

<div id="user-group-dialog" style="display:none" title="<fmt:message key="config.group_users" />">
  <form id="userGroupForm">
    <div id="groupUsers"> </div>
    <div class="buttons-dlg">
        <input id="userGroupSaveDlgButton" type="submit" value="<fmt:message key="save" />" />
        <input id="userGroupCancelDlgButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>  
</div>

</body>
</html>