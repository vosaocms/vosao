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
var users = null;

$(function(){
    $("#user-dialog").dialog({ width: 500, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#addUserButton').click(onAddUser);
    $('#removeUserButton').click(onRemoveUser);
    $('#userForm').submit(function() {onUserSave(); return false;});
    $('#userCancelDlgButton').click(onUserCancel);
    $('#userDisableDlgButton').click(onUserDisable);
    $('ul.ui-tabs-nav li:nth-child(5)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
			.removeClass('ui-state-default');
});

function loadData() {
	Vosao.jsonrpc.userService.getTimezones(function (r) {
        timezones = r.list;
        showTimezones();
        loadUsers();
    });
}

// User 

function onAddUser() {
    user = null;
    initUserForm();
    $('#user-dialog').dialog('open');
}

function onRemoveUser() {
    var ids = [];
    $('#users input:checked').each(function () {
        ids.push(String(this.value));
    });
    if (ids.length == 0) {
    	Vosao.info(messages('nothing_selected'));
        return;
    }
    if (confirm(messages('are_you_sure'))) {
    	Vosao.jsonrpc.userService.remove(function (r) {
    		Vosao.info(r.message);
            loadUsers();
        }, Vosao.javaList(ids));
    }
}

function loadUsers() {
	Vosao.jsonrpc.userService.select(function (r) {
        var h = '<table class="form-table"><tr><th></th><th>' + messages('name') 
        	+ '</th><th>' + messages('email') + '</th><th>' + messages('role') 
        	+ '</th><th>' + messages('access') + '</th></tr>';
        $.each(r.list, function (i, user) {
            var disabled = user.disabled ? messages('disabled') : messages('enabled');
        	h += '<tr><td><input type="checkbox" value="' + user.id 
                + '"/></td><td>' + user.name + '</td><td>\
                <a href="#" onclick="onUserEdit(' + user.id + ')">' 
                + user.email + '</a></td><td>'
                + getRole(user.role) + '</td>'
                + '<td>' + disabled + '</td></tr>';
        });
        $('#users').html(h + '</table>');
        $('#users tr:even').addClass('even');
    });
}

function getRole(role) {
    if (role == 'ADMIN') return messages('administrator');
    if (role == 'USER') return messages('user');
    if (role == 'SITE_USER') return messages('site_user');
}

function onUserEdit(id) {
	Vosao.jsonrpc.userService.getById(function (r) {
        user = r;
        initUserForm();
        $('#user-dialog').dialog('open');
    }, id);
}

function initUserForm() {
	if (user == null) {
        $('#userName').val('');
        $('#userEmail').val('');
        $('#userEmail').removeAttr('disabled');
        $('#userRole').val('');
        $('#userDisableDlgButton').hide();
	}
	else {
        $('#userName').val(user.name);
        $('#userEmail').val(user.email);
        $('#userEmail').attr('disabled', true);
        $('#userRole').val(user.roleString);
        $('#timezone').val(user.timezone);
        $('#userDisableDlgButton').val(user.disabled ? messages('enable') : 
    		messages('disable')).show();
	}
    $('#userPassword1').val('');
    $('#userPassword2').val('');
    $('#user-dialog .messages').html('');
}

function validateUser(vo) {
    var errors = [];
    if (vo.email == '') {
        errors.push(messages('email_is_empty'));
    }
    if (vo.password1 != vo.password2) {
        errors.push(messages('config.passwords_dont_match'));
    }
    return errors;
}

function onUserSave() {
    var vo = {
    	id : user != null ? String(user.id) : '',
        name : $('#userName').val(),
        email : $('#userEmail').val(),
        role : $('#userRole').val(),
        timezone : $('#timezone').val(),
        password : $('#userPassword1').val(),
        password1 : $('#userPassword1').val(),
        password2 : $('#userPassword2').val()
    };
    var errors = validateUser(vo);
    if (errors.length == 0) {
    	Vosao.jsonrpc.userService.save(function (r) {
            if (r.result == 'success') {
                $('#user-dialog').dialog('close');
                Vosao.info(r.message);
                loadUsers();
            }
            else {
                userErrors(r.messages.list);
            }
        }, Vosao.javaMap(vo));
    }
    else {
        userErrors(errors);
    }
}

function onUserCancel() {
    $('#user-dialog').dialog('close');
}

function userError(msg) {
	Vosao.errorMessage('#user-dialog .messages', msg);
}

function userErrors(errors) {
	Vosao.errorMessages('#user-dialog .messages', errors);
}

function onUserDisable() {
	Vosao.jsonrpc.userService.disable(function(r) {
        $('#user-dialog').dialog('close');
        Vosao.showServiceMessages(r);
        loadUsers();
	}, user.id, !user.disabled);
}

function showTimezones() {
	var h = '';
	$.each(timezones, function(i, value) {
		h += '<option value="' + value + '">' + value + '</option>';
	});
	$('#timezone').html(h);
}
