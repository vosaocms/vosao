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
 
/**
 * Declared in page.vm
 * 
 *   var pageId;
 *   var pageParentUrl;
 */

var page = null;
var editMode = pageId != '';
var permission = null;
var permissions = null;
var groups = null;
var pageRequest = null;
    
$(function(){
    Vosao.initJSONRpc(loadData);

    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 

    initVersionDialog();
    $("#permission-dialog").dialog({ width: 400, autoOpen: false });
    
    $('#addPermissionButton').click(onAddPermission);
    $('#deletePermissionButton').click(onDeletePermission);
    $('#permissionForm').submit(function() {onPermissionSave(); return false;});
    $('#permissionCancelButton').click(onPermissionCancel);
    $('#allLanguages').change(onAllLanguagesChange);
    
    $('ul.ui-tabs-nav li:nth-child(5)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
    		.removeClass('ui-state-default');

});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadLanguages();
		loadPage();
		breadcrumbsShow();
	}, pageId, pageParentUrl);
}

function callLoadPage() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		editMode = pageId != null;
		loadPage();
	}, pageId, pageParentUrl);
}

function loadPage() {
	if (editMode) {
		pageId = String(page.id);
		pageParentUrl = page.parentUrl;
		loadVersions();
		loadPermissions();
		loadGroups();
	} else {
		pages['1'] = page;
	}
	loadPagePermission();
}

function loadLanguages() {
	var r = pageRequest.languages;
	languages = {};
	var h = '';
	$.each(r.list, function(i, value) {
		languages[value.code] = value;
	});
	setPermissionLanguages();
}
 
function getPermissionName(perm) {
	if (perm == 'DENIED') {
		return messages('denied');
	}
	if (perm == 'READ') {
		return messages('read');
	}
	if (perm == 'WRITE') {
		return messages('read_write');
	}
	if (perm == 'PUBLISH') {
		return messages('read_write_publish');
	}
	if (perm == 'ADMIN') {
		return messages('read_write_publish_grant');
	}
}

function callLoadPermissions() {
	Vosao.jsonrpc.contentPermissionService.selectByUrl(function (r) {
		pageRequest.permissions = r;
		loadPermissions();
	}, page.friendlyURL);
}

function loadPermissions() {
	var r = pageRequest.permissions;
	permissions = Vosao.idMap(r.list);
	var h = '<table class="form-table"><tr><th></th><th>'
		+ messages('group') + '</th><th>' + messages('permission') + '</th><th>'
		+ messages('languages') + '</th></tr>';
	$.each(permissions, function(i,value) {
		var checkbox = '';
		var editLink = value.group.name;
		if (!value.inherited) {
			checkbox = '<input type="checkbox" value="' + value.id + '">';
			editLink = '<a href="#" onclick="onPermissionEdit(' + value.id 
				+ ')"> ' + value.group.name + '</a>';
		}
		var l = value.allLanguages ? messages('all_languages') : value.languages;
		h += '<tr><td>' + checkbox + '</td><td>' + editLink + '</td><td>'
			+ getPermissionName(value.permission) + '</td><td>' + l 
			+ '</td></tr>';
	});
	$('#permissions').html(h + '</table>');
    $('#permissions tr:even').addClass('even'); 
}

function loadGroups() {
	var r = pageRequest.groups;
	groups = Vosao.idMap(r.list);
	var h = '';
	$.each(groups, function(i,value) {
		h += '<option value="' + value.id + '">' + value.name + '</option>';
	});
	$('#groupSelect').html(h);
}

function setPermissionLanguages() {
	var h = '<fieldset><legend>' + messages('languages') + '</legend>';
	$.each(languages, function(i,value) {
		h += '<input type="checkbox" value="' + value.code + '" /> ' + value.title 
			+ '<br />';
	});
	$('#permLanguages').html(h + '</fieldset>');
}

function onPermissionEdit(id) {
	permission = permissions[id];
	initPermissionForm();
	$('#permission-dialog').dialog('open');
}

function initPermissionForm() {
	$('#permission-dialog input[type=radio]').removeAttr('checked');
	$('#allLanguages').attr('checked','checked');
	$('#permLanguages').hide();
	if (permission == null) {
		$('#permission-dialog input[value=READ]').attr('checked', 'checked');
		$('#groupSelect').show();
		$('#groupName').hide();
	}
	else {
		$('#permissionList input[value=' + permission.permission 
				+ ']').attr('checked', 'checked');
		$('#groupSelect').hide();
		$('#groupName').show();
		$('#groupName').text(permission.group.name);
		if (!permission.allLanguages) {
			$('#allLanguages').removeAttr('checked');
			$('#permLanguages').show();
			var codes = permission.languages.split(',');
			$('#permLanguages input').removeAttr('checked');
			$.each(codes, function(i,value) {
				$('#permLanguages input[value=' + value + ']').attr('checked',
						'checked');
			});
		}
	}
}

function onPermissionSave() {
	var langs = '';
	$('#permLanguages input:checked').each(function() {
		langs += (langs == '' ? '' : ',') + this.value;
	});
	var vo = {
		url: page.friendlyURL,
		groupId: permission == null ? $('#groupSelect').val() : 
			String(permission.group.id),
		permission: $('#permissionList input:checked')[0].value,
		languages: $('#allLanguages')[0].checked ? '' : langs
	};
	Vosao.jsonrpc.contentPermissionService.save(function(r) {
		Vosao.showServiceMessages(r);
		$('#permission-dialog').dialog('close');
		if (r.result == 'success') {
			callLoadPermissions();
		}
	}, Vosao.javaMap(vo));
}

function onAddPermission() {
	permission = null;
	initPermissionForm();
	$('#permission-dialog').dialog('open');
}

function onDeletePermission() {
	var ids = [];
	$('#permissions input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info(messages('nothing_selected'));
		return;
	}
	if (confirm(messages('are_you_sure'))) {
		Vosao.jsonrpc.contentPermissionService.remove(function(r) {
			Vosao.showServiceMessages(r);
			callLoadPermissions();
		}, Vosao.javaList(ids));
	}
}

function onPermissionCancel() {
	$('#permission-dialog').dialog('close');
}

function onAllLanguagesChange() {
	if (this.checked) {
		$('#permLanguages').hide();
	}	
	else {
		$('#permLanguages').show();
	}
}

function loadPagePermission() {
    var r = pageRequest.pagePermission;
   	if (r.admin && editMode) {
   		$('.securityTab').show();
   	}
   	else {
   		$('.securityTab').hide();
   	}
}
