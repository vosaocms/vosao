// file StructureView.js
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

define(['text!template/structure.html',
        'order!cm', 'order!cm-css', 'order!cm-js', 'order!cm-xml', 'order!cm-html'], 
function(tmpl) {
	
	console.log('Loading StructureView.js');
	
	var structureId = '';

	var structure = '';
	var editMode = false;
	var autosaveTimer = '';
	var fields  = [];
	var templates = [];
	var editor = null;

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

	function autoSave() {
		return $("#autosave:checked").length > 0;
	}
	
	function onAutosave() {
	    if (autoSave()) {
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
		Vosao.jsonrpc.structureService.getById(function (r) {
			structure = r;
			initStructureForm();
		}, structureId);
	}

	function initStructureForm() {
		if (structure != null) {
			$('#title').val(structure.title);
	        $('#xmlContent').val(structure.content);
		}
		else {
	        $('#title').val('');
	        $('#xmlContent').val('');
		}
		editor = CodeMirror.fromTextArea(document.getElementById('xmlContent'), {
			lineNumbers: true,
			theme: 'eclipse',
			mode: 'xml'
		});
		editor.focus();
		$(editor.getScrollerElement())
			.css('height', (0.6 * $(window).height()) + 'px')
			.css('border', '1px solid silver');
		editor.refresh();
	}

	function onCancel() {
	    location.href = '#structures';
	}

	function onUpdate(cont) {
		editor.save();
		var vo = Vosao.javaMap({
		    id : structureId,
		    title : $('#title').val(),
	        content : $('#xmlContent').val()
		});
		Vosao.jsonrpc.structureService.save(function (r) {
			if (r.result == 'success') {
				Vosao.info(messages('structure.success_save'));
				if (!cont) {
					location.href = '#structures';
				}
				else if (!editMode) {
					structureId = r.message;
					loadStructure();
					loadTemplates();
				}
			}
			else {
				Vosao.showServiceMessages(r);
			}			
		}, vo);
	}

	function onFieldTitleChange() {
		var name = $("#fieldName").val();
		var title = $("#fieldTitle").val();
		if (name == '') {
			$("#fieldName").val(Vosao.nameFromTitle(title));
		}
	}

	function loadFields() {
		if (editMode) {
			Vosao.jsonrpc.structureService.getFields(function(r) {
				fields = r.list;
				showFields();
			}, structureId);
		}
	}

	function getFieldType(type) {
		if (type == 'TEXT') return messages('text');
		if (type == 'TEXTAREA') return messages('text_area');
		if (type == 'RESOURCE') return messages('resource_link');
		if (type == 'DATE') return messages('date');
		return 'Unknown';
	}

	function showFields() {
		var h = '<table class="form-table"><tr><th>' + messages('title') 
		    + '</th><th>' + messages('structure.tag_name') + '</th><th>'  
		    + messages('type') + '</th><th></th></tr>';
		$.each(fields, function(i, field) {
			h += '<tr><td>' + field.title + '</td>'
			    + '<td>' + field.name + '</td>'
			    + '<td>' + getFieldType(field.type) + '</td>'
			    + '<td><a href="#" onclick="Vosao.app.structureView.onFieldRemove(' + i + ')"><img src="/static/images/02_x.png"/></a> '
			    + '<a href="#" onclick="Vosao.app.structureView.onFieldUp(' + i + ')"><img src="/static/images/02_up.png"/></a> '
			    + '<a href="#" onclick="Vosao.app.structureView.onFieldDown(' + i + ')"><img src="/static/images/02_down.png"/></a> '
			    + '</td></tr>';		
		});
		$('#fields').html(h + '</table>');
	    $('#fields tr:even').addClass('even');
	}

	function validateField(field) {
		var valid = true;
		if (field.name == '') {
			Vosao.error(messages('structure.field_tag_name_empty'));
			valid = false;
		}
		else {
			$(fields, function(i,value) {
				if (value.name == field.name) {
					Vosao.error(messages('structure.field_exists'));
					valid = false;
				}
			});
		}
		if (!Vosao.isValidIdentifier(field.name)) {
			Vosao.error(messages('structure.field_tag_name') + ' ' + field.name 
				+ ' ' + messages('structure.must_valid_identifier'));
			valid = false;
		}
		if (field.title == '') {
			Vosao.error(messages('structure.field_title_empty'));
			valid = false;
		}
		return valid;
	}

	function onAddField() {
		$('#fieldTitle').focus();
		var field = {
				title: Vosao.strip($('#fieldTitle').val()),
				name: Vosao.strip($('#fieldName').val()),
				type: $('#fieldType').val()
		};
		if (validateField(field)) {
			fields.push(field);
			showFields();
			$('#fieldTitle').val('').focus();
			$('#fieldName').val('');
			Vosao.info(messages('structure.field_success_add'));
		}
	}

	function swapFields(i, j) {
		var f = fields[i];
		fields[i] = fields[j];
		fields[j] = f;
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
		editor.setValue(fieldsXML());
		onUpdate(cont);
	}

	function loadTemplates() {
		if (editMode) {
			Vosao.jsonrpc.structureTemplateService.selectByStructure(function(r) {
				templates = r.list;
				showTemplates();
			}, structureId);
		}
	}

	function showTemplates() {
		var h = '<table class="form-table"><tr><th></th>'
			+ '<th>' + messages('title') + '</th>'
			+ '<th>' + messages('name') + '</th>'
			+ '<th>' + messages('type') + '</th></tr>';
		$.each(templates, function(i, template) {
			h += '<tr><td><input type="checkbox" value="' + template.id + '"></td>'
				+ '<td><a href="#structureTemplate/' + template.id + '">'
			    + template.title + '</a></td>'
			    + '<td>' + template.name + '</td>'
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
		location.href = '#addStructureTemplate/' + structureId;
	}

	function onDeleteTemplate() {
	    var ids = new Array();
	    $('#templates input:checked').each(function () {
	        ids.push(this.value);
	    });
	    if (ids.length == 0) {
	    	Vosao.info(messages('nothing_selected'));
	        return;
	    }
	    if (confirm(messages('are_you_sure'))) {
	    	Vosao.jsonrpc.structureTemplateService.remove(function(r) {
	    		Vosao.showServiceMessages(r);
	            loadTemplates();
	        }, Vosao.javaList(ids));
	    }
	}
	
	function tabSelected(event, ui) {
		if (ui.index == 1) {
			if (autoSave()) {
				startAutosave();
			}
			if (editor) {
				editor.focus();
				
				$(editor.getScrollerElement())
					.css('height', (0.6 * $(window).height()) + 'px')
					.css('border', '1px solid silver');
					
				editor.refresh();
			}
		}
		else {
			stopAutosave();
		}
	}

	
	return Backbone.View.extend({
		
		css: ['/static/js/codemirror/codemirror.css',
		      '/static/js/codemirror/eclipse.css'],

		el: $('#content'),
		
		render: function() {
			Vosao.addCSSFiles(this.css);
			this.el.html(_.template(tmpl, {messages:messages}));
			loadData();
		    Vosao.selectTabFromQueryParam($("#tabs").tabs({show: tabSelected}));
		    $('#autosave').change(onAutosave);
		    $('#saveContinueXMLButton').click(onSaveContinueXML);
		    $('#saveXMLButton').click(onSaveXML);
		    $('#cancelXMLButton').click(onCancel);
		    $('#saveContinueButton').click(onSaveContinue);
		    $('#saveButton').click(onSave);
		    $('#cancelButton').click(onCancel);
		    $('#addField').click(onAddField);
		    $('#addTemplateButton').click(onAddTemplate);
		    $('#deleteTemplateButton').click(onDeleteTemplate);
		    $('#fieldTitle').change(onFieldTitleChange);
		},
		
		setId: function(id) {
			structureId = id;
			editMode = structureId != '';
			if (!editMode) {
				structure = '';
				fields  = [];
				templates = [];
			}
		},
		
		remove: function() {
			this.el.html('');
			Vosao.removeCSSFiles(this.css);
		},
		
		onFieldUp: function(i) {
			if (i == 0) return;
			swapFields(i, i - 1);
			showFields();
		},

		onFieldDown: function(i) {
			if (i + 1 >= fields.length) return;
			swapFields(i, i + 1);
			showFields();
		},
		
		onFieldRemove: function(i) {
		    if (confirm(messages('are_you_sure'))) {
		    	fields.splice(i, 1);
		    	showFields();
		    }
		}
		
	});
	
});