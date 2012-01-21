// file StructureTemplateView.js
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

define(['text!template/structureTemplate.html',
        'order!cm', 'order!cm-css', 'order!cm-js', 'order!cm-xml', 'order!cm-html'], 
function(tmpl) {
	
	console.log('Loading StructureTemplateView.js');
	
	var structureTemplateId = ''; 
	var structureId = ''; 

	var structureTemplate = '',
	    editMode = structureTemplateId != '',
	    autosaveTimer = '',
	    editor = null, 
	    headEditor = null;
	    
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
		else {
			Vosao.jsonrpc.structureTemplateService.getById(function (r) {
				structureTemplate = r;
				if (editMode) {
					structureId = structureTemplate.structureId;
				}
				initStructureTemplateForm();
			}, structureTemplateId);
		}
	}

	function initStructureTemplateForm() {
		if (structureTemplate != null) {
			$('#name').val(structureTemplate.name);
			$('#title').val(structureTemplate.title);
	        $('#vcontent').val(structureTemplate.content);
	        $('#headContent').val(structureTemplate.headContent);
		}
		else {
	        $('#name').val('');
	        $('#title').val('');
	        $('#vcontent').val('');
	        $('#headContent').val('');
		}
		if (!editor) {
			editor = createEditor('vcontent');
		} else {
			editor.refresh();
		}
		if (!headEditor) {
			headEditor = createEditor('headContent');
		} else {
			headEditor.refresh();
		}
	}

	function createEditor(id) {
		var e = CodeMirror.fromTextArea(document.getElementById(id), {
			lineNumbers: true,
			theme: 'eclipse',
			mode: 'htmlmixed'
		});
		$(e.getScrollerElement())
			.css('height', (0.6 * $(window).height()) + 'px')
			.css('border', '1px solid silver');
		return e;
	}
	
	function onCancel() {
	    location.href = '#structure/' + structureId;
	}

	function onUpdate(cont) {
		if (editor) {
			editor.save();
		}
		if (headEditor) {
			headEditor.save();
		}
		var structureTemplateVO = Vosao.javaMap({
		    id : structureTemplateId,
		    name : Vosao.strip($('#name').val()),
		    title : Vosao.strip($('#title').val()),
		    type: 'VELOCITY', 
		    structureId: String(structureId),
	        content : $('#vcontent').val(),
	        headContent : $('#headContent').val()
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

	
	return Backbone.View.extend({
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css'],
		
		el: $('#content'),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			editor = null; 
		    headEditor = null;
			this.el.html(_.template(tmpl, {messages:messages}));
			loadStructureTemplate();
		    $("#tabs").tabs();
		    $('#autosave').change(onAutosave);
		    $('#saveContinueButton').click(onSaveContinue);
		    $('#saveButton').click(onSave);
		    $('#cancelButton').click(onCancel);
		    $('#title').change(onTitleChange);
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		},
		
		create: function(id) {
			structureTemplateId = '';
			structureId = id;
			structureTemplate = '';
			editMode = structureTemplateId != '';
		},
		
		edit: function(id) {
			structureTemplateId = id;
			structureId = '';
			structureTemplate = '';
			editMode = structureTemplateId != '';
		}
		
	});
	
});