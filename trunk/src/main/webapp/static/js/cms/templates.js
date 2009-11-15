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

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $('#upload').ajaxForm(afterUpload);
    initJSONRpc(loadTemplates);
    $("#tabs").tabs();
    $('#addButton').click(onAdd);
    $('#deleteButton').click(onDelete);
    $('#exportButton').click(onExport);
    $('#importButton').click(onImport);
    $('#importCancelButton').click(onImportCancel);
    $('#okButton').click(onAfterUploadOk);
});

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        msg = 'Success. File was successfully imported.';
    }
    else {
        msg = "Error. " + msg;
    }   
    $("#import-dialog").dialog("close");
    $("#afterUpload-dialog .message").text(msg);
    $("#afterUpload-dialog").dialog();
}

function onImport() {
    $("#import-dialog").dialog("open");
}

function onImportCancel() {
    $("#import-dialog").dialog("close");
}

function onAfterUploadOk() {
    window.location.reload();
}

function loadTemplates() {
    templateService.getTemplates(function (r) {
        var html = '<table class="form-table"><tr><th></th><th>Title</th></tr>';
        $.each(r.list, function (n, value) {
            html += '<tr><td><input type="checkbox" value="' + value.id 
                + '" /></td><td><a href="/cms/template.jsp?id=' + value.id
                +'">' + value.title + '</a></td></tr>';
        });
        $('#templates').html(html + '</table>');
        $('#templates tr:even').addClass('even');
    });
}

function onAdd() {
	location.href = '/cms/template.jsp';
}

function onDelete() {
    var ids = new Array();
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected.');
        return;
    }
    if (confirm('Are you sure?')) {
        templateService.deleteTemplates(function(r) {
            showServiceMessages(r);
            loadTemapltes();
        }, javaList(ids));
    }
}

function onExport() {
    var ids = new Array();
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
        info('Nothing selected.');
        return;
    }
    var link = '/cms/export?type=theme&ids=';
    $.each(ids, function (n, value) {
        if (n == 0) {
            link += value;
        }
        else {
            link += '::' + value;
        }
    });
    location.href = link;
    $('#templates input:checked').each(function () {
        this.checked = false;
    });
    info('Themes were successfully exported.');
}

