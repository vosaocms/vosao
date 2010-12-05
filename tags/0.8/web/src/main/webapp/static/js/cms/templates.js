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

var exportTimer = null;
var clockTimer = null;
var clockSeconds = 0;
var exportFilename = null;

$(function(){
    $("#import-dialog").dialog({ width: 400, autoOpen: false });
    $("#export-dialog").dialog({ width: 400, autoOpen: false });
    $('#upload').ajaxForm(afterUpload);
    Vosao.initJSONRpc(loadTemplates);
    $("#tabs").tabs();
    $('#addButton').click(onAdd);
    $('#deleteButton').click(onDelete);
    $('#exportButton').click(onExport);
    $('#exportCancelButton').click(onExportCancel);
    $('#importButton').click(onImport);
    $('#importCancelButton').click(onImportCancel);
    $('#okButton').click(onAfterUploadOk);
});

function afterUpload(data) {
    var s = data.split('::');
    var result = s[1];
    var msg = s[2]; 
    if (result == 'success') {
        msg = messages('templates.success_import');
    }
    else {
        msg = messages('error') + ". " + msg;
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
	Vosao.jsonrpc.templateService.getTemplates(function (r) {
        var html = '<table class="form-table"><tr><th></th><th>'
        	+ messages('title') + '</th></tr>';
        $.each(r.list, function (n, value) {
            html += '<tr><td><input type="checkbox" value="' + value.id 
                + '" /></td><td><a href="/cms/template.vm?id=' + value.id
                +'">' + value.title + '</a></td></tr>';
        });
        $('#templates').html(html + '</table>');
        $('#templates tr:even').addClass('even');
    });
}

function onAdd() {
	location.href = '/cms/template.vm';
}

function onDelete() {
    var ids = new Array();
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    if (confirm(messages('are_you_sure'))) {
    	Vosao.jsonrpc.templateService.deleteTemplates(function(r) {
    		Vosao.showServiceMessages(r);
            loadTemplates();
        }, Vosao.javaList(ids));
    }
}

function onExport() {
	clockSeconds = 0;
	showClock();
	var ids = [];
    $('#templates input:checked').each(function () {
        ids.push(this.value);
    });
    if (ids.length == 0) {
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    $("#export-dialog").dialog("open");
    Vosao.jsonrpc.configService.startExportThemeTask(function(r) {
    	if (r.result == 'success') {
    		$('#templates input:checked').each(function () {
    			this.checked = false;
    		});
    		exportFilename = r.message;
    	    Vosao.infoMessage('#exportInfo', messages('creating_export_file'));
            exportTimer = setInterval(checkExport, 10 * 1000);
            clockTimer = setInterval(showClock, 1000);
    	}
    	else {
    		Vosao.showServiceMessages(r);
    	}
    }, Vosao.javaList(ids));
}

function checkExport() {
	Vosao.jsonrpc.configService.isExportTaskFinished(function(r) {
		if (r) {
			clearInterval(exportTimer);
			clearInterval(clockTimer);
			$("#export-dialog").dialog("close");
			$('#exportDialogButton').attr('disabled', false);
			location.href = '/file/tmp/' + exportFilename;
		}
	}, 'theme');
}

function showClock() {
	$('#timer').html(clockSeconds++ + ' ' + messages('sec') + '.');
}

function onExportCancel() {
	$('#export-dialog').dialog('close');
	clearInterval(exportTimer);
	clearInterval(clockTimer);
}