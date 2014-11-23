// file TemplateView.js
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

define(['text!template/template.html', 'jquery.treeview',
        'order!cm', 'order!cm-css', 'order!cm-js', 'order!cm-xml', 'order!cm-html'], 
function(tmpl) {
	
	console.log('Loading TemplateView.js');
	
	var templateId = '';

	var template = '';
	var editMode = templateId != '';
	var autosaveTimer = '';
	var editor = null;
	    
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
		if (editor) editor.save();
	    var content = $("#tcontent").val();
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
	        $('#tcontent').val(template.content);
		}
		else {
	        $('#title').val('');
	        $('#url').val('');
	        $('#tcontent').val('');
		}
		editor = CodeMirror.fromTextArea(document.getElementById('tcontent'), {
			lineNumbers: true,
			theme: 'eclipse',
			mode: 'htmlmixed'
		});
		editor.focus();
		$(editor.getScrollerElement())
			.css('height', (0.6 * $(window).height()) + 'px')
			.css('border', '1px solid silver');
		editor.refresh();
	}

	function onCancel() {
	    location.href = '#templates';
	}

	function onUpdate(cont) {
		if (editor) editor.save();
		var templateVO = Vosao.javaMap({
		    id : templateId,
		    title : $('#title').val(),
	        url : $('#url').val(),
	        content : $('#tcontent').val()
		});
		Vosao.jsonrpc.templateService.saveTemplate(function (r) {
			if (r.result == 'success') {
				Vosao.info(messages('template.success_save'));
				if (!cont) {
					location.href = '#templates';
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

	// Resources

	function onResources() {
		$.cookie('folderReturnPath', '#template/' + template.id, 
				{path:'/', expires: 10});
		Vosao.jsonrpc.folderService.createFolderByPath(function(r) {
			location.href = '#folder/' + r.id;
	    }, '/theme/' + template.url);
	}


	return Backbone.View.extend({
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css',
		      '/static/css/jquery.treeview.css'],
		
		el: $('#content'),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(_.template(tmpl, {messages:messages}));
			loadData();
		    $('#autosave').change(onAutosave);
		    $('#saveContinueButton').click(onSaveContinue);
		    $('#templateForm').submit(function() {onSave(); return false;});
		    $('#cancelButton').click(onCancel);
		    $('#resources').click(onResources);
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		},
		
		create: function() {
			templateId = '';
			template = '';
			editMode = templateId != '';
		},
		
		edit: function(id) {
			templateId = id;
			template = '';
			editMode = templateId != '';
		}
		
	});
	
});