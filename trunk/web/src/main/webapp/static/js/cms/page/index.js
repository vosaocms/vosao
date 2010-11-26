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
var pageRequest = null;
var currentLanguage = '';
var structureTemplates = null;
var isDefault = false;
    
$(function(){
    $("#tag-dialog").dialog({ width: 400, autoOpen: false });
    $(".datepicker").datepicker({dateFormat:'dd.mm.yy'});
    Vosao.initJSONRpc(loadData);
    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 
    initVersionDialog();
    $('#title').change(onTitleChange);
    $('#pageType').change(onPageTypeChange);
    $('#structure').change(onStructureChange);
    $('#pageForm').submit(function() {onPageUpdate(); return false;});
    $('#pagePreview').click(onPagePreview);
    $('#pageCancelButton').click(onPageCancel);
    $('#approveButton').click(onPageApprove);
    $('ul.ui-tabs-nav li:nth-child(1)').addClass('ui-state-active')
    		.addClass('ui-tabs-selected')
    		.removeClass('ui-state-default');
    $('#metadata').click(function() {
    	$('#meta').toggle();
    });
    $('#addTag').click(onAddTag);
    $('#cached').click(function() { $('#dependenciesDiv').toggle(); });
});

function loadData() {
	Vosao.jsonrpc.pageService.getPageRequest(function(r) {
		pageRequest = r;
		page = pageRequest.page;
		loadTemplates();
		loadStructures();
		loadPage();
		breadcrumbsShow();
	}, pageId, pageParentUrl);
}

function checkDefault() {
	if (page != null && page.friendlyURL.endsWith('/_default')) {
		isDefault = true;
		$('.securityTab, .commentsTab, .childrenTab, #approveOnPageSaveDiv'
			+ ', #pagePreview, #versions, #tagsDiv').hide();
		$('#title, #friendlyUrl').attr('disabled', true);
	}
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
		loadLanguages();
		loadContents();
		showTags();
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
    if (page.id != null) {
        $('#templates').val(page.template);
    }
    else {
        $('#templates').val($.cookie("page_template"));
    }
}

function initPageForm() {
	var urlEnd = pageParentUrl == '/' ? '' : '/';
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
	$('#publishTime').val(page.publishTimeString);
	$('#endPublishDate').val(page.endPublishDateString);
	$('#endPublishTime').val(page.endPublishTimeString);
	$('#commentsEnabled').each(function() {
		this.checked = page.commentsEnabled;
	});
	$('#searchable').each(function() {
		this.checked = page.searchable;
	});
	$('#velocityProcessing').each(function() {
		this.checked = page.velocityProcessing;
	});
	$('#wikiProcessing').each(function() {
		this.checked = page.wikiProcessing;
	});
	$('#enableCkeditor').each(function() {
		this.checked = page.enableCkeditor;
	});
	$('#skipPostProcessing').each(function() {
		this.checked = page.skipPostProcessing;
	});
	$('#cached').each(function() {
		this.checked = page.cached;
	});
	$('#restful').each(function() {
		this.checked = page.restful;
	});
	$('#templates').val(page.template);
	$('#pageState').html(page.stateString == 'EDIT' ? 
			messages('edit') : messages('approved'));
	$('#pageCreateDate').html(page.createDateTimeString);
	$('#pageModDate').html(page.modDateTimeString);
	$('#pageCreateUser').html(page.createUserEmail);
	$('#pageModUser').html(page.modUserEmail);
	$('#keywords').val(page.keywords);
	$('#description').val(page.description);
	$('#headHtml').val(page.headHtml);
	$('#dependencies').val(pageRequest.dependencies);
	$('#contentType').val(page.contentType);
	if (page.cached) {
		$('#dependenciesDiv').show();
	}
	else {
		$('#dependenciesDiv').hide();
	}
	if (page.id != null) {
		$('.contentTab').show();
		$('.childrenTab').show();
		$('.commentsTab').show();
		$('.securityTab').show();
		$('#pagePreview').show();
		$('#versions').show();
		$('#tagsDiv').show();
	} else {
		$('.contentTab').hide();
		$('.childrenTab').hide();
		$('.commentsTab').hide();
		$('.securityTab').hide();
		$('#pagePreview').hide();
		$('#versions').hide();
		$('#tagsDiv').hide();
	}
	checkDefault();
	onPageTypeChange();
}

function getPublishDatetime() {
	return Vosao.strip($('#publishDate').val()) 
		+ ' ' + Vosao.strip($('#publishTime').val()) + ':00';	
}

