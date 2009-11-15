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
    initJSONRpc(loadTemplate);
    $("#tabs").tabs();
    $('#autosave').change(onAutosave);
    $('#bigLink').click(onBig);
    $('#smallLink').click(onSmall);
    $('#saveContinueButton').click(onSaveContinue);
    $('#saveButton').click(onSave);
    $('#cancelButton').click(onCancel);
});

function onSaveContinue() {
	onUpdate(true);
}

function onSave() {
	onUpdate(false);
}
    
function startAutosave() {
    if (templateId != 'null') {
        if (autosaveTimer == '') {
            autosaveTimer = setInterval(saveContent, AUTOSAVE_TIMEOUT * 1000);
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
    templateService.updateContent(function(r) {
        if (r.result == 'success') {
            var now = new Date();
            info(r.message + " " + now);
        }
        else {
            error(r.message);
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
	if (!editMode) {
		template = null;
        initTemplateForm();
	}
	templateService.getTemplate(function (r) {
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
	var templateVO = javaMap({
	    id : templateId,
	    title : $('#title').val(),
        url : $('#url').val(),
        content : $('#content').val(),
	});
	templateService.saveTemplate(function (r) {
		showServiceMessages(r);
		if (r.result == 'success' && !cont) {
			location.href = '/cms/templates.jsp';
		}
	}, templateVO);
}

function onBig() {
	$('#content').attr('cols','120');
    $('#content').attr('rows','30');
}

function onSmall() {
    $('#content').attr('cols','80');
    $('#content').attr('rows','20');
}

