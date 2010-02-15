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
var editMode = pageId != '';
var pageRequest = null;
var currentLanguage = '';
var structureTemplates = null;
    
$(function(){
    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
    Vosao.initJSONRpc(loadData);

    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 

    $("#version-dialog").dialog({ width: 400, autoOpen: false });

    $('#title').change(onTitleChange);
    $('#pageType').change(onPageTypeChange);
    $('#structure').change(onStructureChange);
    $('#pageSaveButton').click(onPageUpdate);
    $('#pageForm').submit(function() {onPageUpdate(); return false;});
    $('#pagePreview').click(onPagePreview);
    $('#pageCancelButton').click(onPageCancel);
    $('#addVersionLink').click(onAddVersion);
    $('#approveButton').click(onPageApprove);
    $('#versionSaveButton').click(onVersionTitleSave);
    $('#versionCancelButton').click(onVersionTitleCancel);

    $('ul.ui-tabs-nav li:nth-child(1)').addClass('ui-state-active')
    		.removeClass('ui-state-default');
});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadTemplates();
		loadStructures();
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
		loadLanguages();
		loadContents();
	} else {
		pages['1'] = page;
	}
	initPageForm();
	loadPagePermission();
}

function loadTemplates() {
	var r = pageRequest.templates;
	var html = '';
    $.each(r.list, function (n,value) {
        html += '<option value="' + value.id + '">' 
            + value.title + '</option>';
    });
    $('#templates').html(html);
    if (page != null) {
        $('#templates').val(page.template);
    }
    else {
        $('#templates').val($.cookie("page_template"));
    }
}

function initPageForm() {
	var urlEnd = pageParentUrl == '/' ? '' : '/';
	if (page != null) {
		$('#title').val(page.title);
		$('#titleDiv').hide();
		if (page.parentUrl == '' || page.parentUrl == null) {
			$('#friendlyUrl').hide();
			$('#friendlyUrl').val('');
			$('#parentFriendlyUrl').html('/');
		} else {
			$('#friendlyUrl').show();
			$('#friendlyUrl').val(page.pageFriendlyURL);
			$('#parentFriendlyUrl').html(page.parentFriendlyURL + urlEnd);
		}
	    if (pageRequest.children.list.length > 0) {
	     	$('#parentFriendlyUrl').hide();
	       	$('#friendlyUrl').hide();
	       	$('#friendlyUrlSpan').html(page.friendlyURL);
	    }
		$('#pageType').val(page.pageTypeString);
		$('#publishDate').val(page.publishDateString);
		$('#commentsEnabled').each(function() {
			this.checked = page.commentsEnabled
		});
		$('#templates').val(page.template);
		$('#pageState').html(page.stateString == 'EDIT' ? 'Edit' : 'Approved');
		$('#pageCreateDate').html(page.createDateString);
		$('#pageModDate').html(page.modDateString);
		$('#pageCreateUser').html(page.createUserEmail);
		$('#pageModUser').html(page.modUserEmail);
		$('#keywords').html(page.keywords);
		$('#description').html(page.description);
		$('.contentTab').show();
		$('.childrenTab').show();
		$('.commentsTab').show();
		$('.securityTab').show();
		$('#pagePreview').show();
		$('#versions').show();
	} else {
		$('#title').val('');
		$('#titleDiv').show();
		$('#titleLocal').val('');
		$('#friendlyUrl').show();
		$('#friendlyUrl').val('');
		$('#parentFriendlyUrl').html(pageParentUrl + urlEnd);
		$('#pageType').val('SIMPLE');
		$('#publishDate').val(Vosao.formatDate(new Date()));
		$('#commentsEnabled').each(function() {
			this.checked = false
		});
		$('#pageState').html('Edit');
		$('#pageCreateUser').html('');
		$('#pageCreateDate').html('');
		$('#pageModUser').html('');
		$('#pageModDate').html('');
		$('#keywords').html('');
		$('#description').html('');
		$('.contentTab').hide();
		$('.childrenTab').hide();
		$('.commentsTab').hide();
		$('.securityTab').hide();
		$('#pagePreview').hide();
		$('#versions').hide();
	}
	onPageTypeChange();
}

