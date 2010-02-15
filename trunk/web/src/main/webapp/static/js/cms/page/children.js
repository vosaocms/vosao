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

var page = null;
var pageRequest = null;
var children = {list:[]};
var editMode = pageId != '';
    
$(function(){
    Vosao.initJSONRpc(loadData);

    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 

    $("#version-dialog").dialog({ width: 400, autoOpen: false });

    $('#addVersionLink').click(onAddVersion);
    $('#addChildButton').click(onAddChild);
    $('#deleteChildButton').click(onDelete);
    $('#versionSaveButton').click(onVersionTitleSave);
    $('#versionCancelButton').click(onVersionTitleCancel);
    
    $('ul.ui-tabs-nav li:nth-child(3)').addClass('ui-state-active')
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
		loadChildren();
		loadVersions();
	} else {
		pages['1'] = page;
	}
	loadPagePermission();
}

function callLoadChildren() {
	Vosao.jsonrpc.pageService.getChildren(function(r) {
		pageRequest.children = r;
		loadChildren();
	}, page.friendlyURL);
}

function loadChildren() {
	var r = pageRequest.children;
    var html = '<table class="form-table"><tr><th></th><th>Title</th>\
        <th>Friendly URL</th></tr>';
    $.each(r.list, function (n, value) {
        html += '<tr><td><input type="checkbox" value="' + value.id 
        + '"/></td><td><a href="/cms/page/content.jsp?id=' + value.id 
        +'">' + value.title + '</a></td><td>' + value.friendlyURL
        + '</td></tr>';
    });
    $('#children').html(html + '</table>');
    $('#children tr:even').addClass('even');
}

function onAddChild() {
	location.href = '/cms/page/index.jsp?parent=' + encodeURIComponent(page.friendlyURL);
}

function onDelete() {
	var ids = [];
	$('#children input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info('Nothing selected.');
		return;
	}
	if (confirm('Are you sure?')) {
		Vosao.jsonrpc.pageService.deletePages(function(r) {
			Vosao.showServiceMessages(r);
			callLoadChildren();
		}, Vosao.javaList(ids));
	}
}

function loadPagePermission() {
    var r = pageRequest.pagePermission;
   	if (r.changeGranted) {
   		$('#addChildButton').show();
   		$('#deleteChildButton').show();
   	}
   	else {
   		$('#addChildButton').hide();
   		$('#deleteChildButton').hide();
   	}
   	if (r.admin && editMode) {
   		$('.securityTab').show();
   	}
   	else {
   		$('.securityTab').hide();
   	}
}
