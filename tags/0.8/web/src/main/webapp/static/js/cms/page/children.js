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
var pageRequest = null;
var parentURL = null;
var children = null;
var editMode = pageId != '';
    
$(function(){
    $("#page-dialog").dialog({ width: 400, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 
    initVersionDialog();
    $('#addChildButton').click(onAddChild);
    $('#deleteChildButton').click(onDelete);
    $('#defaultSettingsButton').click(onDefaultSettings);
    $('ul.ui-tabs-nav li:nth-child(3)')
    		.addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
    		.removeClass('ui-state-default');
	$('#cancelDlgButton').click(onPageCancel);
    $('#pageForm').submit(function() {onSave(); return false;});
    $('#title').change(onTitleChange);

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
		loadChildren();
		loadVersions();
		showAuditInfo();
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
	children = pageRequest.children.list;
    var html = '<table class="form-table"><tr><th></th><th>' + messages('title') 
    	+ '</th><th>' + messages('page.friendly_url') + '</th><th></th></tr>';
    $.each(children, function (n, value) {
        html += '<tr><td><input type="checkbox" value="' + value.id 
        + '"/></td><td><a href="/cms/page/content.vm?id=' + value.id 
        +'">' + value.title + '</a></td><td>' + value.friendlyURL + '</td>\
		<td><a href="#" onclick="onPageUp(' + n + ')"><img src="/static/images/02_up.png"/></a>\
        <a href="#" onclick="onPageDown(' + n + ')"><img src="/static/images/02_down.png"/></a>\
        </td></tr>';
    });
    $('#children').html(html + '</table>');
    $('#children tr:even').addClass('even');
}

function onAddChild() {
	$('#ui-dialog-title-page-dialog').text(messages('pages.new_page'));
	parentURL = page.friendlyURL == '/' ? '' : page.friendlyURL;
	$('#page-dialog').dialog('open');
	$('#parentURL').html(parentURL + '/');
	$('#title').val('');
	$('#url').val('');
	$('#title').focus();
}

function onDelete() {
	var ids = [];
	$('#children input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info(messages('nothing_selected'));
		return;
	}
	if (confirm(messages('are_you_sure'))) {
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

function onPageUp(i) {
	if (i - 1 >= 0) {
		Vosao.jsonrpc.pageService.moveUp(function(r) {}, children[i].id);
        children[i].sortIndex--;
        children[i - 1].sortIndex++;
		swapPages(i, i - 1);
		callLoadChildren();
	}
}

function onPageDown(i) {
	if (i + 1 < children.length) {
		Vosao.jsonrpc.pageService.moveDown(function(r) {}, children[i].id);
        children[i + 1].sortIndex--;
        children[i].sortIndex++;
		swapPages(i, i + 1);
		callLoadChildren();
	}
}

function swapPages(i, j) {
	var tmp = children[j];
	children[j] = children[i];
	children[i] = tmp;
}

function onDefaultSettings() {
	Vosao.jsonrpc.pageService.getPageDefaultSettings(function(r) {
		location.href = "/cms/page/index.vm?id=" + r.id;
	}, page.friendlyURL);
}

function onPageCancel() {
	$('#page-dialog').dialog('close');
}

function onTitleChange() {
	var url = $("#url").val();
	var title = $("#title").val();
	if (url == '') {
		$("#url").val(Vosao.urlFromTitle(title));
	}
}

function validate(vo) {
	if (vo.title == '') {
		return messages('title_is_empty');
	}
	else {
		if (vo.title.indexOf(',') != -1) {
			return messages('pages.coma_not_allowed');
		}
	}
	if (vo.url == '') {
		return messages('pages.url_is_empty');
	}
	else {
		if (vo.url.indexOf('/') != -1) {
			return messages('pages.slash_not_allowed');
		}
	}
}

function onSave() {
	var vo = {
		id : '',
		title : $('#title').val(),
		url : $('#url').val(),
		friendlyUrl : parentURL + '/' + $('#url').val()
	};
	var error = validate(vo);
	if (!error) {
		Vosao.jsonrpc.pageService.savePage(function(r) {
			if (r.result == 'success') {
				Vosao.info(messages('pages.success_created'));
				$('#page-dialog').dialog('close');
				location.href = '/cms/page/content.vm?id=' + r.message;
			}
			else {
				showError(r.message);
			}
		}, Vosao.javaMap(vo));
	}
	else {
		showError(error);
	}
}

function showError(msg) {
	Vosao.errorMessage('#pageMessages', msg);
}
