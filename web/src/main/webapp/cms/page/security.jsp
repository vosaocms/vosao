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
  
  <%@ include file="head.jsp" %>

  <script type="text/javascript" src="/static/js/cms/page/security.js"></script>
    
</head>
<body>

<%@ include file="versionsBox.jsp" %>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-1" class=" securityTab ui-tabs-panel ui-widget-content ui-corner-bottom">
    <div id="permissions"> </div>
    <div class="buttons">
        <input id="addPermissionButton" type="button" value="Add permission" />
        <input id="deletePermissionButton" type="button" value="Delete permission" />
    </div>    
</div>

</div>

<%@ include file="versionDialog.jsp" %>

<div id="permission-dialog" style="display:none" title="Permission details">
    <div class="form-row">
        <label>Group</label>
        <select id="groupSelect"></select>
        <span id="groupName"></span>        
    </div>
    <div id="permissionList" class="form-row">
        <fieldset>
            <legend>Permission</legend>
            <input type="radio" name="permission" value="DENIED"/> Denied <br />
            <input type="radio" name="permission" value="READ" /> Read <br />
            <input type="radio" name="permission" value="WRITE" /> Read, Write <br />
            <input type="radio" name="permission" value="PUBLISH" /> Read, Write, Publish <br />
            <input type="radio" name="permission" value="ADMIN" /> Read, Write, Publish, Grant permissions <br />
        </fieldset>        
    </div>
    <div class="form-row">
        <label>All languages</label>
        <input id="allLanguages" type="checkbox" checked="checked" />
    </div>    
    <div id ="permLanguages" class="form-row" style="display:none"> </div>
    <div class="buttons-dlg">
        <input id="permissionSaveButton" type="button" value="Save" />
        <input id="permissionCancelButton" type="button" value="Cancel" />
    </div>
</div>

</body>
</html>