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
    <title>Forms</title>
    <script src="/static/js/cms/plugins/forms.js" type="text/javascript"></script>    
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Forms</a></li>
    <li><a href="#tab-2">Config</a></li>
</ul>

<div id="tab-1">
    <div id="forms"> </div>
    <div class="buttons">
        <input id="addButton" type="button" value="Add" />
        <input id="deleteButton" type="button" value="Delete" />
    </div>
</div>

<div id="tab-2">

<div class="form-row">
    <label>Form template</label>
    <div>
        <a id="editFormTemplateLink" href="#">edit</a> 
        <a id="restoreFormTemplateLink" href="#">restore default</a>
    </div>
    <div>
        <textarea id="formTemplate" rows="20" cols="80" 
            style="display:none"></textarea>
    </div>
</div>
<div class="form-row">
    <label>Form letter template</label>
    <div>
        <a id="editFormLetterLink" href="#" onclick="">edit</a> 
        <a id="restoreFormLetterLink" href="#">restore default</a>
    </div>
    <div>
        <textarea id="letterTemplate" rows="20" cols="80" 
            style="display:none"></textarea>
    </div>
</div>
<div class="buttons">
    <input id="saveButton" type="button" value="Save" />
</div>
</div>

</div>

</body>
</html>