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
    <title>Form</title>
    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />
    <script type="text/javascript">
        var formId = '<c:out value="${param.id}"/>';
    </script>
    <script src="/static/js/cms/plugins/form.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Form</a></li>
    <li class="fieldsTab"><a href="#tab-2">Fields</a></li>
</ul>

<div id="tab-1">
<form id="form">
<div class="form-row">
    <label>Title</label>
    <input id="title" type="text" />
</div>
<div class="form-row">
    <label>Unique name</label>
    <input id="name" type="text" />
</div>
<div class="form-row">
    <label>Email address</label>
    <input id="email" type="text" />
</div>
<div class="form-row">
    <label>Letter subject</label>
    <input id="letterSubject" type="text" />
</div>
<div class="form-row">
    <label>"Send" button title</label>
    <input id="sendButtonTitle" type="text"/>
</div>
<div class="form-row">
    <label>"Reset" button title</label>
    <input id="resetButtonTitle" type="text"/>
</div>
<div class="form-row">
    <label>Show "Reset" button</label>
    <input id="showResetButton" type="checkbox"/>
</div>
<div class="form-row">
    <label>Enable captcha</label>
    <input id="enableCaptcha" type="checkbox"/>
</div>

<div class="buttons">
    <input id="saveButton" type="submit" value="Save" />
    <input id="cancelButton" type="button" value="Cancel" />
</div>
</form>
</div>

<div id="tab-2" class="fieldsTab">
    <div id="fieldsTable"> </div>
    <div class="buttons">
        <input id="addFieldButton" type="button" value="Add field" />
        <input id="deleteFieldButton" type="button" value="Delete fields" />
    </div>    
</div>

</div>

<div id="field-dialog" style="display:none" title="Field details">
<form id="fieldForm">
    <div id="field-messages" class="messages"> </div>
    <div class="form-row">
        <label>Title</label>
        <input type="text" name="field.title" />
    </div>
    <div class="form-row">
        <label>Unique name</label>
        <input type="text" name="field.name" />
    </div>
    <div class="form-row">
        <label>Field type</label>
        <select id="fieldType" name="field.fieldType">
            <option value="TEXT">Text</option>
            <option value="CHECKBOX">Checkbox</option>
            <option value="RADIO">Radiobox</option>
            <option value="PASSWORD">Password</option>
            <option value="LISTBOX">Listbox</option>
            <option value="FILE">File upload</option>
        </select>
    </div>
    <div class="form-row" id="field-width">
        <label>Width in chars</label>
        <input type="text" name="field.width" />
    </div>
    <div class="form-row" id="field-height">
        <label>Height in chars</label>
        <input type="text" name="field.height" />
    </div>
    <div class="form-row">
        <label>Mandatory</label>
        <input type="checkbox" name="field.mandatory" />
    </div>
    <div class="form-row" id="field-values">
        <label>Values</label>
        <textarea name="field.values"></textarea>
    </div>
    <div class="form-row" id="field-defaultValue">
        <label>Default value</label>
        <input type="text" name="field.defaultValue"/>
    </div>
    <div class="buttons-dlg">
        <input id="saveAndAddButton" type="submit" value="Save and Add" />
        <input id="fieldSaveButton" type="button" value="Save" />
        <input id="fieldCancelButton" type="button" value="Cancel" />
    </div>
</form>
</div>

</body>
</html>