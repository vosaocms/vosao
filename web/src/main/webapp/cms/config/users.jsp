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
    <title>Site configuration</title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <link rel="stylesheet" href="/static/css/config.css" type="text/css" />
    <script src="/static/js/cms/config/users.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-5" class="ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div id="users"> </div>
    <div class="buttons">
        <input id="addUserButton" type="button" value="Add" />
        <input id="removeUserButton" type="button" value="Remove" />
    </div>
</div>

</div>

<div id="user-dialog" style="display:none" title="User details">
  <form id="userForm">
    <div class="messages"> </div>
    <div class="form-row">
        <label>User name</label>
        <input id="userName" type="text"/>
    </div>
    <div class="form-row">
        <label>User email</label>
        <input id="userEmail" type="text"/>
    </div>
    <div>
        <label>User role</label>
        <select id="userRole">
            <option value="SITE_USER">Site user</option>
            <option value="USER">User</option>
            <option value="ADMIN">Administrator</option>
        </select>
    </div>
    <div class="form-row">
        <label>Password</label>
        <input id="userPassword1" type="password"/>
    </div>
    <div class="form-row">
        <label>Retype password</label>
        <input id="userPassword2" type="password"/>
    </div>
    <div class="buttons-dlg">
        <input id="userSaveDlgButton" type="submit" value="Save" />
        <input id="userCancelDlgButton" type="button" value="Cancel" />
    </div>
  </form>  
</div>

</body>
</html>