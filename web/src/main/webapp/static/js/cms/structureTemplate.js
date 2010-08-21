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

var structureTemplateId = Vosao.getQueryParam('id');
var structureId = Vosao.getQueryParam('structureId');

var structureTemplate = '';
var editMode = structureTemplateId != '';
var autosaveTimer = '';
    
$(function(){
	Vosao.initJSONRpc(loadStructureTemplate);
    $("#tabs").tabs();
    $('#autosave').change(onAutosave);
    $('#bigLink').click(onBig);
    $('#smallLink').click(onSmall);
    $('#saveContinueButton').click(onSaveContinue);
    $('#saveButton').click(onSave);
    $('#cancelButton').click(onCancel);
    $('#title').change(onTitleChange);
});

function onTitleChange() {
	if (editMode) {
		return;
	}
	var name = $("#name").val();
	var title = $("#title").val();
	if (name == '') {
		$("#name").val(Vosao.urlFromTitle(title));
	}
}

function onSaveContinue() {
	onUpdate(true);
}

function onSave() {
	onUpdate(false);
}
    
function startAutosave() {
    if (structureTemplateId != 'null') {
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
	onUpdate(true);   
}

function onAutosave() {
    if ($("#autosave:checked").length > 0) {
        startAutosave(); 
    }
    else {
        stopAutosave();
    }
}

function loadStructureTemplate() {
	editMode = structureTemplateId != '';
	if (!editMode) {
		structureTemplate = null;
        initStructureTemplateForm();
	}
	Vosao.jsonrpc.structureTemplateService.getById(function (r) {
		structureTemplate = r;
		if (editMode) {
			structureId = structureTemplate.structureId;
		}
		initStructureTemplateForm();
	}, structureTemplateId);
}

function initStructureTemplateForm() {
	if (structureTemplate != null) {
		$('#name').val(structureTemplate.name);
		$('#title').val(structureTemplate.title);
		//$('#type').val(structureTemplate.typeString);
        $('#content').val(structureTemplate.content);
	}
	else {
        $('#name').val('');
        $('#title').val('');
        //$('#type').val('VELOCITY');
        $('#content').val('');
	}
}

function onCancel() {
    location.href = '/cms/structure.vm?tab=2&id=' + structureId;
}

function onUpdate(cont) {
	var structureTemplateVO = Vosao.javaMap({
	    id : structureTemplateId,
	    name : Vosao.strip($('#name').val()),
	    title : Vosao.strip($('#title').val()),
	    type: 'VELOCITY', //$('#type').val(),
	    structureId: String(structureId),
        content : $('#content').val()
	});
	Vosao.jsonrpc.structureTemplateService.save(function (r) {
		if (r.result == 'success') {
			Vosao.info(messages('structureTemplate.success_save'));
			if (!cont) {
				onCancel();
			}
			else if (!editMode) {
				structureTemplateId = r.message;
				loadStructureTemplate();
			}
		}
		else {
			Vosao.showServiceMessages(r);
		}			
	}, structureTemplateVO);
}

function onBig() {
	$('#content').attr('cols','120');
    $('#content').attr('rows','30');
}

function onSmall() {
    $('#content').attr('cols','80');
    $('#content').attr('rows','20');
}