function getEndPublishDatetime() {
	if ($('#endPublishDate').val()) {
		return Vosao.strip($('#endPublishDate').val()) 
			+ ' ' + Vosao.strip($('#endPublishTime').val()) + ':00';
	}
	return '';
}

function onPageUpdate() {
	var pageVO = Vosao.javaMap( {
		id : pageId,
		titles : getTitles(),
		friendlyUrl : $('#parentFriendlyUrl').text() + $('#friendlyUrl').val(),
		publishDate : getPublishDatetime(),
		endPublishDate : getEndPublishDatetime(),
		commentsEnabled : String($('#commentsEnabled:checked').size() > 0),
		searchable : String($('#searchable:checked').size() > 0),
		velocityProcessing : String($('#velocityProcessing:checked').size() > 0),
		wikiProcessing : String($('#wikiProcessing:checked').size() > 0),
		enableCkeditor : String($('#enableCkeditor:checked').size() > 0),
		skipPostProcessing : String($('#skipPostProcessing:checked').size() > 0),
		cached : String($('#cached:checked').size() > 0),
		restful : String($('#restful:checked').size() > 0),
		template : $('#templates option:selected').val(),
		approve : String($('#approveOnPageSave:checked, #approveOnContentSave:checked').size() > 0),
		pageType: $('#pageType').val(),
		structureId: $('#structure').val(),
		structureTemplateId: $('#structureTemplate').val(),
		keywords: $('#keywords').val(),
		description: $('#description').val(),
		dependencies: $('#dependencies').val(),
		contentType: $('#contentType').val(),
		headHtml: $('#headHtml').val()
	});
	$.cookie("page_template", pageVO.map.template, {path:'/', expires: 10});
	Vosao.jsonrpc.pageService.savePage(function(r) {
		if (r.result == 'success') {
			if (editMode) {
				location.href = '/cms/pages.vm';
			}
			pageId = r.message;
			Vosao.info(messages('page.success_save'));
			location.href = '/cms/page/content.vm?id=' + pageId;
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
		if (page != null) {
			$('#structure').val(page.structureId);
		}
		onStructureChange();
	}
}

function onPagePreview() {
	var url = page.friendlyURL + '?language=' + currentLanguage 
		+ '&version=' + page.version;
	window.open(url, "preview");
}

function onPageCancel() {
	location.href = '/cms/pages.vm';
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
   	checkDefault();
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
		if (page != null) {
			$('#structureTemplate').val(page.structureTemplateId);
		}
	}, structureId)
}

function getTitles() {
	if (!editMode) {
		return '{en:"' + $('#title').val() + '"}';
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

// Tags

function onAddTag() {
	Vosao.jsonrpc.tagService.getTree(function(r) {
		$('#tagTree').html(renderTags(r.list));
        $("#tagTree").treeview({
			animated: "fast",
			collapsed: true,
			unique: true,
			persist: "cookie",
			cookieId: "tagTree"
		});
        $('#tag-dialog').dialog('open');
	});
}

function renderTags(list) {
	var html = ''
	$.each(list, function (i, value) {
		html += renderTag(value);
	});
	return html;
}

function renderTag(vo) {
	var html = '<li><a href="#" onclick="onTagSelect(' + vo.entity.id + ')">' 
        + vo.entity.name + '</a>';
    if (vo.children.list.length > 0) {
        html += '<ul>';
        $.each(vo.children.list, function(n, value) {
            html += renderTag(value);
        });
        html += '</ul>';
    }
    return html + '</li>';
}

function onTagSelect(id) {
	Vosao.jsonrpc.tagService.addTag(function(r){
		Vosao.showServiceMessages(r);
	    $('#tag-dialog').dialog('close');
		callLoadTags();
	}, page.friendlyURL, id);
}

function callLoadTags() {
	Vosao.jsonrpc.pageService.getPageTags(function(r) {
		pageRequest.tags = r;
		showTags();
	}, page.friendlyURL);
}

function showTags() {
	var h = '';
	$.each(pageRequest.tags.list, function (i,value) {
		h += '<span class="tag">' + value.name + ' <a href="#" onclick="onTagRemove('
			+ value.id + ')"><img src="/static/images/02_x.png"/></a></span>';
	});
	$('#tags').html(h);
}

function onTagRemove(id) {
	Vosao.jsonrpc.tagService.removeTag(function(r) {
		Vosao.showServiceMessages(r);
		callLoadTags();
	}, page.friendlyURL, id);
}