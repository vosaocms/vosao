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

  <script type="text/javascript" src="/static/js/cms/page/content.js"></script>
    
</head>
<body>

<%@ include file="versionsBox.jsp" %>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">

<%@ include file="tab.jsp" %>

<div id="tab-1" class="contentTab ui-tabs-panel ui-widget-content ui-corner-bottom">

<div style="padding-right:10px">
    <div style="float:right">
        <input id="autosave" type="checkbox"> Autosave</input>
    </div>
    <div>
        Select content language: 
        <select id="language"></select>
    </div>
    <div class="form-row" style="margin-top:10px;">
        <label>Title</label>
        <input id="titleLocal" type="text" class="form-title" size="40" />
    </div>
    <div id="page-content"></div>
</div>
<div class="buttons">
    <div id="approveOnContentSaveDiv" class="checkboxes">
        <input id="approveOnContentSave" type="checkbox"> Approve on Save
    </div> 
    <input id="saveContinueContentButton" type="button" 
        value="Save and continue" />
    <input id="saveContentButton" type="button" value="Save" />
    <input id="contentPreviewButton" type="button" value="Preview" />
    <input id="approveButton" type="button" value="Approve" />
    <input id="contentCancelButton" type="button" value="Cancel" />
</div>    

</div>

</div>

<%@ include file="versionDialog.jsp" %>

</body>
</html>