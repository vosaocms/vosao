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

define(['text!template/structureTemplate.html'], function(tmpl) {
	
	console.log('Loading StructureTemplateView.js');
	
	var structureTemplateId = ''; 
	var structureId = ''; 

	var structureTemplate = '';
	var editMode = structureTemplateId != '';
	var autosaveTimer = '';
	    
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
	        $('#vcontent').val(structureTemplate.content);
	        $('#headContent').val(structureTemplate.headContent);
		}
		else {
	        $('#name').val('');
	        $('#title').val('');
	        $('#vcontent').val('');
	        $('#headContent').val('');
		}
	}

	function onCancel() {
	    location.href = '#structure/' + structureId;
	}

	function onUpdate(cont) {
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

	function onBig() {
		$('#vcontent').attr('cols','120');
	    $('#vcontent').attr('rows','30');
	}

	function onSmall() {
	    $('#vcontent').attr('cols','80');
	    $('#vcontent').attr('rows','20');
	}

	
	return Backbone.View.extend({
		
		css: '/static/css/structureTemplate.css',
		
		el: $('#content'),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el.html(_.template(tmpl, {messages:messages}));
			
			loadStructureTemplate();
		    $("#tabs").tabs();
		    $('#autosave').change(onAutosave);
		    $('#bigLink').click(onBig);
		    $('#smallLink').click(onSmall);
		    $('#saveContinueButton').click(onSaveContinue);
		    $('#saveButton').click(onSave);
		    $('#cancelButton').click(onCancel);
		    $('#title').change(onTitleChange);
		},
		
		remove: function() {
			Vosao.removeCSSFile(this.css);
			this.el.html('');
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