function onPageUpdate() {
	var pageVO = Vosao.javaMap( {
		id : pageId,
		titles : getTitles(),
		friendlyUrl : $('#parentFriendlyUrl').text() + $('#friendlyUrl').val(),
		publishDate : $('#publishDate').val(),
		commentsEnabled : String($('#commentsEnabled:checked').size() > 0),
		template : $('#templates option:selected').val(),
		approve : String($('#approveOnPageSave:checked, #approveOnContentSave:checked').size() > 0),
		pageType: $('#pageType').val(),
		structureId: $('#structure').val(),
		structureTemplateId: $('#structureTemplate').val(),
		keywords: $('#keywords').val(),
		description: $('#description').val()
	});
	$.cookie("page_template", pageVO.map.template, {path:'/', expires: 10});
	Vosao.jsonrpc.pageService.savePage(function(r) {
		if (r.result == 'success') {
			if (editMode) {
				location.href = '/cms/pages.jsp';
			}
			pageId = r.message;
			Vosao.info("Page was successfully saved.");
			location.href = '/cms/page/content.jsp?id=' + pageId;
		} else {
			Vosao.showServiceMessages(r);
		}
	}, pageVO);
}
    
function onTitleChange() {
	if (editMode) {
		return;
	}
	var url = $("#friendlyUrl").val();
	var title = $("#title").val();
	if (url == '') {
		$("#friendlyUrl").val(Vosao.urlFromTitle(title));
	}
}

function onPageTypeChange() {
	if ($('#pageType').val() == 'SIMPLE') {
		$('#structuredControls').hide();
	}
	if ($('#pageType').val() == 'STRUCTURED') {
		$('#structuredControls').show();
		$('#structure').val(page.structureId);
		onStructureChange();
	}
}

function onPagePreview() {
	var url = page.friendlyURL + '?language=' + currentLanguage 
		+ '&version=' + page.version;
	window.open(url, "preview");
}

function onPageCancel() {
	location.href = '/cms/pages.jsp';
}

function onPageApprove() {
	Vosao.jsonrpc.pageService.approve(function(r) {
		Vosao.showServiceMessages(r);
		callLoadPage();
	}, pageId);
}

function loadPagePermission() {
    var r = pageRequest.pagePermission;
   	if (r.publishGranted) {
   		$('#approveButton').show();
   		$('#approveOnPageSaveDiv').show();
    	}
   	else {
   		$('#approveButton').hide();
   		$('#approveOnPageSaveDiv').hide();
   	}
   	if (r.changeGranted) {
   		$('#pageSaveButton').show();
   	}
   	else {
   		$('#pageSaveButton').hide();
   	}
   	if (r.admin && editMode) {
   		$('.securityTab').show();
   	}
   	else {
   		$('.securityTab').hide();
   	}
}

function loadStructures() {
	var h = '';
	$.each(pageRequest.structures.list, function(i, struct) {
		var sel = i == 0 ? 'selected="selected"' : '';
		h += '<option ' + sel + ' value="' + struct.id + '">' + struct.title 
			+ '</option>';
	});
	$('#structure').html(h);
}

function onStructureChange() {
	var structureId = $('#structure').val();
	var h = '';
	Vosao.jsonrpc.structureTemplateService.selectByStructure(function(r) {
		var h = '';
		$.each(r.list, function(i, template) {
			var sel = i == 0 ? 'selected="selected"' : '';
			h += '<option ' + sel + ' value="' + template.id + '">' + template.title 
				+ '</option>';
		});
		$('#structureTemplate').html(h);
		$('#structureTemplate').val(page.structureTemplateId);
	}, structureId)
}

function getTitles() {
	if (!editMode) {
		return 'en' + $('#title').val();
	}
}

function loadLanguages() {
	var r = pageRequest.languages;
	languages = {};
	var h = '';
	$.each(r.list, function(i, value) {
		languages[value.code] = value;
	});
}

function loadContents() {
	if (editMode) {
		var r = pageRequest.contents;
		var allowedLangs = {};
		if (pageRequest.pagePermission.allLanguages) {
			allowedLangs = languages;
		}
		else {
			$.each(pageRequest.pagePermission.languagesList.list, 
					function(i, value) {
				allowedLangs[value] = languages[value];
			});
		}
		if (allowedLangs[Vosao.ENGLISH_CODE] != undefined) {
			currentLanguage = Vosao.ENGLISH_CODE;
		}
		else {
			currentLanguage = r.list[0].languageCode;
		}
	}
}
