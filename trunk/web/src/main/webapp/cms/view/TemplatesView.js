// file TemplatesView.js
/*
 Vosao CMS. Simple CMS for Google App Engine.
 
 Copyright (C) 2009-2011 Vosao development team.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 email: vosao.dev@gmail.com
*/

define(['text!template/templates.html', 'jquery.form'], function(tmpl) {
	
	console.log('Loading TemplatesView.js');
	
	var exportTimer = null;
	var clockTimer = null;
	var clockSeconds = 0;
	var exportFilename = null;

	function initData() {
		loadTemplates();
		loadStructures();
	}

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
	    $("#afterUpload-dialog").dialog('open');
	}

	function onImport() {
	    $("#import-dialog").dialog("open");
	}

	function onImportCancel() {
	    $("#import-dialog").dialog("close");
	}

	function onAfterUploadOk() {
	    $("#afterUpload-dialog").dialog('close');
	    initData();
	}

	function loadTemplates() {
		Vosao.jsonrpc.templateService.getTemplates(function (r) {
	        var html = '<table class="form-table"><tr><th></th><th>'
	        	+ messages('title') + '</th></tr>';
	        $.each(r.list, function (n, value) {
	            html += '<tr><td><input type="checkbox" value="' + value.id 
	                + '" /></td><td><a href="#template/' + value.id
	                +'">' + value.title + '</a></td></tr>';
	        });
	        $('#templates').html(html + '</table>');
	        $('#templates tr:even').addClass('even');
	    });
	}

	function loadStructures() {
		Vosao.jsonrpc.structureService.select(function(r) {
	        var html = '<table class="form-table"><tr><th></th><th>'
	        	+ messages('title') + '</th></tr>';
	        $.each(r.list, function (n, value) {
	            html += '<tr><td><input type="checkbox" value="' + value.id 
	                + '" /></td><td>' + value.title + '</td></tr>';
	        });
	        $('#structures').html(html + '</table>');
	        $('#structures tr:even').addClass('even');
		});
	}

	function onAdd() {
		location.href = '#template';
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
	    $('#structures input:checked').each(function() { this.checked = false; });
		$("#structures-dialog").dialog("open");
	}

	function onStartExport() {
		$("#structures-dialog").dialog("close");
		clockSeconds = 0;
		showClock();
		var ids = [];
		var structureIds = [];
	    $('#templates input:checked').each(function () {
	        ids.push(this.value);
	    });
	    if (ids.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    $('#structures input:checked').each(function () {
	        structureIds.push(this.value);
	    });
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
	    }, Vosao.javaList(ids), Vosao.javaList(structureIds));
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
	
	
	return Backbone.View.extend({
		
		el: $('#content'),
		
		render: function() {
			this.el.html(_.template(tmpl, {messages:messages}));
		    $("#import-dialog").dialog({ width: 400, autoOpen: false });
		    $("#export-dialog").dialog({ width: 400, autoOpen: false });
		    $("#structures-dialog").dialog({ width: 400, autoOpen: false });
		    $("#afterUpload-dialog").dialog({autoOpen: false});
		    $('#upload').ajaxForm(afterUpload);
		    initData();
		    $("#tabs").tabs();
		    $('#addButton').click(onAdd);
		    $('#deleteButton').click(onDelete);
		    $('#exportButton').click(onExport);
		    $('#exportCancelButton').click(onExportCancel);
		    $('#importButton').click(onImport);
		    $('#importCancelButton').click(onImportCancel);
		    $('#okButton').click(onAfterUploadOk);
		    $('#structuresForm').submit(function() {onStartExport(); return false;});
		    $('#structuresCancelButton').click(function() {
		    	$("#structures-dialog").dialog("close");
		    });
		},
		
		remove: function() {
			$('#import-dialog, #export-dialog, #structures-dialog, #afterUpload-dialog')
					.dialog('destroy').remove();
			
			this.el.html('');
		}
		
	});
	
});