/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * Copyright (C) 2009 Vosao development team
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * email: vosao.dev@gmail.com
 */

var registrations = null;
var config = null;

$(function(){
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
    $('#configForm').submit(function() {onSave(); return false;});
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
}

function validate(vo) {
	if (!vo.adminEmail) {
		return 'Admin email is empty';
	}
	if (isNaN(parseInt(vo.clearDays))) {
		return 'Clear days is not a number';
	}
	if (!vo.registerFormTemplate) {
		return 'Register Form Template is empty';
	}
	if (!vo.confirmUserTemplate) {
		return 'Confirm User Template is empty';
	}
	if (!vo.confirmAdminTemplate) {
		return 'Confirm Admin Template is empty';
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
	var h = '<table class="form-table"><tr><th>Email</th><th>Name</th><th></th>';
	$.each(registrations, function(i,value) {
		h += '<tr><td>' + value.email + '</td><td>' + value.name + '</td>\
			<td><a href="#" onclick="onConfirm(' + i + ')">\
			<img src="/static/images/02_plus.png" /></a>\
			<a href="#" onclick="onRemove(' + i + ')">\
			<img src="/static/images/02_x.png" /></a></td></tr>';
	});
	$('#registrations').html(h + '</table>');
}

function onConfirm(i) {
	alert('TODO');
}

function onRemove(i) {
	alert('TODO');
}