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
var contents = null;
var titles = null;
var currentLanguage = '';
var languages = null;
var editMode = pageId != '';
var contentEditor = null;
var etalonContent = '';
var autosaveTimer = '';
var pageRequest = null;
var contentEditors = null;
var browseId = '';
    
$(function(){
    Vosao.initJSONRpc(loadData);

    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 

    $("#version-dialog").dialog({ width: 400, autoOpen: false });

    $('#addVersionLink').click(onAddVersion);
    $('#autosave').change(onAutosave);
    $('#language').change(onLanguageChange);
    $('#saveContinueContentButton').click(saveContent);
    $('#saveContentButton').click(onPageUpdate);
    $('#pageForm').submit(function() {onPageUpdate(); return false;});
    $('#contentPreviewButton').click(onPagePreview);
    $('#approveButton').click(onPageApprove);
    $('#contentCancelButton').click(onPageCancel);
    $('#versionSaveButton').click(onVersionTitleSave);
    $('#versionCancelButton').click(onVersionTitleCancel);
    
    $('ul.ui-tabs-nav li:nth-child(2)').addClass('ui-state-active')
    		.removeClass('ui-state-default');

});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadLanguages();
		loadPage();
		if (editMode) {
			loadContents();
		}
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
		loadTitles();
	} else {
		pages['1'] = page;
	}
	initPageForm();
	loadPagePermission();
}

function loadLanguages() {
	var r = pageRequest.languages;
	languages = {};
	var h = '';
	$.each(r.list, function(i, value) {
		languages[value.code] = value;
	});
	fillLanguagesList(languages);
}
 
function fillLanguagesList(langsMap) {
	var h = '';
	$.each(langsMap, function(code, lang) {
		h += '<option value="' + code + '" ' + '>'
				+ lang.title + '</option>';
	});
	$('#language').html(h);
}

function initPageForm() {
	var urlEnd = pageParentUrl == '/' ? '' : '/';
	if (page != null) {
		$('#pageState').html(page.stateString == 'EDIT' ? 'Edit' : 'Approved');
		$('#pageCreateDate').html(page.createDateString);
		$('#pageModDate').html(page.modDateString);
		$('#pageCreateUser').html(page.createUserEmail);
		$('#pageModUser').html(page.modUserEmail);
		$('.contentTab').show();
		$('.childrenTab').show();
		$('.commentsTab').show();
		$('.securityTab').show();
		$('#pagePreview').show();
		$('#versions').show();
		showContentEditor();
	} else {
		$('#titleLocal').val('');
		$('#pageState').html('Edit');
		$('#pageCreateUser').html('');
		$('#pageCreateDate').html('');
		$('#pageModUser').html('');
		$('#pageModDate').html('');
		$('.contentTab').hide();
		$('.childrenTab').hide();
		$('.commentsTab').hide();
		$('.securityTab').hide();
		$('#pagePreview').hide();
		$('#versions').hide();
	}
}

function onPageUpdate() {
	var pageVO = Vosao.javaMap( {
		id : pageId,
		titles : getTitles(),
		content : getEditorContent(),
		approve : String($('#approveOnPageSave:checked, #approveOnContentSave:checked').size() > 0),
		languageCode : currentLanguage
	});
	Vosao.jsonrpc.pageService.savePage(function(r) {
		if (r.result == 'success') {
			Vosao.info("Page was successfully saved.");
			location.href = '/cms/pages.jsp';
		} else {
			Vosao.showServiceMessages(r);
		}
	}, pageVO);
}
    
function startAutosave() {
	if (editMode) {
		if (autosaveTimer == '') {
			autosaveTimer = setInterval(saveContent, 
					Vosao.AUTOSAVE_TIMEOUT * 1000);
		}
	}
}

function stopAutosave() {
	if (autosaveTimer != '') {
		clearInterval(autosaveTimer);
		autosaveTimer = '';
	}
}

function getEditorContent() {
	if (!editMode) return '';
	if (page.simple) {
		return contentEditor.getData();
	}
	if (page.structured) {
		var xml = '<?xml version="1.0" encoding="utf-8"?>\n<content>\n';
		$.each(pageRequest.structureFields.list, function(i, field) {
			if (field.type == 'TEXT' || field.type == 'DATE' 
				|| field.type == 'RESOURCE') {
				xml += '<' + field.name + '>' + $('#field' + field.name).val()
					+ '</' + field.name + '>\n';
			}
			if (field.type == 'TEXTAREA') {
				xml += '<' + field.name + '>'
					+ Vosao.escapeHtml(contentEditors[field.name].getData())  
					+ '</' + field.name + '>\n';
			}
		});
		return xml + '</content>';
	}
}

function setEditorContent(data) {
	if (page.simple) {
	    contentEditor.setData(data);
	}
	if (page.structured) {
		$.each(pageRequest.structureFields.list, function(i, field) {
			if (field.type == 'TEXT' || field.type == 'DATE' 
				|| field.type == 'RESOURCE') {
				$(data).find(field.name).each(function() {
					$('#field' + field.name).val($(this).text())					
				});
			}
			if (field.type == 'TEXTAREA') {
				$(data).find(field.name).each(function() {
					contentEditors[field.name].setData($(this).text());					
				});
			}
		});
	}
}

