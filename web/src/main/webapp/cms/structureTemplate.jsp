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
    <title>Structure template</title>
    <script type="text/javascript">
        var structureTemplateId = '<c:out value="${param.id}"/>';
        var structureId = '<c:out value="${param.structureId}"/>';
    </script>
    <script src="/static/js/cms/structureTemplate.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1">Structure template</a></li>
</ul>

<div id="tab-1">
    <div class="form-row">
        <label>Title</label>
        <input id="title" type="text" />
    </div>
    <div class="form-row">
        <label>Template type</label>
        <select id="type">
            <option value="VELOCITY">Velocity</option>
            <option value="XSLT">XSLT</option>
        </select>
    </div>
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
        <input id="saveContinueButton" type="button" value="Save and continue" />
        <input id="saveButton" type="button" value="Save" />
        <input id="cancelButton" type="button" value="Cancel" />
    </div>
</div>

</div>

</body>
</html>