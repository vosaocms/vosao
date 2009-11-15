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
    <title>SEO Urls</title>
    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />
    <script src="/static/js/cms/plugins/seo-urls.js" type="text/javascript"></script>    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">SEO Urls</a></li>
</ul>

<div id="tab-1">

<div id="urls"><img src="/static/images/ajax-loader.gif" /></div>

<div class="buttons">
    <input id="addButton" type="button" value="Add" />
    <input id="removeButton" type="button" value="Remove" />
</div>

<div id="url-dialog" style="display:none" title="SEO Url details">
    <div id="url-messages" class="messages"> </div>
    <div class="form-row">
        <label>From Link URL</label>
        <input id="fromLink" type="text" />
    </div>
    <div class="form-row">
        <label>On site redirect link URL</label>
        <input id="toLink" type="text" />
    </div>
    <div class="buttons-dlg">
        <input id="saveAndAddButton" type="button" value="Save and Add" />
        <input id="saveDlgButton" type="button" value="Save" />
        <input id="cancelDlgButton" type="button" value="Cancel" />
    </div>
</div>

</div>

</div>

</body>
</html>