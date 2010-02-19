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

var template = '';
var editMode = templateId != '';
var autosaveTimer = '';
    
$(function(){
	Vosao.initJSONRpc(loadData);
    $('#autosave').change(onAutosave);
    $('#sizeLink').click(onSize);
    $('#wrapLink').click(onWrap);
    $('#saveContinueButton').click(onSaveContinue);
    $('#saveButton').click(onSave);
    $('#templateForm').submit(function() {onSave(); return false;});
    $('#cancelButton').click(onCancel);
    $('#resources').click(onResources);
});

function loadData() {
	loadTemplate();
}

function onSaveContinue() {
	onUpdate(true);
}

function onSave() {
	onUpdate(false);
}
    
function startAutosave() {
    if (templateId != 'null') {
        if (autosaveTimer == '') {
            autosaveTimer = setInterval(saveContent, 
            		Vosao.AUTOSAVE_TIMEOUT * 1000);
        }
    }
}
    
function stopAutosave() {
    if (autosaveTimer != '') {
        clearInterval(autosaveTimer);
        autosaveTimer = '';
    }
}

function saveContent() {
    var content = $("#content").val();
    Vosao.jsonrpc.templateService.updateContent(function(r) {
        if (r.result == 'success') {
            var now = new Date();
            Vosao.info(r.message + " " + now);
        }
        else {
        	Vosao.error(r.message);
        }            
    }, templateId, content);        
}

function onAutosave() {
    if ($("#autosave:checked").length > 0) {
        startAutosave(); 
    }
    else {
        stopAutosave();
    }
}

function loadTemplate() {
	editMode = templateId != '';
	if (!editMode) {
		template = null;
        initTemplateForm();
	}
	Vosao.jsonrpc.templateService.getTemplate(function (r) {
		template = r;
		initTemplateForm();
	}, templateId);
}

function initTemplateForm() {
	if (template != null) {
		$('#title').val(template.title);
        $('#url').val(template.url);
        $('#content').val(template.content);
	}
	else {
        $('#title').val('');
        $('#url').val('');
        $('#content').val('');
	}
}

function onCancel() {
    location.href = '/cms/templates.jsp';
}

function onUpdate(cont) {
	var templateVO = Vosao.javaMap({
	    id : templateId,
	    title : $('#title').val(),
        url : $('#url').val(),
        content : $('#content').val(),
	});
	Vosao.jsonrpc.templateService.saveTemplate(function (r) {
		if (r.result == 'success') {
			Vosao.info('Template was successfully saved.');
			if (!cont) {
				location.href = '/cms/templates.jsp';
			}
			else if (!editMode) {
				templateId = r.message;
				loadTemplate();
			}
		}
		else {
			Vosao.showServiceMessages(r);
		}			
	}, templateVO);
}

function onSize() {
	var label = $('#sizeLink').text();
	if (label == 'Big') {
		$('#content').attr('cols','120');
		$('#content').attr('rows','30');
		$('#sizeLink').text('Small');
	}
	if (label == 'Small') {
		$('#content').attr('cols','80');
		$('#content').attr('rows','20');
		$('#sizeLink').text('Big');
	}
}

function onWrap() {
	var wrap = $('#content').attr('wrap');
	var text = $('#content').val();
	var cols = $('#content').attr('cols');
	var rows = $('#content').attr('rows');
	if (wrap == 'off') {
		$('#contentDiv').html('<textarea id="content" cols="' + cols
				+'" rows="' + rows + '"></textarea>');
		$('#content').val(text);
		$('#wrapLink').text(' Unwrap');
	}
	if (wrap == undefined) {
		$('#contentDiv').html('<textarea id="content" cols="' + cols
				+'" rows="' + rows + '" wrap="off"></textarea>');
		$('#content').val(text);
		$('#wrapLink').text(' Wrap');
	}
}

// Resources

function onResources() {
	$.cookie('folderReturnPath', '/cms/template.jsp?id=' + template.id, 
			{path:'/', expires: 10});
	Vosao.jsonrpc.folderService.createFolderByPath(function(r) {
		location.href = '/cms/folder.jsp?tab=1&id=' + r.id;
    }, '/theme/' + template.url);
}

