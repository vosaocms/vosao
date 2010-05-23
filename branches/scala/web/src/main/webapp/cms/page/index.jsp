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

  <script src="/static/js/jquery.treeview.pack.js" type="text/javascript"></script>
  <link rel="stylesheet" href="/static/css/jquery.treeview.css" type="text/css" />

  <script type="text/javascript" src="/static/js/cms/page/index.js"></script>
    
</head>
<body>

<%@ include file="versionsBox.jsp" %>

<%@ include file="breadcrumbs.jsp" %>

<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all"
    style="margin-top: 14px;">

<%@ include file="tab.jsp" %>

<div id="tab-1" class="ui-tabs-panel ui-widget-content ui-corner-bottom">

<form id="pageForm">

<div id="titleDiv" class="form-row">
    <label><fmt:message key="title" /></label>
    <input id="title" type="text" class="form-title" size="40" />
</div>
<div class="form-row">
    <label><fmt:message key="page.friendly_url" /></label>
    <span id="parentFriendlyUrl"></span>
    <input id="friendlyUrl" type="text" />
    <span id="friendlyUrlSpan"></span>
</div>
<div class="form-row">
    <label><fmt:message key="template" /></label>
    <select id="templates"></select>
</div>
<div class="form-row">
    <label><fmt:message key="page.publication_date" /></label>
    <input id="publishDate" type="text" class="datepicker" size="8"/>
</div>
<div class="form-row">
    <label><fmt:message key="enable_comments" /></label>
    <input id="commentsEnabled" type="checkbox" />
</div>
<div class="form-row">
    <label><fmt:message key="page.include_search" /></label>
    <input id="searchable" type="checkbox" />
</div>
<div class="form-row">
    <label><fmt:message key="page.velocity_processing" /></label>
    <input id="velocityProcessing" type="checkbox" />
</div>
<div class="form-row">
    <label><fmt:message key="page.skip_postprocessing" /></label>
    <input id="skipPostProcessing" type="checkbox" />
</div>

<div class="form-row">
    <label><fmt:message key="page.type" /></label>
    <select id="pageType">
        <option value="SIMPLE"><fmt:message key="simple" /></option>
        <option value="STRUCTURED"><fmt:message key="structured" /></option>
    </select>
</div>

<div id="structuredControls">
<div class="form-row">
    <label><fmt:message key="structure" /></label>
    <select id="structure"></select>
</div>
<div class="form-row">
    <label><fmt:message key="page.structure_template" /></label>
    <select id="structureTemplate"></select>
</div>
</div>

<div class="form-row">
    <a id="metadata" href="#"><fmt:message key="page.metadata" /></a>
</div>
<div id="meta" style="display:none">
<div class="form-row">
    <label><fmt:message key="page.meta_keywords" /></label>
    <textarea id="keywords" rows="5" cols="80"></textarea>
</div>
<div class="form-row">
    <label><fmt:message key="page.meta_description" /></label>
    <textarea id="description" rows="5" cols="80"></textarea>
</div>
<div class="form-row">
    <label><fmt:message key="page.head_content" /></label>
    <textarea id="headHtml" rows="5" cols="80"></textarea>
</div>
</div>

<div class="form-row">
    <label><fmt:message key="tags" /></label>
    <a id="addTag" href="#"><fmt:message key="add_tag" /></a>
    <span id="tags"></span>
</div>

<div class="buttons">
    <div id="approveOnPageSaveDiv" class="checkboxes">
        <input id="approveOnPageSave" type="checkbox"> 
            <fmt:message key="page.approve_save" />
    </div> 
    <input id="pageSaveButton" type="submit" 
        value="<fmt:message key="save" />" />
    <input id="pagePreview" type="button" 
        value="<fmt:message key="preview" />" />
    <input id="pageCancelButton" type="button" 
        value="<fmt:message key="cancel" />" />
</div>    

</form>

</div>

</div>

<%@ include file="versionDialog.jsp" %>

<div id="tag-dialog" style="display:none" title="<fmt:message key="select_tag" />">
    <ul id="tagTree"></ul>
</div>


</body>
</html>