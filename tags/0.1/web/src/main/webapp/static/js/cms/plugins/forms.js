/**
 * Vosao CMS. Simple CMS for Google App Engine. Copyright (C) 2009 Vosao
 * development team
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 * email: vosao.dev@gmail.com
 */

var formConfig = '';

$(function() {
    $("#tabs").tabs();
    initJSONRpc(loadData);
    $('#addButton').click(onAdd);
    $('#deleteButton').click(onDelete);
    $('#editFormTemplateLink').click(function() { $('#formTemplate').toggle(); });
    $('#restoreFormTemplateLink').click(onFormTemplateRestore);
    $('#editFormLetterLink').click(function() { $('#letterTemplate').toggle(); });
    $('#restoreFormLetterLink').click(onFormLetterRestore);
    $('#saveButton').click(onSaveConfig);
});

function loadData() {
    loadForms();
    loadFormConfig();
}
    
function loadForms() {
	jsonrpc.formService.select(function (r) {
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
    	jsonrpc.formService.deleteForm(function (r) {
            showServiceMessages(r);
            loadForms();
        }, javaList(ids));
    }
}

function loadFormConfig() {
	jsonrpc.formService.getFormConfig(function (r) {
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
    jsonrpc.formService.saveFormConfig(function (r) {
        showServiceMessages(r);
    }, vo);
}

function onFormTemplateRestore() {
	jsonrpc.formService.restoreFormTemplate(function (r) {
        showServiceMessages(r);
        loadFormConfig();
    });
}

function onFormLetterRestore() {
	jsonrpc.formService.restoreFormLetter(function (r) {
        showServiceMessages(r);
        loadFormConfig();
    });
}