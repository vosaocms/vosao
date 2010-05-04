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

<form id="pageForm">

<div style="padding-right:10px">
    <div style="float:right">
        <input id="autosave" type="checkbox"> <fmt:message key="autosave" /></input>
    </div>
    <div>
        <fmt:message key="page.select_language" />: 
        <select id="language"></select>
    </div>
    <div class="form-row" style="margin-top:10px;">
        <label><fmt:message key="title" /></label>
        <input id="titleLocal" type="text" class="form-title" size="40" />
    </div>
    <div class="form-row" id="editorButtons"></div>
    <div id="page-content"></div>
</div>
<div class="buttons">
    <div id="approveOnContentSaveDiv" class="checkboxes">
        <input id="approveOnContentSave" type="checkbox"> <fmt:message key="page.approve_save" />
    </div> 
    <input id="saveContinueContentButton" type="button" 
        value="<fmt:message key="save_continue" />" />
    <input id="saveContentButton" type="submit" 
        value="<fmt:message key="save" />" />
    <input id="contentPreviewButton" type="button" 
        value="<fmt:message key="preview" />" />
    <input id="approveButton" type="button" 
        value="<fmt:message key="approve" />" />
    <input id="restoreButton" type="button" 
        value="<fmt:message key="restore" />" />
    <input id="contentCancelButton" type="button" 
        value="<fmt:message key="cancel" />" />
</div>    

</form>

</div>

</div>

<%@ include file="versionDialog.jsp" %>

<div id="restore-dialog" style="display:none" title="<fmt:message key="page.restore_content" />">
  <form id="restoreForm">
    <div class="form-row">
        <input name="page" type="radio" value="home" checked="checked"/> 
            <fmt:message key="home_page" /> <br/>        
        <input name="page" type="radio" value="login" /> 
            <fmt:message key="login_page" /> <br/>        
        <input name="page" type="radio" value="search" /> 
            <fmt:message key="search_page" />         
    </div>
    <div class="buttons-dlg">
        <input id="restoreSaveButton" type="submit" value="OK" />
        <input id="restoreCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>
</div>

</body>
</html>