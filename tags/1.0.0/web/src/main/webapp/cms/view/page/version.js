/**
 * Vosao CMS. Simple CMS for Google App Engine.
 * 
 * Copyright (C) 2009-2011 Vosao development team.
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

define(['view/page/context'], function(ctx) {

var versions = [];

function initVersionDialog() {
    $('#addVersionLink').click(onAddVersion);
    $('#versionCancelButton').click(onVersionTitleCancel);
    $('#versionForm').submit(function() {onVersionTitleSave(); return false;});
}

function callLoadVersions() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		ctx.pageRequest = r;
		ctx.page = ctx.pageRequest.page;
		loadVersions();
	}, ctx.pageId, ctx.pageParentUrl);
}

function loadVersions() {
	var r = ctx.pageRequest.versions; 
    versions = [];
    ctx.pages = {};
    $.each(r.list, function (i, value) {
    	ctx.pages[String(value.version)] = value;
        versions.push(String(value.version));
    });
    versions.sort();
    var h = '';
    $.each(versions, function (i, version) {
        var vPage = ctx.pages[version];
        h += '<div>';
        if (ctx.pageId != vPage.id) {
            h += '<a class="select button ui-state-default ui-corner-all"\
               title="' + vPage.versionTitle + '"\
               data-version="' + version + '">Version ' + version +'</a>';
        }
        else {
            h += '<a class="select button ui-state-default ui-state-active \
               ui-corner-all" title="' + vPage.versionTitle 
               + '" data-version="' + version + '" \
               ><span class="ui-icon ui-icon-triangle-1-e"></span> \
               Version ' + version + '</a>';
        }
        if (versions.length > 1) {
        	h += '<img class="delete button" src="/static/images/delete-16.png" data-version="' 
        		+ version + '"/></div>';
        }
    });
    $('#versions .vertical-buttons-panel').html(h);
    $('#versions a.select').click(function() {
   		onVersionSelect($(this).attr('data-version'));
    });
    $('#versions img.delete').click(function() {
   		onVersionDelete($(this).attr('data-version'));
    });

}

function onVersionDelete(version) {
	if (confirm(messages('are_you_sure'))) {
		var delPage = ctx.pages[version];
		Vosao.jsonrpc.pageService.deletePageVersion(function(r) {
			if (version == String(ctx.page.version)) {
				if (versions.length == 1) {
					location.href = '#pages';
				} else {
					var previousVersion = versions[0];
					if (versions.indexOf(version) == 0) {
						previousVersion = versions[1];
					} else {
						previousVersion = versions[versions
								.indexOf(version) - 1];
					}
					ctx.pageId = ctx.pages[previousVersion].id;
					ctx.loadData();
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
			ctx.pageId = r.message;
			ctx.loadData();
			Vosao.info(messages('page.version_success_add'));
		}
		else {
			Vosao.showServiceMessages(r);
		}			
		$('#version-dialog').dialog('close');
	}, ctx.page.friendlyURL, $('#version-title').val());
}

function onVersionTitleCancel() {
	$('#version-dialog').dialog('close');
}

function onVersionSelect(version) {
	var selPage = ctx.pages[version];
	ctx.pageId = selPage.id;
	ctx.loadData();
}

function showAuditInfo() {
	$('#pageState').html(ctx.page.stateString == 'EDIT' ? 
			messages('edit') : messages('approved'));
	$('#pageCreateDate').html(ctx.page.createDateTimeString);
	$('#pageModDate').html(ctx.page.modDateTimeString);
	$('#pageCreateUser').html(ctx.page.createUserEmail);
	$('#pageModUser').html(ctx.page.modUserEmail);
}

	return {
		initVersionDialog : initVersionDialog,
		loadVersions : loadVersions,
		showAuditInfo: showAuditInfo
	};

});
