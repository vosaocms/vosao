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
    <title>Templates</title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <script src="/static/js/cms/templates.js" language="javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Templates</a></li>
</ul>

<div id="tab-1">
    <div id="templates"><img src="/static/images/ajax-loader.gif" /></div>
    <div class="buttons">
        <input id="addButton" type="button" value="Add" />
        <input id="deleteButton" type="button" value="Delete" />
        <input id="exportButton" type="button" value="Export" />
        <input id="importButton" type="button" value="Import" />
    </div>
</div>

</div>

<div id="import-dialog" title="Import themes" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    File upload:
    <input type="hidden" name="fileType" value="import" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="Send" />
        <input id="importCancelButton" type="button" value="Cancel" />
    </div>    
</form>
</div>

<div id="afterUpload-dialog" style="display:none" title="Status window">
    <p class="message"></p>
    <div class="buttons-dlg">
        <input id="okButton" type="button" value="OK" />
    </div>
</div>

<div id="export-dialog" style="display:none" title="Export window">
  <form id="exportForm">
    <p>After export task finish. You will be redirected to created export file
    stored at "Resources" /tmp folder.</p>
    <div id="exportInfo" class="form-row"></div>    
    <div id="timer" class="form-row"></div>
    <div class="buttons-dlg">
        <input id="exportCancelButton" type="button" value="Cancel" />
    </div>
  </form>
</div>


</body>
</html>