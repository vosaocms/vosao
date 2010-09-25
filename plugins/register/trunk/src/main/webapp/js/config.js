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

var registrations = null;
var config = null;

$(function(){
	localize();
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
    $('#configForm').submit(function() {onSave(); return false;});
    $('#cancelTemplateButton, #cancelButton').click(function() {
    	location.href = "/cms/plugins/config.vm";
    });
});

function loadData() {
	loadConfig();
	loadRegistrations();
}

function loadConfig() {
	Vosao.jsonrpc.registerBackService.getConfig(function(r) {
		config = r;
		showConfig();
	});
}

function showConfig() {
	$('#adminEmail').val(config.adminEmail);
	$('#sendConfirmAdmin').each(function() {this.checked = config.sendConfirmAdmin});
	$('#sendConfirmUser').each(function() {this.checked = config.sendConfirmUser});
	$('#clearDays').val(config.clearDays);
	$('#registerFormTemplate').val(config.registerFormTemplate);
	$('#confirmUserTemplate').val(config.confirmUserTemplate);
	$('#confirmAdminTemplate').val(config.confirmAdminTemplate);
	$('#captcha').each(function() {this.checked = config.captcha});
}

function validate(vo) {
	if (!vo.adminEmail) {
		return messages('register.admin_email_empty');
	}
	if (isNaN(parseInt(vo.clearDays))) {
		return messages('register.clear_days_nan');
	}
	if (!vo.registerFormTemplate) {
		return messages('register.template_empty');
	}
	if (!vo.confirmUserTemplate) {
		return messages('register.confirm_user_empty');
	}
	if (!vo.confirmAdminTemplate) {
		return messages('register.confirm_admin_empty');
	}
}

function onSave() {
	var vo = {
		adminEmail : $('#adminEmail').val(),
		sendConfirmAdmin : String($('#sendConfirmAdmin:checked').size() > 0),
		sendConfirmUser : String($('#sendConfirmUser:checked').size() > 0),
		clearDays : $('#clearDays').val(),
		registerFormTemplate : $('#registerFormTemplate').val(),
		confirmUserTemplate : $('#confirmUserTemplate').val(),
		captcha : String($('#captcha:checked').size() > 0),
		confirmAdminTemplate : $('#confirmAdminTemplate').val()		
	};
	var error = validate(vo);
	if (error) {
		Vosao.error(error);
	}
	else {
		Vosao.jsonrpc.registerBackService.saveConfig(function(r) {
			Vosao.showServiceMessages(r);
		}, Vosao.javaMap(vo));
	}
}

function loadRegistrations() {
	Vosao.jsonrpc.registerBackService.getRegistrations(function(r) {
		registrations = r.list;
		showRegistrations();
	});
}

function showRegistrations() {
	var h = '<table class="form-table"><tr><th>' + messages('register.date')
	    + '</th><th>' + messages('email') + '</th><th>' + messages('name') 
	    + '</th><th></th></tr>';
	$.each(registrations, function(i,value) {
		h += '<tr><td>' + value.createdDateString + '</td><td>' 
			+ value.email + '</td><td>' + value.name 
			+ '</td><td><a href="#" onclick="onConfirm(' + i 
			+ ')" title="Confirm"><img src="/static/images/02_plus.png" /></a>'
			+ '<a href="#" onclick="onRemove(' + i + ')" title="' 
			+ messages('remove') + '"><img src="/static/images/02_x.png" /></a></td></tr>';
	});
	$('#registrations').html(h + '</table>');
}

function onConfirm(i) {
	if (confirm(messages('are_you_sure'))) {
		Vosao.jsonrpc.registerBackService.confirmRegistration(function(r) {
			Vosao.showServiceMessages(r);
			if (r.result == 'success') {
				loadData();
			}
		}, registrations[i].id);
	}
}

function onRemove(i) {
	if (confirm(messages('are_you_sure'))) {
		Vosao.jsonrpc.registerBackService.removeRegistration(function(r) {
			Vosao.showServiceMessages(r);
			if (r.result == 'success') {
				loadData();
			}
		}, registrations[i].id);
	}
}

function onFormTemplateRestore() {
	Vosao.jsonrpc.registerBackService.restoreRegisterFormTemplate(function(r) {
		if (r.result == 'success') {
			$('#registerFormTemplate').val(r.message);
			Vosao.info(messages('success'));
		}
		else {
			Vosao.showServiceMessages(r);
		}
	});
}

function onConfirmUserLetterRestore() {
	Vosao.jsonrpc.registerBackService.restoreUserConfirmLetter(function(r) {
		if (r.result == 'success') {
			$('#confirmUserTemplate').val(r.message);
			Vosao.info(messages('success'));
		}
		else {
			Vosao.showServiceMessages(r);
		}
	});
}

function onConfirmAdminLetterRestore() {
	Vosao.jsonrpc.registerBackService.restoreAdminConfirmLetter(function(r) {
		if (r.result == 'success') {
			$('#confirmAdminTemplate').val(r.message);
			Vosao.info(messages('success'));
		}
		else {
			Vosao.showServiceMessages(r);
		}
	});
}

function localize() {
	document.title = messages('register.plugin_config');
	$('#leftmenu a:eq(1)').text(messages('content'));
	$('#leftmenu a:eq(2)').text(messages('templates'));
	$('#leftmenu a:eq(3)').text(messages('resources'));
	$('#leftmenu a:eq(4)').text(messages('configuration'));
	$('#leftmenu a:eq(5)').text(messages('plugins'));
	$('#leftmenu a:eq(6)').text(messages('plugins.config'));

	$('#rightmenu a:eq(0)').text(messages('profile'));
	$('#rightmenu a:eq(1)').text(messages('logout'));
	$('#languageSelect').text(messages('language'));
	$('#rightmenu a:contains(support)').text(messages('support'));
	
	$('#tabs ul li:eq(0) a').text(messages('register.tab1'));
	$('#tabs ul li:eq(1) a').text(messages('templates'));
	$('#tabs ul li:eq(2) a').text(messages('register.tab3'));
	
	$('#tab-1 div:eq(0) label').text(messages('register.admin_email'));
	$('#tab-1 div:eq(1) label').text(messages('register.send_confirm_admin'));
	$('#tab-1 div:eq(2) label').text(messages('register.send_confirm_user'));
	$('#tab-1 div:eq(3) label').text(messages('register.clear_days'));
	$('#tab-1 div:eq(4) label').text(messages('register.enable_captcha'));

	$('input[value=save]').val(messages('save'));
	$('input[value=cancel]').val(messages('cancel'));
	
	$('#tab-2 > div:eq(0) span').html(messages('register.registration_form'));
	$('#tab-2 a').text(messages('restore'));
	$('#tab-2 > div:eq(1) span').html(messages('register.confirm_user_letter'));
	$('#tab-2 > div:eq(2) span').html(messages('register.confirm_admin_letter'));
}