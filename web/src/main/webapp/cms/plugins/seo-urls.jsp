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
    <title><fmt:message key="plugins.seo_urls" /></title>
    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />
    <script src="/static/js/cms/plugins/seo-urls.js" type="text/javascript"></script>    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1"><fmt:message key="plugins.seo_urls" /></a></li>
</ul>

<div id="tab-1">

<div id="urls"><img src="/static/images/ajax-loader.gif" /></div>

<div class="buttons">
    <input id="addButton" type="button" value="<fmt:message key="add" />" />
    <input id="removeButton" type="button" value="<fmt:message key="remove" />" />
</div>

<div id="url-dialog" style="display:none" title="<fmt:message key="seo_urls.details" />">
  <form id="seoForm">
    <div id="url-messages" class="messages"> </div>
    <div class="form-row">
        <label><fmt:message key="seo_urls.from_link" /></label>
        <input id="fromLink" type="text" />
    </div>
    <div class="form-row">
        <label><fmt:message key="seo_urls.to_link" /></label>
        <input id="toLink" type="text" />
    </div>
    <div class="buttons-dlg">
        <input id="saveAndAddButton" type="button" value="<fmt:message key="save_add" />" />
        <input id="saveDlgButton" type="submit" value="<fmt:message key="save" />" />
        <input id="cancelDlgButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
  </form>
</div>

</div>

</div>

</body>
</html>