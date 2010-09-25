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

var fileId = Vosao.getQueryParam('id');
var folderId = Vosao.getQueryParam('folderId');
var file = '';
var editMode = fileId != '';
var autosaveTimer = '';

$(function() {
	$("#tabs").tabs();
	$("#tabs").bind('tabsselect', tabSelected);
	Vosao.initJSONRpc(loadFile);
	$('#fileForm').submit(function() {onUpdate(); return false});
	$('#cancelButton').click(onCancel);
	$('#autosave').change(onAutosave);
	$('#contentForm').submit(function() {saveContent(); return false;});
	$('#contentCancelButton').click(onCancel);
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
	var content = $("textarea").val();
	Vosao.jsonrpc.fileService.updateContent(function(r) {
		if (r.result == 'success') {
			var now = new Date();
			Vosao.info(r.message + " " + now);
		} else {
			Vosao.error(r.message);
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
	Vosao.jsonrpc.fileService.getFile(function(r) {
		file = r;
		if (editMode) {
			folderId = file.folderId;
		}
		initFormFields();
	}, fileId);
}

function initFormFields() {
	if (editMode) {
		$('#filename').html(file.name);
		$('#title').val(file.title);
		$('#name').val(file.name);
		$('#fileEditDiv').show();
		$('#mimeType').html(file.mimeType);
		$('#size').html(file.size);
		$('#fileLink').html(file.link);
		$('#download').html('<a href="' + file.link + '">' + messages('download') + '</a>');
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
		$('#filename').html('');
		$('#title').val('');
		$('#name').val('');
		$('#fileEditDiv').hide();
		$('.contentTab').hide();
		$('#imageContent').html('');
	}
}

function onUpdate() {
	var vo = Vosao.javaMap( {
		id : fileId,
		folderId : folderId,
		title : $('#title').val(),
		name : $('#name').val()
	});
	Vosao.jsonrpc.fileService.saveFile(function(r) {
		if (r.result == 'success') {
			location.href = '/cms/folder.vm?id=' + folderId;
		} else {
			Vosao.showServiceMessages(r);
		}
	}, vo);
}

function onCancel() {
	location.href = '/cms/folder.vm?id=' + folderId;
}
