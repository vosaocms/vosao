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

<script type="text/javascript">

    var formConfig = '';

    $(function() {
        $("#tabs").tabs();
        initJSONRpc(loadData);
    });

    function loadData() {
        loadForms();
        loadFormConfig();
    }
    
    function loadForms() {
        formService.select(function (r) {
            var html = '<table class="form-table"><tr><th></th><th>Title</th>\
<th>Name</th><th>Email</th></tr>';
            $.each(r.list, function(i, form) {
                html += '<tr><td><input type="checkbox" value="' + form.id
                    + '"/></td><td><a href="/cms/plugins/form.jsp?id=' + form.id 
                    + '">' + form.title + '</a></td><td>' + form.name 
                    + '</td><td>' + form.email + '</td></tr>';
            });
            $('#forms').html(html + '</table>');
            $('#forms tr:even').addClass('even');
        });
    }

    function onAdd() {
        location.href = '/cms/plugins/form.jsp';
    }
    
    function onDelete() {
        var ids = [];
        $('#forms input:checked').each(function() {
            ids.push(this.value);
        });
        if (ids.length == 0) {
            info('Nothing selected');
            return;
        }
        if (confirm('Are you sure?')) {
            formService.deleteForm(function (r) {
                showServiceMessages(r);
                loadForms();
            }, javaList(ids));
        }
    }

    function loadFormConfig() {
        formService.getFormConfig(function (r) {
            formConfig = r;
            $('#formTemplate').val(r.formTemplate);
            $('#letterTemplate').val(r.letterTemplate);
        });
    }
    
    function onSaveConfig() {
        var vo = javaMap({
        	formTemplate : $('#formTemplate').val(),
        	letterTemplate : $('#letterTemplate').val()
        });
        formService.saveFormConfig(function (r) {
            showServiceMessages(r);
        }, vo);
    }

    function onFormTemplateRestore() {
        formService.restoreFormTemplate(function (r) {
            showServiceMessages(r);
            loadFormConfig();
        });
    }

    function onFormLetterRestore() {
        formService.restoreFormLetter(function (r) {
            showServiceMessages(r);
            loadFormConfig();
        });
    }

</script>    
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
        <input type="button" value="Add" onclick="onAdd()" />
        <input type="button" value="Delete" onclick="onDelete()" />
    </div>
</div>

<div id="tab-2">

<div class="form-row">
    <label>Form template</label>
    <div>
    <a href="#" onclick="$('#formTemplate').toggle()">edit</a> 
    <a href="#" onclick="onFormTemplateRestore()">restore default</a>
    </div>
    <div>
    <textarea id="formTemplate" rows="20" cols="80" style="display:none"></textarea>
    </div>
</div>
<div class="form-row">
    <label>Form letter template</label>
    <div>
    <a href="#" onclick="$('#letterTemplate').toggle()">edit</a> 
    <a href="#" onclick="onFormLetterRestore()">restore default</a>
    </div>
    <div>
    <textarea id="letterTemplate" rows="20" cols="80" style="display:none"></textarea>
    </div>
</div>
<div class="buttons">
    <input type="button" value="Save" onclick="onSaveConfig()" />
</div>
</div>

</div>

</body>
</html>