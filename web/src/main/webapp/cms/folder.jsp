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
    <title><fmt:message key="folder" /></title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <script type="text/javascript">
        var folderId = '<c:out value="${param.id}"/>';
        var folderParentId = '<c:out value="${param.parent}"/>';
    </script>
    <script src="/static/js/cms/folder.js" language="javascript"></script>
</head>
<body>

<div id="breadcrumbs">
    <span class="button ui-state-default ui-corner-all" style="padding:4px 10px;">
        <a href="/cms/folders.jsp"><fmt:message key="resources" />:</a> /
        <span id="crumbs"></span>
    </span>
</div>

<div id="tabs" style="top:14px;">
<ul>
    <li><a href="#tab-1"><fmt:message key="folder" /></a></li>
    <li class="filesTab"><a href="#tab-2"><fmt:message key="files" /></a></li>
    <li class="childrenTab"><a href="#tab-3"><fmt:message key="subfolders" /></a></li>
    <li class="securityTab"><a href="#tab-4"><fmt:message key="security" /></a></li>
</ul>

<div id="tab-1">
<form id="folderForm">
<div class="form-row">
    <label><fmt:message key="title" /></label>
    <input id="title" type="text" class="form-title" />
</div>
<div class="form-row">
    <label><fmt:message key="folder.name_for_url" /></label>
    <input id="name" type="text" class="form-url"/>
</div>
<div class="buttons">
    <input id="saveButton" type="submit" value="<fmt:message key="save" />" />
    <input id="cancelButton" type="button" value="<fmt:message key="cancel" />" />
    <input id="exportButton" type="button" value="<fmt:message key="export" />" />
</div>    
</form>
</div>

<div id="tab-2" class="filesTab">
    <div id="filesTable"> </div>
    <div class="buttons">
        <input id="createFileButton" type="button" 
            value="<fmt:message key="create_file" />" />
        <input id="uploadButton" type="button" 
            value="<fmt:message key="upload_file" />" />
        <input id="deleteFilesButton" type="button" 
            value="<fmt:message key="delete_files" />" />
        <input id="fileCancelButton" type="button" 
            value="<fmt:message key="cancel" />" />
    </div>
</div>

<div id="tab-3" class="childrenTab">
    <div id="children"> </div>
    <div class="buttons">
        <input id="addChildButton" type="button" 
            value="<fmt:message key="folder.add_child_folder" />" />
        <input id="deleteFoldersButton" type="button" 
            value="<fmt:message key="folder.delete_folders" />" />
        <input id="folderCancelButton" type="button" 
            value="<fmt:message key="cancel" />" />
    </div>    
</div>

<div id="tab-4" class="securityTab">
    <div id="permissions"> </div>
    <div class="buttons">
        <input id="addPermissionButton" type="button" 
            value="<fmt:message key="add_permission" />" />
        <input id="deletePermissionButton" type="button" 
            value="<fmt:message key="delete_permission" />" />
    </div>    
</div>

</div>

<div id="file-upload" title="<fmt:message key="folder.upload_file" />" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    <fmt:message key="file_upload" />:
    <input type="hidden" name="fileType" value="resource" />
    <input type="hidden" name="folderId" value="<c:out value="${param.id}" />" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="<fmt:message key="send" />" />
        <input id="fileUploadCancelButton" type="button" 
            value="<fmt:message key="cancel" />" />
    </div>
</form>
</div>

<div id="permission-dialog" style="display:none" title="<fmt:message key="permission_details" />">
<form id="permissionForm">
    <div class="form-row">
        <label><fmt:message key="group" /></label>
        <select id="groupSelect"></select>
        <span id="groupName"></span>        
    </div>
    <div id="permissionList" class="form-row">
        <fieldset>
            <legend><fmt:message key="permission" /></legend>
            <input type="radio" name="permission" value="DENIED"/> <fmt:message key="denied" /> <br />
            <input type="radio" name="permission" value="READ" /> <fmt:message key="read" /> <br />
            <input type="radio" name="permission" value="WRITE" /> <fmt:message key="read_write" /> <br />
        </fieldset>        
    </div>
    <div class="buttons-dlg">
        <input id="permissionSaveButton" type="submit" value="<fmt:message key="save" />" />
        <input id="permissionCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
</form>
</div>

<div id="export-dialog" style="display:none" title="<fmt:message key="folder.export_window" />">
  <form id="exportForm">
    <p><fmt:message key="folder.export_window_info" /></p>
    <div id="exportInfo" class="form-row"></div>    
    <div id="timer" class="form-row"></div>
    <div class="buttons-dlg">
        <input id="exportCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>
</div>


</body>
</html>