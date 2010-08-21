/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2010 Vosao development team.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * email: vosao.dev@gmail.com
 */

var formConfig = '';

$(function() {
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
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
	Vosao.jsonrpc.formService.select(function (r) {
        var html = '<table class="form-table"><tr><th></th><th>' + messages('title')
        	+ '</th><th>' + messages('name') + '</th><th>' + messages('email') 
        	+ '</th></tr>';
        $.each(r.list, function(i, form) {
            html += '<tr><td><input type="checkbox" value="' + form.id
                + '"/></td><td><a href="/cms/plugins/form.vm?id=' + form.id 
                + '">' + form.title + '</a></td><td>' + form.name 
                + '</td><td>' + form.email + '</td></tr>';
        });
        $('#forms').html(html + '</table>');
        $('#forms tr:even').addClass('even');
    });
}

function onAdd() {
    location.href = '/cms/plugins/form.vm';
}
    
function onDelete() {
    var ids = [];
    $('#forms input:checked').each(function() {
        ids.push(this.value);
    });
    if (ids.length == 0) {
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    if (confirm(messages('are_you_sure'))) {
    	Vosao.jsonrpc.formService.deleteForm(function (r) {
    		Vosao.showServiceMessages(r);
            loadForms();
        }, Vosao.javaList(ids));
    }
}

function loadFormConfig() {
	Vosao.jsonrpc.formService.getFormConfig(function (r) {
        formConfig = r;
        $('#formTemplate').val(r.formTemplate);
        $('#letterTemplate').val(r.letterTemplate);
    });
}
    
function onSaveConfig() {
    var vo = Vosao.javaMap({
   	    formTemplate : $('#formTemplate').val(),
   	    letterTemplate : $('#letterTemplate').val()
    });
    Vosao.jsonrpc.formService.saveFormConfig(function (r) {
    	Vosao.showServiceMessages(r);
    }, vo);
}

function onFormTemplateRestore() {
	Vosao.jsonrpc.formService.restoreFormTemplate(function (r) {
		Vosao.showServiceMessages(r);
        loadFormConfig();
    });
}

function onFormLetterRestore() {
	Vosao.jsonrpc.formService.restoreFormLetter(function (r) {
		Vosao.showServiceMessages(r);
        loadFormConfig();
    });
}