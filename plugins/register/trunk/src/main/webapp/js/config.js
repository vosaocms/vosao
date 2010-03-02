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

$(function(){
    $("#tabs").tabs();
    Vosao.initJSONRpc(loadData);
});

function test() {
    Vosao.jsonrpc.registerBackService.test(function(r) {});
}

function loadData() {
	loadRegistrations();
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