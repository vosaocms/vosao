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

var user = null;
var timezones = null;

$(function(){
	$("#tabs").tabs();
	Vosao.initJSONRpc(loadData);
    $('#saveButton').click(onSave);
});

function loadData() {
	Vosao.jsonrpc.userService.getTimezones(function (r) {
        timezones = r.list;
        showTimezones();
        loadUser();
    });
}

function loadUser() {
	Vosao.jsonrpc.userService.getLoggedIn(function (r) {
        user = r;
        $('#name').val(user.name);
        $('#email').val(user.email);
        $('#password1').val('');
        $('#password2').val('');
        $('#timezone').val(user.timezone);
    });
}

function validPasswords() {
    var pass1 = $('#password1').val();
    var pass2 = $('#password2').val();
    if (pass1 || pass2) {
        if (pass1 == pass2) {
            return true;
        }
        return false;
    }
    return true;
}
    
function onSave() {
    var pass = '';
    if (validPasswords()) {
        pass = $('#password1').val();
    }
    else {
    	Vosao.error(messages('profile.password_dont_match'));
        return;
    }
    var vo = {
     	id : String(user.id),
        name : $('#name').val(),
        timezone : $('#timezone').val(),
        password : pass   
    };
    Vosao.jsonrpc.userService.save(function (r) {
    	Vosao.showServiceMessages(r);
    }, Vosao.javaMap(vo));
}

function showTimezones() {
	var h = '';
	$.each(timezones, function(i, value) {
		h += '<option value="' + value + '">' + value + '</option>';
	});
	$('#timezone').html(h);
}
