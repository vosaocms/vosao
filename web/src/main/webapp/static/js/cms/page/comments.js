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
 
/**
 * Declared in page.jsp
 * 
 *   var pageId;
 *   var pageParentUrl;
 */

var pageRequest = null;
var page = null;
var editMode = pageId != '';
    
$(function(){
    Vosao.initJSONRpc(loadData);
    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 
    initVersionDialog();
    $('#enableCommentsButton').click(onEnableComments);
    $('#disableCommentsButton').click(onDisableComments);
    $('#deleteCommentsButton').click(onDeleteComments);
    $('ul.ui-tabs-nav li:nth-child(4)').addClass('ui-state-active')
    		.removeClass('ui-state-default');
});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadPage();
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
		pageId = page.id;
		pageParentUrl = page.parentUrl;
		loadVersions();
		loadComments();
	} else {
		pages['1'] = page;
	}
	loadPagePermission();
}

function loadComments() {
	var r = pageRequest.comments;
    var html = '<table class="form-table"><tr><th></th><th>Status</th>\
        <th>Name</th><th>Content</th></tr>';
    $.each(r.list, function (n, value) {
        var status = value.disabled ? 'Disabled' : 'Enabled';
        html += '<tr><td><input type="checkbox" value="' + value.id 
        + '"/></td><td>' + status + '</a></td><td>' + value.name
        + '</td><td>' + value.content + '</td></tr>';
    });
    $('#comments').html(html + '</table>');
    $('#comments tr:even').addClass('even'); 
}

function onEnableComments() {
	var ids = [];
	$('#comments input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info('Nothing selected.');
		return;
	}
	Vosao.jsonrpc.commentService.enableComments(function(r) {
		Vosao.showServiceMessages(r);
		loadData();
	}, Vosao.javaList(ids));
}

function onDisableComments() {
	var ids = [];
	$('#comments input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info('Nothing selected.');
		return;
	}
	Vosao.jsonrpc.commentService.disableComments(function(r) {
		Vosao.showServiceMessages(r);
		loadData();
	}, Vosao.javaList(ids));
}

function onDeleteComments() {
	var ids = [];
	$('#comments input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info('Nothing selected.');
		return;
	}
	if (confirm('Are you sure?')) {
		Vosao.jsonrpc.commentService.deleteComments(function(r) {
			Vosao.showServiceMessages(r);
			loadData();
		}, Vosao.javaList(ids));
	}
}

function loadPagePermission() {
    var r = pageRequest.pagePermission;
   	if (r.changeGranted) {
   		$('#enableCommentsButton').show();
   		$('#disableCommentsButton').show();
   		$('#deleteCommentsButton').show();
   	}
   	else {
   		$('#enableCommentsButton').hide();
   		$('#disableCommentsButton').hide();
   		$('#deleteCommentsButton').hide();
   	}
   	if (r.admin && editMode) {
   		$('.securityTab').show();
   	}
   	else {
   		$('.securityTab').hide();
   	}
}
