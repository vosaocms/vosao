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

var file = '';
var editMode = fileId != '';
var autosaveTimer = '';

$(function() {
	$("#tabs").tabs();
	$("#tabs").bind('tabsselect', tabSelected);
	initJSONRpc(loadFile);
	$('#saveButton').click(onUpdate);
	$('#cancelButton').click(onCancel);
	$('#autosave').change(onAutosave);
	$('#saveContentButton').click(saveContent);
	$('#contentCancelButton').click(onCancel);
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
	$('#').click();
});

function tabSelected(event, ui) {
	if (ui.index == 1) {
		startAutosave();
	} else {
		stopAutosave();
	}
}

function startAutosave() {
	if (fileId != 'null') {
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
	var content = $("textarea").val();
	fileService.updateContent(function(r) {
		if (r.result == 'success') {
			var now = new Date();
			info(r.message + " " + now);
		} else {
			error(r.message);
		}
	}, fileId, content);
}

function onAutosave() {
	if ($("#autosave:checked").length > 0) {
		startAutosave();
	} else {
		stopAutosave();
	}
}

function loadFile() {
	fileService.getFile(function(r) {
		file = r;
		if (editMode) {
			folderId = file.folderId;
		}
		initFormFields();
	}, fileId);
}

function initFormFields() {
	if (editMode) {
		$('#title').val(file.title);
		$('#name').val(file.name);
		$('#fileEditDiv').show();
		$('#mimeType').html(file.mimeType);
		$('#size').html(file.size);
		$('#fileLink').html(file.link);
		$('#download').html('<a href="' + file.link + '">Download</a>');
		if (file.textFile) {
			$('.contentTab').show();
			$('#content').val(file.content);
		} else {
			$('.contentTab').hide();
		}
		if (file.imageFile) {
			$('#imageContent').html('<img src="' + file.link + '" />');
		} else {
			$('#imageContent').html('');
		}
	} else {
		$('#title').val('');
		$('#name').val('');
		$('#fileEditDiv').hide();
		$('.contentTab').hide();
		$('#imageContent').html('');
	}
}

function onUpdate() {
	var vo = javaMap( {
		id : fileId,
		folderId : folderId,
		title : $('#title').val(),
		name : $('#name').val()
	});
	fileService.saveFile(function(r) {
		if (r.result == 'success') {
			location.href = '/cms/folder.jsp?id=' + folderId;
		} else {
			showServiceMessages(r);
		}
	}, vo);
}

function onCancel() {
	location.href = '/cms/folder.jsp?id=' + folderId;
}
