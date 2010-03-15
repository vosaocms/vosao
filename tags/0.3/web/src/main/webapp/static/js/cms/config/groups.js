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

var group = null;
var groups = null;
var users = null;

$(function(){
     $("#group-dialog").dialog({ width: 460, autoOpen: false });
    $("#user-group-dialog").dialog({ width: 300, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    $('#addGroupButton').click(onAddGroup);
    $('#removeGroupButton').click(onRemoveGroup);
    $('#groupForm').submit(function() {onGroupSave(); return false;});
    $('#groupCancelDlgButton').click(onGroupCancel);
    $('#userGroupForm').submit(function() {onUserGroupSave(); return false;});
    $('#userGroupCancelDlgButton').click(onUserGroupCancel);
    $('ul.ui-tabs-nav li:nth-child(6)').addClass('ui-state-active')
			.removeClass('ui-state-default');
});

function loadData() {
	loadGroups();
}

// Group

function loadGroups() {
	Vosao.jsonrpc.groupService.select(function (r) {
    	groups = Vosao.idMap(r.list);
        var h = '<table class="form-table"><tr><th></th><th>Name</th><th>Users</th></tr>';
        $.each(r.list, function (i, group) {
        	if (group.name == 'guests') {
        		return;
        	}
        	var users = 'add users';
        	if (group.users.list.length > 0) {
        		users = '';
        		$.each(group.users.list, function (i, user) {
        			users += (i==0 ? '' : ', ') + user.name;
        		});
        	}
        	var editLink = '<a href="#" onclick="onGroupEdit(' + group.id + ')">' 
                + group.name + '</a>';
        	var userGroupLink = '<a href="#" onclick="onEditUserGroup(\'' 
                + group.id + '\')">' + users + '</a>';
            h += '<tr><td><input type="checkbox" value="' + group.id 
                + '"/></td><td>' + editLink + '</td><td>' + userGroupLink 
                + '</td></tr>';
        });
        $('#groups').html(h + '</table>');
        $('#groups tr:even').addClass('even');
    });
}

function onAddGroup() {
    group = null;
    initGroupForm();
    $('#group-dialog').dialog('open');
}

function onRemoveGroup() {
    var ids = [];
    $('#groups input:checked').each(function () {
        ids.push(String(this.value));
    });
    if (ids.length == 0) {
    	Vosao.info('Nothing selected');
        return;
    }
    if (confirm('Are you sure?')) {
    	Vosao.jsonrpc.groupService.remove(function (r) {
    		Vosao.info(r.message);
            loadGroups();
        }, Vosao.javaList(ids));
    }
}

function onGroupEdit(id) {
	Vosao.jsonrpc.groupService.getById(function (r) {
        group = r;
        initGroupForm();
        $('#group-dialog').dialog('open');
    }, id);
}

function initGroupForm() {
	if (group == null) {
        $('#groupName').val('');
	}
	else {
        $('#groupName').val(group.name);
	}
    $('#group-dialog .messages').html('');
}

function validateGroup(vo) {
    var errors = [];
    if (vo.name == '') {
        errors.push('Name is empty');
    }
    return errors;
}

function onGroupSave() {
    var vo = {
    	id : group != null ? String(group.id) : '',
        name : $('#groupName').val()
    };
    var errors = validateGroup(vo);
    if (errors.length == 0) {
    	Vosao.jsonrpc.groupService.save(function (r) {
            if (r.result == 'success') {
                $('#group-dialog').dialog('close');
                Vosao.info(r.message);
                loadGroups();
            }
            else {
                groupErrors(r.messages.list);
            }
        }, Vosao.javaMap(vo));
    }
    else {
        groupErrors(errors);
    }
}

function onGroupCancel() {
    $('#group-dialog').dialog('close');
}

function groupError(msg) {
	Vosao.errorMessage('#group-dialog .messages', msg);
}

function groupErrors(errors) {
	Vosao.errorMessages('#group-dialog .messages', errors);
}

function onEditUserGroup(id) {
	group = groups[id];
	Vosao.jsonrpc.userService.select(function (r) {
		users = Vosao.idMap(r.list);
		var groupUsers = Vosao.idMap(group.users.list);
		var h = '';
		$.each(users, function (i, value) {
			var checked = '';
			if (groupUsers[value.id] != undefined) {
				checked = 'checked = "checked"';
			}
			h += '<div class="form-row"><input type="checkbox" ' + checked 
				+ ' value="' + value.id + '"> ' + value.name + '</div>';
		});
		$('#groupUsers').html(h);
		$('#user-group-dialog').dialog('open');
	});
}

function onUserGroupCancel() {
    $('#user-group-dialog').dialog('close');
}

function onUserGroupSave() {
	var usersId = [];
	$('#user-group-dialog input:checked').each(function () {
		usersId.push(this.value);
	});
	Vosao.jsonrpc.groupService.setGroupUsers(function (r) {
		Vosao.showServiceMessages(r);
	    $('#user-group-dialog').dialog('close');
	    loadGroups();
	}, group.id, Vosao.javaList(usersId));
}
