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

define(['text!template/file.html', 'order!cm', 'order!cm-css',
        'order!cm-js', 'order!cm-xml', 'order!cm-html'], 
function(html) {
	
	console.log('Loading FileView.js');
	
	var fileId = null;
	var folderId = null;
	
	var file = '';
	var editMode = fileId != '';
	var autosaveTimer = '';
	var editor = null;

	function postRender() {
		editMode = fileId != '';
		$("#tabs").tabs({show: tabSelected});
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
			if (editor) {
				editor.focus();
				
				$(editor.getScrollerElement())
					.css('height', (0.6 * $(window).height()) + 'px')
					.css('border', '1px solid silver');
					
				editor.refresh();
			}
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
		if (editor) editor.save();
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

	function loadData() {
		Vosao.jsonrpc.fileService.getFile(function(r) {
			file = r;
			if (editMode) {
				folderId = file.folderId;
			}
			initFormFields();
		}, fileId);
	}

	function openCM(mimetype) {
		var mode = 'html';
		if (file.mimeType == 'text/css') {
			mode = 'css';
		}
		if (file.mimeType == 'text/xml') {
			mode = 'xml';
		}
		if (file.mimeType == 'text/html') {
			mode = 'htmlmixed';
		}
		if (file.mimeType.indexOf('javascript') != -1) {
			mode = 'javascript';
		}
		editor = CodeMirror.fromTextArea(document.getElementById('fileContent'), {
			lineNumbers: true,
			theme: 'eclipse',
			mode: mode
		});
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
				$('#fileContent').val(file.content);
				openCM(file.mimetype);
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
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css',
		      '/static/css/file.css'],
		
		el: $('#content'),
		
		tmpl: _.template(html),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		},
		
		setFileId: function(id) {
			fileId = id;
		},
		
		setFolderId: function(id) {
			folderId = id;
		}
		
	});
	
});