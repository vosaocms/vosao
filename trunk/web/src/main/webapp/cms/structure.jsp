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
    <title>Structure</title>
    <script type="text/javascript">
        var structureId = '<c:out value="${param.id}"/>';
    </script>
    <script src="/static/js/cms/structure.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Structure</a></li>
    <li><a href="#tab-2">As XML</a></li>
    <li><a href="#tab-3">Structure templates</a></li>
</ul>

<div id="tab-1">
    <div class="form-row">
        <label>Title</label>
        <input id="title" type="text" />
    </div>
    <div class="form-row">
        <label>Fields</label>
        <div id="fields"></div>    
    </div>
    <div class="form-row" style="margin-top: 20px;">
        <span>Field title</span>
        <input id="fieldTitle" type="text" /> &nbsp;
        <span>Tag name</span> &nbsp;
        <input id="fieldName" type="text" /> &nbsp;
        <select id="fieldType"> &nbsp;
            <option value="TEXT">Text</option>
            <option value="TEXTAREA">Text area</option>
            <option value="RESOURCE">Resource link</option>
            <option value="DATE">Date</option>
        </select>
        <a id="addField" href="#"> Add field</a>
    </div>
    <div class="buttons">
        <input id="saveContinueButton" type="button" value="Save and continue" />
        <input id="saveButton" type="button" value="Save" />
        <input id="cancelButton" type="button" value="Cancel" />
    </div>
</div>

<div id="tab-2">
    <div class="form-row">
        <div>
            <input id="autosave" type="checkbox"> Autosave</input>
            Editor size:
            <a id="bigLink" href="#"> Big</a>
            <a id="smallLink" href="#"> Small</a>
        </div>
        <textarea id="content" rows="20" cols="80" wrap="off"></textarea>
    </div>
    <div class="buttons">
        <input id="saveContinueXMLButton" type="button" value="Save and continue" />
        <input id="saveXMLButton" type="button" value="Save" />
        <input id="cancelXMLButton" type="button" value="Cancel" />
    </div>
</div>

<div id="tab-3">
    <div id="templates"><img src="/static/images/ajax-loader.gif" /></div>
    <div class="buttons">
        <input id="addTemplateButton" type="button" value="Add"/>
        <input id="deleteTemplateButton" type="button" value="Delete"/>
    </div></div>
</div>

</body>
</html>