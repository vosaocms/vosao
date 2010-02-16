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

var user = null;
var users = null;

$(function(){
    $("#user-dialog").dialog({ width: 460, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#addUserButton').click(onAddUser);
    $('#removeUserButton').click(onRemoveUser);
    $('#userForm').submit(function() {onUserSave(); return false;});
    $('#userCancelDlgButton').click(onUserCancel);
    $('ul.ui-tabs-nav li:nth-child(5)').addClass('ui-state-active')
			.removeClass('ui-state-default');
});

function loadData() {
	loadUsers();
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
    	Vosao.info('Nothing selected');
        return;
    }
    if (confirm('Are you sure?')) {
    	Vosao.jsonrpc.userService.remove(function (r) {
    		Vosao.info(r.message);
            loadUsers();
        }, Vosao.javaList(ids));
    }
}

function loadUsers() {
	Vosao.jsonrpc.userService.select(function (r) {
        var h = '<table class="form-table"><tr><th></th><th>Name</th><th>Email</th><th>Role</th></tr>';
        $.each(r.list, function (i, user) {
            h += '<tr><td><input type="checkbox" value="' + user.id 
                + '"/></td><td>' + user.name + '</td><td>\
                <a href="#" onclick="onUserEdit(' + user.id + ')">' 
                + user.email + '</a></td><td>'
                + getRole(user.role) + '</td></tr>';
        });
        $('#users').html(h + '</table>');
        $('#users tr:even').addClass('even');
    });
}

function getRole(role) {
    if (role == 'ADMIN') return 'Administrator';
    if (role == 'USER') return 'User';
    if (role == 'SITE_USER') return 'Site user';
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
	}
	else {
        $('#userName').val(user.name);
        $('#userEmail').val(user.email);
        $('#userEmail').attr('disabled', true);
        $('#userRole').val(user.roleString);
	}
    $('#userPassword1').val('');
    $('#userPassword2').val('');
    $('#user-dialog .messages').html('');
}

function validateUser(vo) {
    var errors = [];
    if (vo.email == '') {
        errors.push('Email is empty');
    }
    if (vo.password1 != vo.password2) {
        errors.push('Passwords don\'t match');
    }
    return errors;
}

function onUserSave() {
    var vo = {
    	id : user != null ? String(user.id) : '',
        name : $('#userName').val(),
        email : $('#userEmail').val(),
        role : $('#userRole').val(),
        password : $('#userPassword1').val(),
        password1 : $('#userPassword1').val(),
        password2 : $('#userPassword2').val(),
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
