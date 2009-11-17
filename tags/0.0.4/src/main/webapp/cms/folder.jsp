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
    <title>Folder</title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <script type="text/javascript">
        var folderId = '<c:out value="${param.id}"/>';
        var parentFolderId = '<c:out value="${param.parent}"/>';
    </script>
    <script src="/static/js/cms/folder.js" language="javascript"></script>
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Folder</a></li>
    <li class="filesTab"><a href="#tab-2">Files</a></li>
    <li class="childrenTab"><a href="#tab-3">Subfolders</a></li>
</ul>

<div id="tab-1">

<div class="form-row">
    <label>Title</label>
    <input id="title" type="text" class="form-title" />
</div>
<div class="form-row">
    <label>Name for URL</label>
    <input id="name" type="text" class="form-url"/>
</div>
<div class="buttons">
    <input id="saveButton" type="button" value="Save" />
    <input id="cancelButton" type="button" value="Cancel" />
    <input id="exportButton" type="button" value="Export" />
</div>    

</div>

<div id="tab-2" class="filesTab">
    <div id="filesTable"> </div>
    <div class="buttons">
        <input id="createFileButton" type="button" value="Create file" />
        <input id="uploadButton" type="button" value="Upload file" />
        <input id="deleteFilesButton" type="button" value="Delete files" />
        <input id="fileCancelButton" type="button" value="Cancel" />
    </div>
</div>

<div id="tab-3" class="childrenTab">
    <div id="children"> </div>
    <div class="buttons">
        <input id="addChildButton" type="button" value="Add child folder" />
        <input id="deleteFoldersButton" type="button" value="Delete folders" />
        <input id="folderCancelButton" type="button" value="Cancel" />
    </div>    
</div>

</div>

<div id="file-upload" title="Upload file to folder" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    File upload:
    <input type="hidden" name="fileType" value="resource" />
    <input type="hidden" name="folderId" value="<c:out value="${param.id}" />" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="Send" />
        <input id="fileUploadCancelButton" type="button" value="Cancel" />
    </div>
</form>
</div>

</body>
</html>