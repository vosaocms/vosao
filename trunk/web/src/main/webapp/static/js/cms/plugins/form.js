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

var editMode = formId != '';
var field = null;
var fields = null;
var test = null;

$( function() {
	$("#tabs").tabs();
	$("#field-dialog").dialog({ width :500, autoOpen :false });
	Vosao.initJSONRpc(loadData);
	$('#saveButton').click(onUpdate);
	$('#cancelButton').click(onCancel);
	$('#addFieldButton').click(onAddField);
	$('#deleteFieldButton').click(onDeleteFields);
	$('#fieldType').change(onFieldTypeChange);
	$('#saveAndAddButton').click(onSaveAndAdd);
	$('#fieldSaveButton').click(function() { onFieldSave(true); });
	$('#fieldCancelButton').click(onFieldCancel);
});

function loadData() {
	loadForm();
	loadFields();
}

function loadFields() {
	if (formId == '') {
		return;
	}
	Vosao.jsonrpc.fieldService.getByForm(function(r, e) {
		fields = r;
		if (r.list.length > 0) {
			var h = '<table class="form-table"><tr><th></th><th>Title</th><th>Name</th><th>Type</th></tr>';
			for ( var i = 0; i < r.list.length; i++) {
				var field = r.list[i];
				h += 
					'<tr>\
					<td><input type="checkbox" name="item' + i + '" value="' + field.id + '"/></td>\
					<td><a href="#" onclick="onFieldEdit(\'' + field.id + '\')">'   + field.title + '</a></td>\
					<td>' + field.name + '</td>\
					<td>'   + fieldTypeString(field.fieldType) + '</td>\
					</tr>';
			}
			h += '</table>';
			$('#fieldsTable').html(h);
			$('#fieldsTable tr:even').addClass('even');
		}
	}, formId);
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
	var fieldType = $('select[name=field.fieldType]').val();
	if (fieldType == 'TEXT') {
		$('#field-values').hide();
		$('#field-height').show();
		$('#field-width').show();
		$('#field-defaultValue').show();
	}
	if (fieldType == 'LISTBOX') {
		$('#field-values').show();
		$('#field-height').hide();
		$('#field-width').hide();
		$('#field-defaultValue').hide();
	}
	if (fieldType == 'CHECKBOX') {
		$('#field-values').show();
		$('#field-height').hide();
		$('#field-width').hide();
		$('#field-defaultValue').hide();
	}
	if (fieldType == 'RADIO') {
		$('#field-values').show();
		$('#field-height').hide();
		$('#field-width').hide();
		$('#field-defaultValue').hide();
	}
	if (fieldType == 'PASSWORD') {
		$('#field-values').hide();
		$('#field-height').hide();
		$('#field-width').show();
		$('#field-defaultValue').hide();
	}
	if (fieldType == 'FILE') {
		$('#field-values').hide();
		$('#field-height').hide();
		$('#field-width').hide();
		$('#field-defaultValue').hide();
	}
}

function fieldDialogInit() {
	if (field == null) {
		$('input[name=field.name]').val('');
		$('input[name=field.title]').val('');
		$('select[name=field.fieldType]').val('TEXT');
		$('textarea[name=field.values]').val('');
		$('input[name=field.defaultValue]').val('');
		$('input[name=field.height]').val('1');
		$('input[name=field.width]').val('20');
		$('input[name=field.mandatory]')[0].checked = false;
	} else {
		$('input[name=field.name]').val(field.name);
		$('input[name=field.title]').val(field.title);
		$('select[name=field.fieldType]').val(field.fieldType);
		$('textarea[name=field.values]').val(field.values);
		$('input[name=field.defaultValue]').val(field.defaultValue);
		$('input[name=field.height]').val(field.height);
		$('input[name=field.width]').val(field.width);
		$('input[name=field.mandatory]')[0].checked = field.optional;
	}
	fieldDialogShowInputs();
}

function createFieldVO() {
	return Vosao.javaMap( {
		id :field != null ? field.id : null,
				formId :formId,
				name :$('input[name=field.name]').val(),
				title :$('input[name=field.title]').val(),
				fieldType :$('select[name=field.fieldType]').val(),
				values :$('textarea[name=field.values]').val(),
				defaultValue :$('input[name=field.defaultValue]').val(),
				height :$('input[name=field.height]').val(),
				width :$('input[name=field.width]').val(),
				mandatory :String($('input[name=field.mandatory]:checked').size() > 0)
	});
}

function validateField(fieldVO) {
	var errors = new Array();
	if (fieldVO.map.name == '') {
		errors.push('Name is empty');
	}
	if (fieldVO.map.title == '') {
		errors.push('Title is empty');
	}
	var height = Number(fieldVO.map.height);
	if (fieldVO.map.fieldType == 'TEXT' && height <= 0) {
		errors.push('Height can\'t be less or zero');
	}
	var width = Number(fieldVO.map.width);
	if (fieldVO.map.fieldType == 'TEXT' && width <= 0) {
		errors.push('Width can\'t be less or zero');
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
		Vosao.info('Nothing selected');
		return;
	}
	if (confirm('Are you sure?')) {
		Vosao.jsonrpc.fieldService.remove(function() {
			Vosao.info(ids.length + ' fields was successfully deleted.');
			loadFields();
		}, Vosao.javaList(ids));
	}
}

function onSaveAndAdd() {
	onFieldSave(false);
	onAddField();
}

function loadForm() {
	Vosao.jsonrpc.formService.getForm(function (r) {
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
	});
	Vosao.jsonrpc.formService.saveForm(function (r) {
		if (r.result = 'success') {
			location.href = '/cms/plugins/forms.jsp';
		}
		else {
			Vosao.showServiceMessages(r);
		}
	}, vo);
}

function onCancel() {
	location.href = '/cms/plugins/forms.jsp';
}
