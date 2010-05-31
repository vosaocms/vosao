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
    <title>File view</title>
    <script type="text/javascript">
        var fileId = '<c:out value="${param.id}" />';
        var folderId = '<c:out value="${param.folderId}" />';
    </script>
    <script src="/static/js/cms/file.js" type="text/javascript"></script>

</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1"><fmt:message key="file" /></a></li>
    <li class="contentTab"><a href="#tab-2"><fmt:message key="content" /></a></li>
</ul>

<div id="tab-1">
<form id="fileForm">

<div style="float:left">
 <div class="form-row">
    <label><fmt:message key="title" /></label>
    <input id="title" type="text" />
 </div>
 <div class="form-row">
    <label><fmt:message key="name" /></label>
    <input id="name" type="text" />
 </div>
 
<div id="fileEditDiv">
 <div class="form-row">
    <label><fmt:message key="content_type" /></label>
    <span id="mimeType"> </span>
 </div>
 <div class="form-row">
    <label><fmt:message key="size" /></label>
    <span id="size"> </span>
 </div>
 <div class="form-row">
    <label><fmt:message key="external_link" /></label>
    <span id="fileLink"> </span>
 </div>
 <div class="form-row">
    <label> </label>
    <span id="download"> </span>
 </div>
 </div>
  
 <div class="buttons">
    <input id="saveButton" type="submit" value="<fmt:message key="save" />" />
    <input id="cancelButton" type="button" value="<fmt:message key="cancel" />" />
 </div>
     
</div>

<div id="imageContent" style="float:left;margin-left: 20px;"> </div>
<div style="clear:both"> </div>

</form>
</div>

<div id="tab-2" class="contentTab">
  <form id="contentForm">
    <div>
        <input id="autosave" type="checkbox" checked="checked"> <fmt:message key="autosave" /></input>
    </div>
    <div class="form-row">
        <textarea id="content" rows="20" cols="80"></textarea>
    </div>
    <div class="buttons">
        <input id="saveContentButton" type="submit" value="<fmt:message key="save_continue" />" />
        <input id="contentCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>    
  </form>
</div>

</div>

</body>
</html>