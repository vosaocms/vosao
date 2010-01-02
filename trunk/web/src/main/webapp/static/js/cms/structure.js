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

var structure = '';
var editMode = structureId != '';
var autosaveTimer = '';
var fields  = [];
var templates = [];

$(function(){
    initJSONRpc(loadData);
    $("#tabs").tabs();
    $('#autosave').change(onAutosave);
    $('#bigLink').click(onBig);
    $('#smallLink').click(onSmall);
    $('#saveContinueXMLButton').click(onSaveContinueXML);
    $('#saveXMLButton').click(onSaveXML);
    $('#cancelXMLButton').click(onCancel);
    $('#saveContinueButton').click(onSaveContinue);
    $('#saveButton').click(onSave);
    $('#cancelButton').click(onCancel);
    $('#addField').click(onAddField);
    $('#addTemplateButton').click(onAddTemplate);
    $('#deleteTemplateButton').click(onDeleteTemplate);
});

function loadData() {
	loadStructure();
	loadFields();
	loadTemplates();
}

function onSaveContinueXML() {
	onUpdate(true);
}

function onSaveXML() {
	onUpdate(false);
}
    
function startAutosave() {
    if (structureId != 'null') {
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

function loadStructure() {
	editMode = structureId != '';
	if (!editMode) {
		structure = null;
        initStructureForm();
	}
	jsonrpc.structureService.getById(function (r) {
		structure = r;
		initStructureForm();
	}, structureId);
}

function initStructureForm() {
	if (structure != null) {
		$('#title').val(structure.title);
        $('#content').val(structure.content);
	}
	else {
        $('#title').val('');
        $('#content').val('');
	}
}

function onCancel() {
    location.href = '/cms/structures.jsp';
}

function onUpdate(cont) {
	var vo = javaMap({
	    id : structureId,
	    title : $('#title').val(),
        content : $('#content').val(),
	});
	jsonrpc.structureService.save(function (r) {
		if (r.result == 'success') {
			info('Structure was successfully saved.');
			if (!cont) {
				location.href = '/cms/structures.jsp';
			}
			else if (!editMode) {
				structureId = r.message;
				loadStructure();
			}
		}
		else {
			showServiceMessages(r);
		}			
	}, vo);
}

function onBig() {
	$('#content').attr('cols','120');
    $('#content').attr('rows','30');
}

function onSmall() {
    $('#content').attr('cols','80');
    $('#content').attr('rows','20');
}

function loadFields() {
	if (editMode) {
		jsonrpc.structureService.getFields(function(r) {
			fields = r.list;
			showFields();
		}, structureId);
	
	}
}

function getFieldType(type) {
	if (type == 'TEXT') return 'Text';
	if (type == 'TEXTAREA') return 'Text area';
	if (type == 'RESOURCE') return 'Resource link';
	if (type == 'DATE') return 'Date';
	return 'Unknown';
}

function showFields() {
	var h = '<table class="form-table"><tr><th>Title</th><th>Tag name</th><th>Type</th><th></th></tr>';
	$.each(fields, function(i, field) {
		h += '<tr><td>' + field.title + '</td>'
		    + '<td>' + field.name + '</td>'
		    + '<td>' + getFieldType(field.type) + '</td>'
		    + '<td><a href="#" onclick="onFieldRemove(' + i + ')">remove</a> '
		    + '<a href="#" onclick="onFieldUp(' + i + ')">Up</a> '
		    + '<a href="#" onclick="onFieldDown(' + i + ')">Down</a> '
		    + '</td></tr>';		
	});
	$('#fields').html(h + '</table>');
    $('#fields tr:even').addClass('even');
}

function onFieldRemove(i) {
    if (confirm('Are you sure?')) {
    	fields.splice(i, 1);
    	showFields();
    }
}

function validateField(field) {
	var valid = true;
	if (field.name == '') {
		error('Field tag name is empty');
		valid = false;
	}
	else {
		$(fields, function(i,value) {
			if (value.name == field.name) {
				error('Field with such a tag name already exists');
				valid = false;
			}
		});
	}
	if (!isValidIdentifier(field.name)) {
		error('Field tag name ' + field.name + ' must be valid identifier');
		valid = false;
	}
	if (field.title == '') {
		error('Field title is empty');
		valid = false;
	}
	return valid;
}

function onAddField() {
	$('#fieldTitle').focus();
	var field = {
			title: strip($('#fieldTitle').val()),
			name: strip($('#fieldName').val()),
			type: $('#fieldType').val()
	};
	if (validateField(field)) {
		fields.push(field);
		showFields();
		$('#fieldTitle').val('').focus();
		$('#fieldName').val('');
		info('Field was successfully added.');
	}
}

function swapFields(i, j) {
	var f = fields[i];
	fields[i] = fields[j];
	fields[j] = f;
}

function onFieldUp(i) {
	if (i == 0) return;
	swapFields(i, i - 1);
	showFields();
}

function onFieldDown(i) {
	if (i + 1 >= fields.length) return;
	swapFields(i, i + 1);
	showFields();
}

function fieldsXML() {
	var xml = '<structure>\n';
	$.each(fields, function(i, field) {
		xml += '  <field>\n'
		    + '    <title>' + field.title + '</title>\n'
			+ '    <name>' + field.name + '</name>\n'
		    + '    <type>' + field.type + '</type>\n  </field>\n';
	});
	return xml + '</structure>';
}

function onSave() {
	saveFields(false);
}

function onSaveContinue() {
	saveFields(true);
}

function saveFields(cont) {
	$('#content').val(fieldsXML());
	onUpdate(cont);
}

function loadTemplates() {
	if (editMode) {
		jsonrpc.structureTemplateService.selectByStructure(function(r) {
			templates = r.list;
			showTemplates();
		}, structureId);
	}
}

function showTemplates() {
	var h = '<table class="form-table"><tr><th></th><th>Title</th><th><th>Type</th></tr>';
	$.each(templates, function(i, template) {
		h += '<tr><td><input type="checkbox" value="' + template.id + '"></td>'
		    + '<td><a href="structureTemplate.jsp?id=' + template.id + '">' 
		    + template.title + '</a></td>'
		    + '<td>' + getTemplateType(template.typeString) + '</td></tr>';
	});
	$('#templates').html(h + '</table>');
    $('#templates tr:even').addClass('even');	
}

function getTemplateType(type) {
	if (type == 'VELOCITY') return 'Velocity';
	if (type == 'XSLT') return 'XSLT';
	return 'Undefined';
}

function onAddTemplate() {
	location.href = 'structureTemplate.jsp?structureId=' + structureId;
}

function onDeleteTemplate() {
	alert('TODO');
}