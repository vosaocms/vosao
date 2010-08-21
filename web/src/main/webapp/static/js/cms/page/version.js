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
 
var versions = [];
var pages = {};

function initVersionDialog() {
    $("#version-dialog").dialog({ width: 400, autoOpen: false });
    $('#addVersionLink').click(onAddVersion);
    $('#versionCancelButton').click(onVersionTitleCancel);
    $('#versionForm').submit(function() {onVersionTitleSave(); return false;});
}

function callLoadVersions() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadVersions();
	}, pageId, pageParentUrl);
}

function loadVersions() {
	var r = pageRequest.versions; 
    versions = [];
    pages = {};
    $.each(r.list, function (i, value) {
        pages[String(value.version)] = value;
        versions.push(String(value.version));
    });
    versions.sort();
    var h = '';
    $.each(versions, function (i, version) {
        var vPage = pages[version];
        h += '<div>';
        if (pageId != vPage.id) {
            h += '<a class="button ui-state-default ui-corner-all"\
               href="#" title="' + vPage.versionTitle + '"\
               onclick="onVersionSelect(\'' + version + '\')">Version ' 
               + version +'</a>';
        }
        else {
            h += '<a class="button ui-state-default ui-state-active \
               ui-corner-all" href="#" title="' + vPage.versionTitle 
               + '" onclick="onVersionSelect(\'' + version + '\')" \
               ><span class="ui-icon ui-icon-triangle-1-e"></span> \
               Version ' + version + '</a>';
        }
        if (versions.length > 1) {
        	h += '<img class="button" src="/static/images/delete-16.png" \
        		onclick="onVersionDelete(\'' + version + '\')"/></div>';
        }
    });
    $('#versions .vertical-buttons-panel').html(h);
}

function onVersionDelete(version) {
	if (confirm(messages('are_you_sure'))) {
		var delPage = pages[version];
		Vosao.jsonrpc.pageService.deletePageVersion(function(r) {
			if (version == String(page.version)) {
				if (versions.length == 1) {
					location.href = '/cms/pages.vm';
				} else {
					var previousVersion = versions[0];
					if (versions.indexOf(version) == 0) {
						previousVersion = versions[1];
					} else {
						previousVersion = versions[versions
								.indexOf(version) - 1];
					}
					pageId = pages[previousVersion].id;
					loadData();
				}
			} else {
				callLoadVersions();
			}
		}, delPage.id);
	}
}

function onAddVersion() {
	$('#version-dialog').dialog('open');
	$('#version-title').val('');
}

function onVersionTitleSave() {
	Vosao.jsonrpc.pageService.addVersion(function(r) {
		if (r.result == 'success') {
			pageId = r.message;
			loadData();
			Vosao.info(messages('page.version_success_add'));
		}
		else {
			Vosao.showServiceMessages(r);
		}			
		$('#version-dialog').dialog('close');
	}, page.friendlyURL, $('#version-title').val());
}

function onVersionTitleCancel() {
	$('#version-dialog').dialog('close');
}

function onVersionSelect(version) {
	var selPage = pages[version];
	pageId = selPage.id;
	loadData();
}

function showAuditInfo() {
	$('#pageState').html(page.stateString == 'EDIT' ? 
			messages('edit') : messages('approved'));
	$('#pageCreateDate').html(page.createDateTimeString);
	$('#pageModDate').html(page.modDateTimeString);
	$('#pageCreateUser').html(page.createUserEmail);
	$('#pageModUser').html(page.modUserEmail);
}
