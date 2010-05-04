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
    <title><fmt:message key="plugins" /></title>
    <script src="/static/js/jquery.form.js" language="javascript"></script>
    <script src="/static/js/cms/plugins/config.js" type="text/javascript"></script>    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1"><fmt:message key="plugins" /></a></li>
</ul>

<div id="tab-1">
    <div id="plugins"></div>
    <div class="buttons">
        <input id="installButton" type="button" value="<fmt:message key="install" />" />
    </div>
</div>

</div>

<div id="import-dialog" title="<fmt:message key="install" />" style="display:none">
<form id="upload" action="/cms/upload" method="post" enctype="multipart/form-data">
    <fmt:message key="file_upload" />:
    <input type="hidden" name="fileType" value="plugin" />
    <input type="file" name="uploadFile" />
    <div class="buttons-dlg">
        <input type="submit" value="<fmt:message key="send" />" />
        <input id="importCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>    
</form>
</div>

<div id="afterUpload-dialog" style="display:none" title="<fmt:message key="status_window" />">
    <p class="message"></p>
    <div class="buttons-dlg">
        <input id="okButton" type="button" value="OK" />
    </div>
</div>

</body>
</html>