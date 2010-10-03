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

var pageRequest = null;
var page = null;
var editMode = pageId != '';
var attributes = null;
    
$(function(){
    Vosao.initJSONRpc(loadData);
    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 
    initVersionDialog();
    //$('#addButton').click(onEnableComments);
    // to be continued...
    //$('#deleteButton').click(onDeleteComments);
    $('ul.ui-tabs-nav li:nth-child(7)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
    		.removeClass('ui-state-default');
});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
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
		showAuditInfo();
		loadAttributes();
	} else {
		pages['1'] = page;
	}
	loadPagePermission();
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

function loadAttributes() {
	Vosao.jsonrpc.pageAttributeService.getByPage(function(r) {
		attributes = r.list;
		showAttributes();
	}, page.friendlyURL);
}

function showAttributes() {
	// TODO
}