function isContentChanged() {
	return contents[currentLanguage] != getEditorContent();
}

function saveContent() {
	var content = getEditorContent();
	var approve = $('#approveOnContentSave:checked').size() > 0;
	contents[currentLanguage] = content;
	Vosao.jsonrpc.pageService.updateContent(function(r) {
		titles[currentLanguage] = $('#titleLocal').val();
		if (r.result == 'success') {
			var now = new Date();
			Vosao.info(r.message + " " + now);
			callLoadPage();
		} else {
			Vosao.error(r.message);
		}
	}, pageId, content, $('#titleLocal').val(), currentLanguage, approve);
}

function onAutosave() {
	if ($("#autosave:checked").length > 0) {
		startAutosave();
	} else {
		stopAutosave();
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

function onLanguageChange() {
	if (!isContentChanged()
			|| confirm('Are you sure? All changes will be lost.')) {
		currentLanguage = $('#language').val();
		if (contents[currentLanguage] == undefined) {
			contents[currentLanguage] = '';
		}
		setEditorContent(contents[currentLanguage]);
		$('#titleLocal').val(getTitle());
	} else {
		$('#language').val(currentLanguage);
	}
}

function loadContents() {
	if (editMode) {
		var r = pageRequest.contents;
		contents = [];
		$.each(r.list, function(i, value) {
			contents[value.languageCode] = value.content;
		});
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
		fillLanguagesList(allowedLangs);
		if (allowedLangs[Vosao.ENGLISH_CODE] != undefined) {
			currentLanguage = Vosao.ENGLISH_CODE;
		}
		else {
			currentLanguage = r.list[0].languageCode;
		}
		setEditorContent(contents[currentLanguage]);
		$('#titleLocal').val(getTitle());
	} else {
		setEditorContent('');
	}
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
   		$('#approveOnContentSaveDiv').show();
   	}
   	else {
   		$('#approveButton').hide();
   		$('#approveOnPageSaveDiv').hide();
   		$('#approveOnContentSaveDiv').hide();
   	}
   	if (r.changeGranted) {
   		$('#pageSaveButton').show();
   		$('#saveContinueContentButton').show();
   		$('#saveContentButton').show();
   	}
   	else {
   		$('#pageSaveButton').hide();
   		$('#saveContinueContentButton').hide();
   		$('#saveContentButton').hide();
   	}
   	if (r.admin && editMode) {
   		$('.securityTab').show();
   	}
   	else {
   		$('.securityTab').hide();
   	}
}

function showContentEditor() {
	if (page.simple && contentEditor == null) {
		$('#page-content').html('<textarea id="content" rows="20" cols="80"></textarea>');
	    contentEditor = CKEDITOR.replace('content', {
	        height: 350, width: 'auto',
	        filebrowserUploadUrl : '/cms/upload',
	        filebrowserBrowseUrl : '/cms/fileBrowser.jsp'
	    });
	}
	if (page.structured && contentEditors == null) {
		var h = '';
		$.each(pageRequest.structureFields.list, function(i, field) {
			h += '<div><div class="label">' + field.title + ':</div>';
			if (field.type == 'TEXT') {
				h += '<input id="field' + field.name + '" size="30"/>';
			}
			if (field.type == 'TEXTAREA') {
				h += '<textarea id="field' + field.name + '"></textarea>';
			}
			if (field.type == 'DATE') {
				h += '<input id="field' + field.name + '" class="datepicker" size="8" />';
			}
			if (field.type == 'RESOURCE') {
				h += '<input id="field' + field.name + '" size="60"/> '
					+ '<a href="#" onclick="browseResources(\'field' + field.name 
					+ '\')">Browse</a>';
			}
			h += '</div>';
		});
		$('#page-content').html(h);
		$('#page-content').css('float','left');
	    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
		contentEditors = [];
		$.each(pageRequest.structureFields.list, function(i, field) {
			if (field.type == 'TEXTAREA') {
				if (contentEditors[field.name] == undefined) {
					var ceditor = CKEDITOR.replace('field' + field.name, {
				        height: 150, width: 'auto',
				        filebrowserUploadUrl : '/cms/upload',
				        filebrowserBrowseUrl : '/cms/fileBrowser.jsp'
				    });
					contentEditors[field.name] = ceditor;
				}
			}
		});
	}
}

function browseResources(id) {
	browseId = id;
	window.open('/cms/fileBrowser.jsp?mode=page');
}

function setResource(path) {
	$('#' + browseId).val(path);
}

function loadTitles() {
	titles = page.titles.map;
}

function getTitle() {
	if (titles[currentLanguage] == undefined) {
		return '';
	}
	return titles[currentLanguage];
}

function getTitles() {
	if (!editMode) {
		return 'en' + $('#title').val();
	}
	titles[currentLanguage] = $('#titleLocal').val();
	var result = '';
	var count = 0;
	$.each(titles, function(lang, value) {
		var coma = count++ == 0 ? '' : ',';
		result += coma + lang + value;
	});
	return result;
}