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
    <title><fmt:message key="structure"/></title>
    <script type="text/javascript">
        var structureId = '<c:out value="${param.id}"/>';
    </script>
    <script src="/static/js/cms/structure.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">

<ul>
    <li><a href="#tab-1"><fmt:message key="structure"/></a></li>
    <li><a href="#tab-2"><fmt:message key="structure.as_xml"/></a></li>
    <li><a href="#tab-3"><fmt:message key="structure.templates"/></a></li>
</ul>

<div id="tab-1">
    <div class="form-row">
        <label><fmt:message key="title"/></label>
        <input id="title" type="text" />
    </div>
    <div class="form-row">
        <label><fmt:message key="fields"/></label>
        <div id="fields"></div>    
    </div>
    <div class="form-row" style="margin-top: 20px;">
        <span><fmt:message key="structure.field_title"/></span>
        <input id="fieldTitle" type="text" /> &nbsp;
        <span><fmt:message key="structure.tag_name"/></span> &nbsp;
        <input id="fieldName" type="text" /> &nbsp;
        <select id="fieldType"> &nbsp;
            <option value="TEXT"><fmt:message key="text"/></option>
            <option value="TEXTAREA"><fmt:message key="text_area"/></option>
            <option value="RESOURCE"><fmt:message key="resource_link"/></option>
            <option value="DATE"><fmt:message key="date"/></option>
        </select>
        <a id="addField" href="#"> <fmt:message key="add_field"/></a>
    </div>
    <div class="buttons">
        <input id="saveContinueButton" type="button" value="<fmt:message key="save_continue"/>" />
        <input id="saveButton" type="button" value="<fmt:message key="save"/>" />
        <input id="cancelButton" type="button" value="<fmt:message key="cancel"/>" />
    </div>
</div>

<div id="tab-2">
    <div class="form-row">
        <div>
            <input id="autosave" type="checkbox"> <fmt:message key="autosave"/></input>
            <fmt:message key="editor_size"/>:
            <a id="bigLink" href="#"> <fmt:message key="big"/></a>
            <a id="smallLink" href="#"> <fmt:message key="small"/></a>
        </div>
        <textarea id="content" rows="20" cols="80" wrap="off"></textarea>
    </div>
    <div class="buttons">
        <input id="saveContinueXMLButton" type="button" 
            value="<fmt:message key="save_continue"/>" />
        <input id="saveXMLButton" type="button" value="<fmt:message key="save"/>" />
        <input id="cancelXMLButton" type="button" value="<fmt:message key="cancel"/>" />
    </div>
</div>

<div id="tab-3">
    <div id="templates"><img src="/static/images/ajax-loader.gif" /></div>
    <div class="buttons">
        <input id="addTemplateButton" type="button" value="<fmt:message key="add"/>"/>
        <input id="deleteTemplateButton" type="button" value="<fmt:message key="delete"/>"/>
    </div></div>
</div>

</body>
</html>