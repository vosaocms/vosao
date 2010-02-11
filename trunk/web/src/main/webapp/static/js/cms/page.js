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
var pages = {};
var versions = [];
var contents = null;
var titles = null;
var currentLanguage = '';
var languages = null;
var children = {list:[]};
var editMode = pageId != '';
var contentEditor = null;
var etalonContent = '';
var autosaveTimer = '';
var permission = null;
var permissions = null;
var groups = null;
var pageRequest = null;
var structureTemplates = null;
var contentEditors = null;
var browseId = '';
var tab = null;
    
$(function(){
    tab = $("#tabs").tabs();
    $("#tabs").bind('tabsselect', tabSelected);
    Vosao.selectTabFromQueryParam(tab);
    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
    Vosao.initJSONRpc(loadData);

    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 

    $("#version-dialog").dialog({ width: 400, autoOpen: false });
    $("#permission-dialog").dialog({ width: 400, autoOpen: false });

    $('#title').change(onTitleChange);
    $('#pageType').change(onPageTypeChange);
    $('#structure').change(onStructureChange);
    $('#pageSaveButton').click(onPageUpdate);
    $('#pagePreview').click(onPagePreview);
    $('#pageCancelButton').click(onPageCancel);
    $('#addVersionLink').click(onAddVersion);
    $('#autosave').change(onAutosave);
    $('#language').change(onLanguageChange);
    $('#saveContinueContentButton').click(saveContent);
    $('#saveContentButton').click(onPageUpdate);
    $('#contentPreviewButton').click(onPagePreview);
    $('#approveButton').click(onPageApprove);
    $('#contentCancelButton').click(onPageCancel);
    $('#addChildButton').click(onAddChild);
    $('#deleteChildButton').click(onDelete);
    $('#enableCommentsButton').click(onEnableComments);
    $('#disableCommentsButton').click(onDisableComments);
    $('#deleteCommentsButton').click(onDeleteComments);
    $('#versionSaveButton').click(onVersionTitleSave);
    $('#versionCancelButton').click(onVersionTitleCancel);
    $('#addPermissionButton').click(onAddPermission);
    $('#deletePermissionButton').click(onDeletePermission);
    $('#permissionSaveButton').click(onPermissionSave);
    $('#permissionCancelButton').click(onPermissionCancel);
    $('#allLanguages').change(onAllLanguagesChange);
});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadLanguages();
		loadTemplates();
		loadStructures();
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
		loadChildren();
		loadVersions();
		loadComments();
		loadPermissions();
		loadGroups();
		loadTitles();
	} else {
		pages['1'] = page;
	}
	initPageForm();
	loadPagePermission();
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

function loadLanguages() {
	var r = pageRequest.languages;
	languages = {};
	var h = '';
	$.each(r.list, function(i, value) {
		languages[value.code] = value;
	});
	fillLanguagesList(languages);
	setPermissionLanguages();
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
		showContentEditor();
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
		content : getEditorContent(),
		template : $('#templates option:selected').val(),
		approve : String($('#approveOnPageSave:checked, #approveOnContentSave:checked').size() > 0),
		languageCode : currentLanguage,
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
			editMode = pageId != null;
			loadData();
			tab.tabs('select', 1);
			Vosao.info("Page was successfully saved.");
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

function tabSelected(event, ui) {
	if (ui.index == 1 && $('#autosave:checked').size() > 0) {
		startAutosave();
	} else {
		stopAutosave();
	}
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
		var xml = '<content>';
		$.each(pageRequest.structureFields.list, function(i, field) {
			if (field.type == 'TEXT' || field.type == 'DATE' 
				|| field.type == 'RESOURCE') {
				xml += '<' + field.name + '>' + $('#field' + field.name).val()
					+ '</' + field.name + '>';
			}
			if (field.type == 'TEXTAREA') {
				xml += '<' + field.name + '>'
					+ Vosao.escapeHtml(contentEditors[field.name].getData())  
					+ '</' + field.name + '>';
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
        + '"/></td><td><a href="/cms/page.jsp?id=' + value.id 
        +'">' + value.title + '</a></td><td>' + value.friendlyURL
        + '</td></tr>';
    });
    $('#children').html(html + '</table>');
    $('#children tr:even').addClass('even');
    if (r.list.length > 0) {
     	$('#parentFriendlyUrl').hide();
       	$('#friendlyUrl').hide();
       	$('#friendlyUrlSpan').html(page.friendlyURL);
    }
}

function onAddChild() {
	location.href = '/cms/page.jsp?parent=' + encodeURIComponent(page.friendlyURL);
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
		loadComments();
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
		loadComments();
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
			loadComments();
		}, Vosao.javaList(ids));
	}
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

function onVersionDelete(version) {
	if (confirm('Are you sure?')) {
		var delPage = pages[version];
		Vosao.jsonrpc.pageService.deletePages(function(r) {
			if (version == String(page.version)) {
				if (versions.length == 1) {
					location.href = '/cms/pages.jsp';
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
		}, Vosao.javaList( [ delPage.id ]));
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
			Vosao.info('Version was successfully added.');
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

function onPageApprove() {
	Vosao.jsonrpc.pageService.approve(function(r) {
		Vosao.showServiceMessages(r);
		callLoadPage();
	}, pageId);
}

function getPermissionName(perm) {
	if (perm == 'DENIED') {
		return 'Denied';
	}
	if (perm == 'READ') {
		return 'Read';
	}
	if (perm == 'WRITE') {
		return 'Read, Write';
	}
	if (perm == 'PUBLISH') {
		return 'Read, Write, Publish';
	}
	if (perm == 'ADMIN') {
		return 'Read, Write, Publish, Grant permissions';
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
	var h = '<table class="form-table"><tr><th></th><th>Group</th><th>Permission</th><th>Languages</th></tr>';
	$.each(permissions, function(i,value) {
		var checkbox = '';
		var editLink = value.group.name;
		if (!value.inherited) {
			checkbox = '<input type="checkbox" value="' + value.id + '">';
			editLink = '<a href="#" onclick="onPermissionEdit(\'' + value.id 
				+ '\')"> ' + value.group.name + '</a>';
		}
		var l = value.allLanguages ? 'all languages' : value.languages;
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
	var h = '<fieldset><legend>Languages</legend>';
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
		languages: $('#allLanguages')[0].checked ? '' : langs,
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
		Vosao.info('Nothing selected.');
		return;
	}
	if (confirm('Are you sure?')) {
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
   		$('#addChildButton').show();
   		$('#deleteChildButton').show();
   		$('#enableCommentsButton').show();
   		$('#disableCommentsButton').show();
   		$('#deleteCommentsButton').show();
   	}
   	else {
   		$('#pageSaveButton').hide();
   		$('#saveContinueContentButton').hide();
   		$('#saveContentButton').hide();
   		$('#addChildButton').hide();
   		$('#deleteChildButton').hide();
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
	window.open('fileBrowser.jsp?mode=page');
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