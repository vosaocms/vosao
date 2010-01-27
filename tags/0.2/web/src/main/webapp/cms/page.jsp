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
<%@ page import="org.vosao.servlet.FileUploadServlet" %>
<html>
<head>
  <title>Page</title>
  <link rel="stylesheet" href="/static/css/page.css" type="text/css" />
  <script type="text/javascript" src="/static/ckeditor/ckeditor.js"></script>
  
<%
    if (request.getParameter("id") != null) {    
        session.setAttribute(FileUploadServlet.IMAGE_UPLOAD_PAGE_ID, 
        		request.getParameter("id"));
    }
%>  
  
  <script type="text/javascript">
    var pageId = '<c:out value="${param.id}"/>';
    var pageParentUrl = decodeURIComponent('<c:out value="${param.parent}"/>');
  </script>

  <script type="text/javascript" src="/static/js/cms/page.js"></script>
    
</head>
<body>

<div id="versions" class="ui-tabs ui-widget ui-corner-all ui-widget-content">
    <div class="vertical-buttons-panel"> </div>      
    <a id="addVersionLink" class="button ui-state-default ui-corner-all" 
        href="#">
        <span class="ui-icon ui-icon-plus"></span> Add version
    </a>
    <div id="auditData">
        <div>Page state: <span id="pageState"> </span></div>
        <div>User created: <span id="pageCreateUser"> </span></div>
        <div>Creation date: <span id="pageCreateDate"> </span></div>
        <div>User modified: <span id="pageModUser"> </span></div>
        <div>Modify date: <span id="pageModDate"> </span></div>
    </div>
</div>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Page</a></li>
    <li class="contentTab"><a href="#tab-2">Content</a></li>
    <li class="childrenTab"><a href="#tab-3">Children pages</a></li>
    <li class="commentsTab"><a href="#tab-4">Comments</a></li>
    <li class="securityTab"><a href="#tab-5">Security</a></li>
</ul>

<div id="tab-1">

<div class="form-row">
    <label>Title</label>
    <input id="title" type="text" class="form-title" size="40" />
</div>
<div class="form-row">
    <label>Friendly URL </label>
    <span id="parentFriendlyUrl"></span>
    <input id="friendlyUrl" type="text" />
    <span id="friendlyUrlSpan"></span>
</div>
<div class="form-row">
    <label>Template</label>
    <select id="templates"></select>
</div>
<div class="form-row">
    <label>Publication date</label>
    <input id="publishDate" type="text" class="datepicker" size="8"/>
</div>
<div class="form-row">
    <label>Enable comments</label>
    <input id="commentsEnabled" type="checkbox" />
</div>

<div class="form-row">
    <label>Page type</label>
    <select id="pageType">
        <option value="SIMPLE">Simple</option>
        <option value="STRUCTURED">Structured</option>
    </select>
</div>
<div id="structuredControls">
<div class="form-row">
    <label>Structure</label>
    <select id="structure"></select>
</div>
<div class="form-row">
    <label>Structure template</label>
    <select id="structureTemplate"></select>
</div>
</div>

<div class="buttons">
    <div id="approveOnPageSaveDiv" class="checkboxes">
        <input id="approveOnPageSave" type="checkbox"> Approve on Save
    </div> 
    <input id="pageSaveButton" type="button" value="Save" />
    <input id="pagePreview" type="button" value="Preview" />
    <input id="pageCancelButton" type="button" value="Cancel" />
</div>    

</div>

<div id="tab-2" class="contentTab">

<div style="padding-right:10px">
    <div style="float:right">
        <input id="autosave" type="checkbox"> Autosave</input>
    </div>
    <div>
        Select content language: 
        <select id="language"></select>
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

<div id="tab-3" class="childrenTab">
    <div id="children"> </div>
    <div class="buttons">
        <input id="addChildButton" type="button" value="Add child page" />
        <input id="deleteChildButton" type="button" value="Delete pages" />
    </div>    
</div>

<div id="tab-4" class="commentsTab">
    <div id="comments"> </div>
    <div class="buttons">
        <input id="enableCommentsButton" type="button" value="Enable comments" />
        <input id="disableCommentsButton" type="button" value="Disable comments" />
        <input id="deleteCommentsButton" type="button" value="Delete comments" />
    </div>    
</div>

<div id="tab-5" class="securityTab">
    <div id="permissions"> </div>
    <div class="buttons">
        <input id="addPermissionButton" type="button" value="Add permission" />
        <input id="deletePermissionButton" type="button" value="Delete permission" />
    </div>    
</div>

</div>

<div id="version-dialog" style="display:none" title="Version title">
    <div class="form-row">
        <label>Version title</label>
        <input id="version-title" type="text" />        
    </div>
    <div class="buttons-dlg">
        <input id="versionSaveButton" type="button" value="Add" />
        <input id="versionCancelButton" type="button" value="Cancel" />
    </div>
</div>

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