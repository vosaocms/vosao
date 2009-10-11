<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
  xmlns:ui="http://java.sun.com/jsf/facelets"
  xmlns:h="http://java.sun.com/jsf/html"
  xmlns:f="http://java.sun.com/jsf/core"
  template="WEB-INF/facelets/layout.xhtml">

<ui:define name="head">
    <link rel="stylesheet" href="/static/css/form.css" type="text/css" />
    <script type="text/javascript">
        var formId = '#{formBean.current.id}';
        //<!--
        var field = null;
        var fields = null;
        var test = null;

        $( function() {
            $("#tabs").tabs();
            $("#field-dialog").dialog({ width :500, autoOpen :false });
            initJSONRpc(loadFields);
        });

        function loadFields() {
            if (formId == '') {
                return;
            }
            jsonrpc.fieldService.getByForm(function(r, e) {
                fields = r;
                if (r.list.length > 0) {
                    var h = '<table class="form-table">';
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
                jsonrpc.fieldService.updateField( function(r, e) {
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
                $('input[name=field.optional]')[0].checked = false;
            } else {
                $('input[name=field.name]').val(field.name);
                $('input[name=field.title]').val(field.title);
                $('select[name=field.fieldType]').val(field.fieldType);
                $('textarea[name=field.values]').val(field.values);
                $('input[name=field.defaultValue]').val(field.defaultValue);
                $('input[name=field.height]').val(field.height);
                $('input[name=field.width]').val(field.width);
                $('input[name=field.optional]')[0].checked = field.optional;
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
                optional :String($('input[name=field.optional]:checked').size() > 0)
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
            $("#field-messages").html(
                    "<ul><li class=\"info-msg\">" + message + "</li></ul>");
        }

        function fieldErrorMessages(messages) {
            var h = '<ul>';
            for ( var i = 0; i < messages.length; i++) {
                h += '<li class="error-msg">' + messages[i] + '</li>';
            }
            h += '</li>';
            $("#field-messages").html(h);
        }

        function fieldErrorMessage(message) {
            $("#field-messages").html(
                    "<ul><li class=\"error-msg\">" + message + "</li></ul>");
        }

        function clearFieldMessage() {
            $('#field-messages').html('');
        }

        function onFieldEdit(fieldId) {
            clearFieldMessage();
            jsonrpc.fieldService.getById( function(r) {
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
                return;
            }
            jsonrpc.fieldService.remove(function() {
                infoMessage(ids.length + ' fields was successfully deleted.');
                loadFields();
            }, javaList(ids));
        }

        function onSaveAndAdd() {
            onFieldSave(false);
            onAddField();
        }
        
        // -->        
    </script>
</ui:define>

<ui:define name="title">Form</ui:define>

<ui:define name="body">

<h:form>

<div id="tabs">
<ul>
    <li><a href="#tab-1">Form</a></li>
<h:panelGroup rendered="#{formBean.edit}">
    <li><a href="#tab-2">Fields</a></li>
</h:panelGroup>
</ul>

<div id="tab-1">

<div class="form-row">
    <label>Title</label>
    <h:inputText value="#{formBean.current.title}" />
</div>
<div class="form-row">
    <label>Unique name</label>
    <h:inputText value="#{formBean.current.name}" />
</div>
<div class="form-row">
    <label>Email address</label>
    <h:inputText value="#{formBean.current.email}" />
</div>
<div class="form-row">
    <label>Letter subject</label>
    <h:inputText value="#{formBean.current.letterSubject}" />
</div>
<div class="form-row">
    <label>"Send" button title</label>
    <h:inputText value="#{formBean.current.sendButtonTitle}" />
</div>
<div class="form-row">
    <label>"Reset" button title</label>
    <h:inputText value="#{formBean.current.resetButtonTitle}" />
</div>
<div class="form-row">
    <label>Show "Reset" button</label>
    <h:selectBooleanCheckbox value="#{formBean.current.showResetButton}" />
</div>

<div class="buttons">
    <h:commandButton value="Save" action="#{formBean.update}" />
    <h:commandButton value="Cancel" action="#{formBean.cancelEdit}" />
</div>

</div>

<h:panelGroup rendered="#{formBean.edit}">
<div id="tab-2">
    <div id="fieldsTable"> </div>
    <div class="buttons">
        <input type="button" value="Add field" onclick="onAddField()" />
        <input type="button" value="Delete fields" onclick="onDeleteFields()" />
    </div>    
</div>
</h:panelGroup>

</div>
</h:form>

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
        <label>Optional</label>
        <input type="checkbox" name="field.optional" />
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
    <h:commandButton value="Save and Add" onclick="onSaveAndAdd()" />
        <input type="button" onclick="onFieldSave(true)" value="Save" />
        <input type="button" onclick="onFieldCancel()" value="Cancel" />
    </div>
</div>

</ui:define>

</ui:composition>
