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
        
        //<!--
        
        var editMode = formId != '';
        var field = null;
        var fields = null;
        var test = null;

        $( function() {
            $("#tabs").tabs();
            $("#field-dialog").dialog({ width :500, autoOpen :false });
            initJSONRpc(loadData);
        });

        function loadData() {
            loadForm();
            loadFields();
        }
        
        function loadFields() {
            if (formId == '') {
                return;
            }
            fieldService.getByForm(function(r, e) {
                fields = r;
                if (r.list.length > 0) {
                    var h = '<table class="form-table"><tr><th></th><th>Title</th><th>Name</th><th>Type</th></tr>';
                    for ( var i = 0; i < r.list.length; i++) {
                        var field = r.list[i];
                        h += 
'<tr>\
<td><input type="checkbox" name="item' + i + '" value="' + field.id + '"/></td>\
<td><a href="#" onclick="onFieldEdit(\'' + field.id + '\')">'   + field.title + '</a></td>\
<td>' + field.name + '</td>\
<td>'   + fieldTypeString(field.fieldType) + '</td>\
</tr>';
                    }
                    h += '</table>';
                    $('#fieldsTable').html(h);
                    $('#fieldsTable tr:even').addClass('even');
                }
            }, formId);
        }

        function fieldTypeString(v) {
            if (v == 'TEXT') return 'Text';
            if (v == 'CHECKBOX') return 'Checkbox';
            if (v == 'RADIO') return 'Radiobox';
            if (v == 'PASSWORD') return 'Password';
            if (v == 'LISTBOX') return 'Listbox';
            if (v == 'FILE') return 'File upload';
            return 'undefined';
        }

        function onAddField() {
            field = null;
            fieldDialogInit();
            $("#field-dialog").dialog("open");
        }

        function onFieldCancel() {
            $("#field-dialog").dialog("close");
        }

        function onFieldSave(closeFlag) {
            var fieldVO = createFieldVO();
            var errors = validateField(fieldVO);
            if (errors.length == 0) {
                fieldService.updateField( function(r, e) {
                    if (r.result == 'success') {
                        if (closeFlag) {
                            $("#field-dialog").dialog("close");
                        }
                        loadFields();
                    } else {
                        fieldErrorMessages(r.messages.list);
                    }
                }, fieldVO);
            } else {
                fieldErrorMessages(errors);
            }
        }

        function onFieldTypeChange() {
            fieldDialogShowInputs();
        }

        function fieldDialogShowInputs() {
            var fieldType = $('select[name=field.fieldType]').val();
            if (fieldType == 'TEXT') {
                $('#field-values').hide();
                $('#field-height').show();
                $('#field-width').show();
                $('#field-defaultValue').show();
            }
            if (fieldType == 'LISTBOX') {
                $('#field-values').show();
                $('#field-height').hide();
                $('#field-width').hide();
                $('#field-defaultValue').hide();
            }
            if (fieldType == 'CHECKBOX') {
                $('#field-values').show();
                $('#field-height').hide();
                $('#field-width').hide();
                $('#field-defaultValue').hide();
            }
            if (fieldType == 'RADIO') {
                $('#field-values').show();
                $('#field-height').hide();
                $('#field-width').hide();
                $('#field-defaultValue').hide();
            }
            if (fieldType == 'PASSWORD') {
                $('#field-values').hide();
                $('#field-height').hide();
                $('#field-width').show();
                $('#field-defaultValue').hide();
            }
            if (fieldType == 'FILE') {
                $('#field-values').hide();
                $('#field-height').hide();
                $('#field-width').hide();
                $('#field-defaultValue').hide();
            }
        }

        function fieldDialogInit() {
            if (field == null) {
                $('input[name=field.name]').val('');
                $('input[name=field.title]').val('');
                $('select[name=field.fieldType]').val('TEXT');
                $('textarea[name=field.values]').val('');
                $('input[name=field.defaultValue]').val('');
                $('input[name=field.height]').val('1');
                $('input[name=field.width]').val('20');
                $('input[name=field.mandatory]')[0].checked = false;
            } else {
                $('input[name=field.name]').val(field.name);
                $('input[name=field.title]').val(field.title);
                $('select[name=field.fieldType]').val(field.fieldType);
                $('textarea[name=field.values]').val(field.values);
                $('input[name=field.defaultValue]').val(field.defaultValue);
                $('input[name=field.height]').val(field.height);
                $('input[name=field.width]').val(field.width);
                $('input[name=field.mandatory]')[0].checked = field.optional;
            }
            fieldDialogShowInputs();
        }

        function createFieldVO() {
            return javaMap( {
                id :field != null ? field.id : null,
                formId :formId,
                name :$('input[name=field.name]').val(),
                title :$('input[name=field.title]').val(),
                fieldType :$('select[name=field.fieldType]').val(),
                values :$('textarea[name=field.values]').val(),
                defaultValue :$('input[name=field.defaultValue]').val(),
                height :$('input[name=field.height]').val(),
                width :$('input[name=field.width]').val(),
                mandatory :String($('input[name=field.mandatory]:checked').size() > 0)
            });
        }

        function validateField(fieldVO) {
            var errors = new Array();
            if (fieldVO.map.name == '') {
                errors.push('Name is empty');
            }
            if (fieldVO.map.title == '') {
                errors.push('Title is empty');
            }
            var height = Number(fieldVO.map.height);
            if (fieldVO.map.fieldType == 'TEXT' && height <= 0) {
                errors.push('Height can\'t be less or zero');
            }
            var width = Number(fieldVO.map.width);
            if (fieldVO.map.fieldType == 'TEXT' && width <= 0) {
                errors.push('Width can\'t be less or zero');
            }
            return errors;
        }

        function fieldInfoMessage(message) {
            infoMessage('#field-messages', message);
        }

        function fieldErrorMessages(messages) {
            errorMessages('#field-messages', messages);
        }

        function fieldErrorMessage(message) {
            errorMessage('#field-messages', message);
        }

        function clearFieldMessage() {
            $('#field-messages').html('');
        }

        function onFieldEdit(fieldId) {
            clearFieldMessage();
            fieldService.getById( function(r) {
                field = r;
                fieldDialogInit();
                $("#field-dialog").dialog("open");
            }, fieldId);
        }

        function onDeleteFields() {
            var ids = new Array();
            $('input:checked').each(function() {
                ids.push(this.value);
            });
            if (ids.length == 0) {
                info('Nothing selected');
                return;
            }
            if (confirm('Are you sure?')) {
                fieldService.remove(function() {
                    info(ids.length + ' fields was successfully deleted.');
                    loadFields();
                }, javaList(ids));
            }
        }

        function onSaveAndAdd() {
            onFieldSave(false);
            onAddField();
        }

        function loadForm() {
            formService.getForm(function (r) {
                if (r != null) {
                    $('#title').val(r.title);
                    $('#name').val(r.name);
                    $('#email').val(r.email);
                    $('#letterSubject').val(r.letterSubject);
                    $('#sendButtonTitle').val(r.sendButtonTitle);
                    $('#resetButtonTitle').val(r.resetButtonTitle);
                    $('#showResetButton').each(function() {
                        this.checked = r.showResetButton;
                    });
                    $('#enableCaptcha').each(function() {
                        this.checked = r.enableCaptcha;
                    });
                    $('.fieldsTab').show();
                }
                else {
                    $('#title').val('');
                    $('#name').val('');
                    $('#email').val('');
                    $('#letterSubject').val('');
                    $('#sendButtonTitle').val('');
                    $('#resetButtonTitle').val('');
                    $('#showResetButton').each(function() {
                        this.checked = false;
                    });
                    $('#enableCaptcha').each(function() {
                        this.checked = false;
                    });
                    $('.fieldsTab').hide();
                }
            }, formId);
        }
        
        function onUpdate() {
            var vo = javaMap({
                id : formId,
            	title : $('#title').val(),
            	name : $('#name').val(),
                email : $('#email').val(),
                letterSubject : $('#letterSubject').val(),
                sendButtonTitle : $('#sendButtonTitle').val(),
                resetButtonTitle : $('#resetButtonTitle').val(),
                showResetButton : String($('#showResetButton:checked').size() > 0),
                enableCaptcha : String($('#enableCaptcha:checked').size() > 0),
            });
            formService.saveForm(function (r) {
                if (r.result = 'success') {
                    location.href = '/cms/plugins/forms.jsp';
                }
                else {
                    showServiceMessages(r);
                }
            }, vo);
        }

        function onCancel() {
            location.href = '/cms/plugins/forms.jsp';
        }

        // -->        
</script>
</head>
<body>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Form</a></li>
    <li class="fieldsTab"><a href="#tab-2">Fields</a></li>
</ul>

<div id="tab-1">

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
    <input type="button" value="Save" onclick="onUpdate()" />
    <input type="button" value="Cancel" onclick="onCancel()" />
</div>

</div>

<div id="tab-2" class="fieldsTab">
    <div id="fieldsTable"> </div>
    <div class="buttons">
        <input type="button" value="Add field" onclick="onAddField()" />
        <input type="button" value="Delete fields" onclick="onDeleteFields()" />
    </div>    
</div>

</div>

<div id="field-dialog" style="display:none" title="Field details">
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
        <select name="field.fieldType" onchange="onFieldTypeChange()">
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
        <input type="button" value="Save and Add" onclick="onSaveAndAdd()" />
        <input type="button" value="Save" onclick="onFieldSave(true)" />
        <input type="button" value="Cancel" onclick="onFieldCancel()" />
    </div>
</div>

</body>
</html>