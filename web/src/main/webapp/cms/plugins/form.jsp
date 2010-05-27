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
    <title><fmt:message key="form" /></title>
    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />
    <script type="text/javascript">
        var formId = '<c:out value="${param.id}"/>';
    </script>
    <script src="/static/js/cms/plugins/form.js" type="text/javascript"></script>
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1"><fmt:message key="form" /></a></li>
    <li class="fieldsTab"><a href="#tab-2"><fmt:message key="fields" /></a></li>
    <li class="dataTab"><a href="#tab-3"><fmt:message key="saved_data" /></a></li>
</ul>

<div id="tab-1">
<form id="form">
<div class="form-row">
    <label><fmt:message key="title" /></label>
    <input id="title" type="text" />
</div>
<div class="form-row">
    <label><fmt:message key="form.unique_name" /></label>
    <input id="name" type="text" />
</div>
<div class="form-row">
    <label><fmt:message key="form.email" /></label>
    <input id="email" type="text" />
</div>
<div class="form-row">
    <label><fmt:message key="letter_subject" /></label>
    <input id="letterSubject" type="text" />
</div>
<div class="form-row">
    <label><fmt:message key="form.send_title" /></label>
    <input id="sendButtonTitle" type="text"/>
</div>
<div class="form-row">
    <label><fmt:message key="form.reset_title" /></label>
    <input id="resetButtonTitle" type="text"/>
</div>
<div class="form-row">
    <label><fmt:message key="form.show_reset" /></label>
    <input id="showResetButton" type="checkbox"/>
</div>
<div class="form-row">
    <label><fmt:message key="enable_captcha" /></label>
    <input id="enableCaptcha" type="checkbox"/>
</div>
<div class="form-row">
    <label><fmt:message key="enable_save" /></label>
    <input id="enableSave" type="checkbox"/>
</div>

<div class="buttons">
    <input id="saveButton" type="submit" value="<fmt:message key="save" />" />
    <input id="cancelButton" type="button" value="<fmt:message key="cancel" />" />
</div>
</form>
</div>

<div id="tab-2" class="fieldsTab">
    <div id="fieldsTable"> </div>
    <div class="buttons">
        <input id="addFieldButton" type="button" value="<fmt:message key="add_field" />" />
        <input id="deleteFieldButton" type="button" value="<fmt:message key="delete_fields" />" />
    </div>    
</div>

<div id="tab-3" class="dataTab">
    <div id="dataTable"></div>
    <div class="buttons">
        <input id="deleteDataButton" type="button" value="<fmt:message key="delete" />" />
    </div>    
</div>

</div>

<div id="field-dialog" style="display:none" title="<fmt:message key="form.field_details" />">
<form id="fieldForm">
    <div id="field-messages" class="messages"> </div>
    <div class="form-row">
        <label><fmt:message key="title" /></label>
        <input type="text" name="field.title" />
    </div>
    <div class="form-row">
        <label><fmt:message key="form.unique_name" /></label>
        <input type="text" name="field.name" />
    </div>
    <div class="form-row">
        <label><fmt:message key="form.field_type" /></label>
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
        <label><fmt:message key="form.width_chars" /></label>
        <input type="text" name="field.width" />
    </div>
    <div class="form-row" id="field-height">
        <label><fmt:message key="form.height_chars" /></label>
        <input type="text" name="field.height" />
    </div>
    <div class="form-row">
        <label><fmt:message key="mandatory" /></label>
        <input type="checkbox" name="field.mandatory" />
    </div>
    <div class="form-row" id="field-values">
        <label><fmt:message key="values" /></label>
        <textarea name="field.values"></textarea>
    </div>
    <div class="form-row" id="field-defaultValue">
        <label><fmt:message key="default_value" /></label>
        <input type="text" name="field.defaultValue"/>
    </div>
    <div id="regexDiv">
    <div class="form-row" id="field-defaultValue">
        <label><fmt:message key="form.regex_code" /></label>
        <input type="text" name="field.regex"/>
    </div>
    <div class="form-row" id="field-defaultValue">
        <label><fmt:message key="form.regex_message" /></label>
        <select id="language"></select> 
        <input type="text" name="field.regexMessage"/>
    </div>
    </div>
    <div class="buttons-dlg">
        <input id="saveAndAddButton" type="submit" value="<fmt:message key="save_add" />" />
        <input id="fieldSaveButton" type="button" value="<fmt:message key="save" />" />
        <input id="fieldCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
</form>
</div>

<div id="formData-dialog" style="display:none" title="<fmt:message key="form.form_data" />">
<form id="formDataForm">
    <div id="formData"></div>
    <div class="buttons-dlg">
        <input id="formDataSendButton" type="button" value="<fmt:message key="send" />" />
        <input id="formDataCancelButton" type="button" value="<fmt:message key="cancel" />" />
    </div>
</form>
</div>


</body>
</html>