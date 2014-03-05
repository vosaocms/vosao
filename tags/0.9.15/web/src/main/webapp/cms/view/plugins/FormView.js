// file FormView.js
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

define(['text!template/plugins/form.html'], function(html) {
	
	console.log('Loading FormView.js');
	
	var formId = '';
	var form = null;
	var languages = {};
	var editMode = formId != '';
	var field = null;
	var fields = null;
	var test = null;
	var regexMessages = {};
	var formData = null;
	var formDataIndex = 0;

	function postRender() {
		editMode = formId != '';
		$("#tabs").tabs();
		$("#field-dialog").dialog({ width :500, autoOpen :false });
		$("#formData-dialog").dialog({ width :500, autoOpen :false });
		Vosao.initJSONRpc(loadData);
		$('#title').change(onTitleChange);
		$('#language').change(onLanguageChange);
		$('input[name="field.regexMessage"]').change(onRegexMessageChange);
		$('#form').submit(function() {onUpdate(); return false;});
		$('#cancelButton').click(onCancel);
		$('#addFieldButton').click(onAddField);
		$('#deleteFieldButton').click(onDeleteFields);
		$('#fieldType').change(onFieldTypeChange);
		$('#fieldForm').submit(function() {onSaveAndAdd(); return false;});
		$('#fieldSaveButton').click(function() { onFieldSave(true); });
		$('#fieldCancelButton').click(onFieldCancel);
		$('input[name="field.title"]').change(onFieldTitleChange);
		$('#deleteDataButton').click(onDeleteData);
		$('#formDataCancelButton').click(function() { 
			$('#formData-dialog').dialog('close');
		});
		$('#formDataSendButton').click(onFormDataSend);
	}

	function loadData() {
		loadForm();
		loadFields();
		loadLanguages();
	}

	function loadLanguages() {
		Vosao.jsonrpc.languageService.select(function(r) {
			var h = '';
			$.each(r.list, function(i, value) {
				languages[value.code] = value;
				h += '<option value="' + value.code + '">' + value.title + '</option>';
			});
			$('#language').html(h);
		});
	}

	function loadFields() {
		if (formId == '') {
			return;
		}
		Vosao.jsonrpc.fieldService.getByForm(function(r, e) {
			fields = r.list;
			showFields();
		}, formId);
	}

	function showFields() {
		var h = '<table class="form-table"><tr><th></th><th>' + messages('title') 
			+ '</th><th>' + messages('name') + '</th><th>' + messages('type') 
			+ '</th><th></th></tr>';
		$.each(fields, function(i, field) {
			h += 
				'<tr>\
				<td><input type="checkbox" name="item' + i + '" value="' + field.id + '"/></td>\
				<td><a class="fieldEdit" data-id="' + field.id + '">'   + field.title + '</a></td>\
				<td>' + field.name + '</td>\
				<td>'   + fieldTypeString(field.fieldType) + '</td>\
				<td><a class="fieldUp" data-index="' + i+ '"><img src="/static/images/02_up.png"/></a>\
				    <a class="fieldDown" data-index="' + i + '"><img src="/static/images/02_down.png"/></a>\
				</td>\
				</tr>';
		});
		$('#fieldsTable').html(h + '</table>');
		$('#fieldsTable tr:even').addClass('even');
		$('#fieldsTable .fieldEdit').click(function() {
			onFieldEdit($(this).attr('data-id'));
		});
		$('#fieldsTable .fieldUp').click(function() {
			onFieldUp($(this).attr('data-index'));
		});
		$('#fieldsTable .fieldDown').click(function() {
			onFieldDown($(this).attr('data-index'));
		});
	}

	function fieldTypeString(v) {
		if (v == 'TEXT') return 'Text';
		if (v == 'CHECKBOX') return 'Checkbox';
		if (v == 'RADIO') return 'Radiobox';
		if (v == 'PASSWORD') return 'Password';
		if (v == 'LISTBOX') return 'Listbox';
		if (v == 'FILE') return 'File upload';
		return 'undefined';
	}

	function onAddField() {
		field = null;
		fieldDialogInit();
		$("#field-dialog").dialog("open");
	}

	function onFieldCancel() {
		$("#field-dialog").dialog("close");
	}

	function onFieldSave(closeFlag) {
		var fieldVO = createFieldVO();
		var errors = validateField(fieldVO);
		if (errors.length == 0) {
			Vosao.jsonrpc.fieldService.updateField( function(r, e) {
				if (r.result == 'success') {
					if (closeFlag) {
						$("#field-dialog").dialog("close");
					}
					loadFields();
				} else {
					fieldErrorMessages(r.messages.list);
				}
			}, fieldVO);
		} else {
			fieldErrorMessages(errors);
		}
	}

	function onFieldTypeChange() {
		fieldDialogShowInputs();
	}

	function fieldDialogShowInputs() {
		var fieldType = $('select[name="field.fieldType"]').val();
		if (fieldType == 'TEXT') {
			$('#field-values').hide();
			$('#field-height').show();
			$('#field-width').show();
			$('#field-defaultValue').show();
			$('#regexDiv').show();
		}
		if (fieldType == 'LISTBOX') {
			$('#field-values').show();
			$('#field-height').hide();
			$('#field-width').hide();
			$('#field-defaultValue').hide();
			$('#regexDiv').hide();
		}
		if (fieldType == 'CHECKBOX') {
			$('#field-values').show();
			$('#field-height').hide();
			$('#field-width').hide();
			$('#field-defaultValue').hide();
			$('#regexDiv').hide();
		}
		if (fieldType == 'RADIO') {
			$('#field-values').show();
			$('#field-height').hide();
			$('#field-width').hide();
			$('#field-defaultValue').hide();
			$('#regexDiv').hide();
		}
		if (fieldType == 'PASSWORD') {
			$('#field-values').hide();
			$('#field-height').hide();
			$('#field-width').show();
			$('#field-defaultValue').hide();
			$('#regexDiv').hide();
		}
		if (fieldType == 'FILE') {
			$('#field-values').hide();
			$('#field-height').hide();
			$('#field-width').hide();
			$('#field-defaultValue').hide();
			$('#regexDiv').hide();
		}
	}

	function fieldDialogInit() {
		if (field == null) {
			regexMessages = {};
			$('input[name="field.name"]').val('');
			$('input[name="field.title"]').val('');
			$('select[name="field.fieldType"]').val('TEXT');
			$('textarea[name="field.values"]').val('');
			$('input[name="field.defaultValue"]').val('');
			$('input[name="field.height"]').val('1');
			$('input[name="field.width"]').val('20');
			$('input[name="field.mandatory"]')[0].checked = false;
			$('input[name="field.regex"]').val('');
			$('input[name="field.regexMessage"]').val('');
		} else {
			$('input[name="field.name"]').val(field.name);
			$('input[name="field.title"]').val(field.title);
			$('select[name="field.fieldType"]').val(field.fieldType);
			$('textarea[name="field.values"]').val(field.values);
			$('input[name="field.defaultValue"]').val(field.defaultValue);
			$('input[name="field.height"]').val(field.height);
			$('input[name="field.width"]').val(field.width);
			$('input[name="field.mandatory"]')[0].checked = field.optional;
			$('input[name="field.regex"]').val(field.regex);
			$('input[name="field.regexMessage"]').val(getRegexMessage());
		}
		fieldDialogShowInputs();
		clearFieldMessage();
	}

	function createFieldVO() {
		var fieldIndex = field != null ? field.index : 
			(fields == null ? 0 : fields.length);
		return Vosao.javaMap( {
			id :field != null ? String(field.id) : null,
			formId :formId,
			name :$('input[name="field.name"]').val(),
			title :$('input[name="field.title"]').val(),
			fieldType :$('select[name="field.fieldType"]').val(),
			values :$('textarea[name="field.values"]').val(),
			defaultValue :$('input[name="field.defaultValue"]').val(),
			height :$('input[name="field.height"]').val(),
			width :$('input[name="field.width"]').val(),
			index : String(fieldIndex),
			regex : $('input[name="field.regex"]').val(),
			regexMessage : saveRegexMessage(),
			mandatory :String($('input[name="field.mandatory"]:checked').size() > 0)
		});
	}

	function validateField(fieldVO) {
		var errors = new Array();
		if (fieldVO.map.name == '') {
			errors.push(messages('name_is_empty'));
		}
		if (fieldVO.map.title == '') {
			errors.push(messages('title_is_empty'));
		}
		var height = Number(fieldVO.map.height);
		if (fieldVO.map.fieldType == 'TEXT' && height <= 0) {
			errors.push(messages('form.height_zero_error'));
		}
		var width = Number(fieldVO.map.width);
		if (fieldVO.map.fieldType == 'TEXT' && width <= 0) {
			errors.push(messages('form.width_zero_error'));
		}
		return errors;
	}

	function fieldInfoMessage(message) {
		Vosao.infoMessage('#field-messages', message);
	}

	function fieldErrorMessages(messages) {
		Vosao.errorMessages('#field-messages', messages);
	}

	function fieldErrorMessage(message) {
		Vosao.errorMessage('#field-messages', message);
	}

	function clearFieldMessage() {
		$('#field-messages').html('');
	}

	function onFieldEdit(fieldId) {
		clearFieldMessage();
		Vosao.jsonrpc.fieldService.getById( function(r) {
			field = r;
			loadRegexMessage();
			fieldDialogInit();
			$("#field-dialog").dialog("open");
		}, fieldId);
	}

	function onDeleteFields() {
		var ids = new Array();
		$('input:checked').each(function() {
			ids.push(this.value);
		});
		if (ids.length == 0) {
			Vosao.info(messages('nothing_selected'));
			return;
		}
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.fieldService.remove(function(r) {
				if (r.result == 'success') {
				    Vosao.info(ids.length + ' ' + messages('form.success_field_delete') 
				        + '.');
				    loadFields();
				}
				else {
					Vosao.showServiceMessages(r);
				}
			}, Vosao.javaList(ids));
		}
	}

	function onSaveAndAdd() {
		onFieldSave(false);
		onAddField();
	}

	function loadForm() {
		Vosao.jsonrpc.formService.getForm(function (r) {
			form = r
			loadFormData();
			if (r != null) {
				$('#title').val(r.title);
				$('#name').val(r.name);
				$('#email').val(r.email);
				$('#letterSubject').val(r.letterSubject);
				$('#sendButtonTitle').val(r.sendButtonTitle);
				$('#resetButtonTitle').val(r.resetButtonTitle);
				$('#showResetButton').each(function() {
					this.checked = r.showResetButton;
				});
				$('#enableCaptcha').each(function() {
					this.checked = r.enableCaptcha;
				});
				$('#enableSave').each(function() {
					this.checked = r.enableSave;
				});
				$('.fieldsTab').show();
			}
			else {
				$('#title').val('');
				$('#name').val('');
				$('#email').val('');
				$('#letterSubject').val('');
				$('#sendButtonTitle').val('');
				$('#resetButtonTitle').val('');
				$('#showResetButton').each(function() {
					this.checked = false;
				});
				$('#enableCaptcha').each(function() {
					this.checked = false;
				});
				$('#enableSave').each(function() {
					this.checked = false;
				});
				$('.fieldsTab').hide();
			}
		}, formId);
	}

	function onUpdate() {
		var vo = Vosao.javaMap({
			id : formId,
			title : $('#title').val(),
			name : $('#name').val(),
			email : $('#email').val(),
			letterSubject : $('#letterSubject').val(),
			sendButtonTitle : $('#sendButtonTitle').val(),
			resetButtonTitle : $('#resetButtonTitle').val(),
			showResetButton : String($('#showResetButton:checked').size() > 0),
			enableCaptcha : String($('#enableCaptcha:checked').size() > 0),
			enableSave : String($('#enableSave:checked').size() > 0)
		});
		Vosao.jsonrpc.formService.saveForm(function (r) {
			if (r.result == 'success') {
				if (!editMode) {
					formId = r.message;
					editMode = true;
					loadData();
					Vosao.info(messages('form.success_create'));
				}
				else {
					location.href = '#plugins/forms';
				}
			}
			else {
				Vosao.showServiceMessages(r);
			}
		}, vo);
	}

	function onCancel() {
		location.href = '#plugins/forms';
	}

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

	function onFieldTitleChange() {
		if (field != null) {
			return;
		}
		var name = $('input[name="field.name"]').val();
		var title = $('input[name="field.title"]').val();
		if (name == '') {
			$('input[name="field.name"]').val(Vosao.urlFromTitle(title));
		}
	}

	function onFieldUp(i) {
		if (i - 1 >= 0) {
			Vosao.jsonrpc.fieldService.moveUp(function(r) {}, formId, fields[i].id);
	        fields[i].index--;
	        fields[i - 1].index++;
			swapFields(i, i - 1);
			showFields();
		}
	}

	function onFieldDown(i) {
		if (i + 1 < fields.length) {
			Vosao.jsonrpc.fieldService.moveDown(function(r) {}, formId, fields[i].id);
	        fields[i + 1].index--;
	        fields[i].index++;
			swapFields(i, i + 1);
			showFields();
		}
	}

	function swapFields(i, j) {
		var tmp = fields[j];
		fields[j] = fields[i];
		fields[i] = tmp;
	}

	function getRegexMessage() {
		if (regexMessages[$('#language').val()]) {
			return regexMessages[$('#language').val()];
		}
		return '';
	}

	function onRegexMessageChange() {
		regexMessages[$('#language').val()] = $('input[name="field.regexMessage"]').val();
	}

	function onLanguageChange() {
		$('input[name="field.regexMessage"]').val(regexMessages[$('#language').val()]);
	}

	function saveRegexMessage() {
		var r = '';
		var i = 0;
		$.each(regexMessages, function(code, value) {
			r += (i++ == 0 ? '' : '::') + code + value;
		});
		return r;
	}

	function loadRegexMessage() {
		if (field.regexMessage) {
			$.each(field.regexMessage.split('::'), function(i, value) {
				var code = value.substr(0,2);
				var message = value.substr(2);
				regexMessages[code] = message;
			});
		}
		else {
			regexMessages = {};
		}
	}

	function loadFormData() {
		if (form == null) {
			formData = [];
			showFormData();
		}
		else {
			Vosao.jsonrpc.formService.getFormData(function(r) {
				formData = r.list;
				showFormData();
			}, form.id);
		}		
	}

	function showFormData() {
		var h = '<table class="form-table"><tr><th></th><th>' + messages('ip_address') 
			+ '</th><th>' + messages('date') + '</th></tr>';	
		$.each(formData, function(i, value) {
			h += '<tr><td><input type="checkbox" name="item' + i + '" value="' 
				+ value.id + '"/></td><td>' + value.ipAddress + '</td>'
				+ '<td><a class="showForm" data-index="' + i + '">' 
				+ new Date(value.modDate.time).toLocaleString() + '</a></td></tr>';
		});
		$('#dataTable').html(h + '</table>');
		$('#dataTable tr:even').addClass('even');
		$('#dataTable a.showForm').click(function() {
			onShowFormData($(this).attr('data-index'));
		});
	}

	function onDeleteData() {
		var ids = new Array();
		$('#dataTable input:checked').each(function() {
			ids.push(this.value);
		});
		if (ids.length == 0) {
			Vosao.info(messages('nothing_selected'));
			return;
		}
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.formService.removeData(function(r) {
				if (r.result == 'success') {
					Vosao.info(ids.length + ' ' + messages('form.success_records_delete') 
					    + '.');
					loadData();
				}
				else {
					Vosao.showServiceMessages(r);
				}
			}, Vosao.javaList(ids));
		}
	}

	function onShowFormData(i) {
		formDataIndex = i;
		var data = formData[i];
		var h = '<table class="form-table"><tr><th>' + messages('name') 
			+ '</th><th>' + messages('value') + '</th></tr>';
		$.each(data.values.map, function(key,value) {
			var v = value;
			if (v.indexOf('/file/form') == 0) {
				var parts = v.split('/');
				var filename = parts[parts.length - 1];
				v = '<a href="' + v + '">' + filename + '</a>';
			}
			h += '<tr><td>' + key + '</td><td>' + v + '</td></tr>';
		});
		$('#formData').html(h + '</table>');
		$('#formData tr:even').addClass('even');
		$('#formData-dialog').dialog('open');
	}

	function onFormDataSend() {
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.formService.sendFormLetter(function(r) {
				Vosao.showServiceMessages(r);
			}, formData[formDataIndex].id);
		}
	}
	
	return Backbone.View.extend({
		
		css: '/static/css/form.css',
		
		el: $('#content'),
		
		tmpl: _.template(html),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
			$('#field-dialog, #formData-dialog').dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		},
		
		setId: function(id) {
			formId = id;
		}
		
	});
	
});