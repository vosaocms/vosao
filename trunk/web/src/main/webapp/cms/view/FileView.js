// file FileView.js
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

define(['text!template/file.html'], 
function(html) {
	
	console.log('Loading FileView.js');
	
	var fileId = null;
	var folderId = null;
	
	var file = '';
	var editMode = fileId != '';
	var autosaveTimer = '';
	var editor = true;
	var aceEditor = false;

	function postRender() {
		editMode = fileId != '';
		$("#tabs").tabs();
		$("#tabs").bind('tabsselect', tabSelected);
		Vosao.initJSONRpc(loadData);
		$('#fileForm').submit(function() {onUpdate(); return false});
		$('#cancelButton').click(onCancel);
		$('#autosave').change(onAutosave);
		$('#contentForm').submit(function() {saveContent(); return false;});
		$('#contentCancelButton').click(onCancel);
	}

	function tabSelected(event, ui) {
		if (ui.index == 1) {
			startAutosave();
		} else {
			stopAutosave();
		}
	}

	function startAutosave() {
		if (fileId != 'null' && $("#autosave:checked").length > 0) {
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
		var content = aceEditor ? $("#ace").text() : $("textarea").val();
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

	function loadData() {
		Vosao.jsonrpc.fileService.getFile(function(r) {
			file = r;
			if (editMode) {
				folderId = file.folderId;
			}
			initFormFields();
		}, fileId);
	}

	function openAce(mode, content) {
		$('#ace').text(content);
		aceEditor = true;
		editor = ace.edit("ace");
	    editor.setTheme("ace/theme/eclipse");
	    var Mode = require("ace/mode/" + mode).Mode;
	    editor.getSession().setMode(new Mode());
	    $('#content').hide();
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
				/*if (file.mimeType == 'text/css') {
					openAce('css', file.content);
				}
				if (file.mimeType == 'text/xml') {
					openAce('xml', file.content);
				}
				if (file.mimeType == 'text/html') {
					openAce('html', file.content);
				}
				if (file.mimeType == 'text/javascript') {
					openAce('javascript', file.content);
				}
				else {
					$('#content').val(file.content);
				}*/
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
			folderId : String(folderId),
			title : $('#title').val(),
			name : $('#name').val()
		});
		Vosao.jsonrpc.fileService.saveFile(function(r) {
			Vosao.showServiceMessages(r);
			if (r.result == 'success') {
				location.href = '#file/' + r.data;
			}
		}, vo);
	}

	function onCancel() {
		location.href = '#folder/' + folderId;
	}

	
	return Backbone.View.extend({
		
		css: '/static/css/file.css',
		
		el: $('#content'),
		
		tmpl: _.template(html),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		},
		
		setFileId: function(id) {
			fileId = id;
		},
		
		setFolderId: function(id) {
			folderId = id;
		}
		
	});
	
});