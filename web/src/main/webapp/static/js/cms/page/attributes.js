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
var attrDef = null;
var attributes = null;
var attributesMap = {};
var attrValues = {};
var languages = null;
    
$(function(){
    $("#attribute-dialog").dialog({ width: 430, autoOpen: false });
    $("#attrValue-dialog").dialog({ width: 430, autoOpen: false });
    Vosao.initJSONRpc(loadData);
    // hover states on the link buttons
    $('a.button').hover(
     	function() { $(this).addClass('ui-state-hover'); },
       	function() { $(this).removeClass('ui-state-hover'); }
    ); 
    initVersionDialog();
    $('#addButton').click(onAdd);
    $('#cancelButton').click(function() {
    	$("#attribute-dialog").dialog('close');
    });
    $('#cancelValueButton').click(function() {
    	$("#attrValue-dialog").dialog('close');
    });
    $('#attributeForm').submit(function() {onSave(); return false;});
    $('#attrValueForm').submit(function() {onSaveValue(); return false;});
    $('#deleteButton').click(onDelete);
    $('#language').change(onLanguageChange);
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
		loadLanguages();
		showAuditInfo();
		attrValues = page.attributes ? eval('(' + page.attributes + ')') : {};
		loadAttributes();
	} else {
		pages['1'] = page;
	}
	loadPagePermission();
}

function loadLanguages() {
	var r = pageRequest.languages;
	languages = {};
	var h = '';
	$.each(r.list, function(i, value) {
		languages[value.code] = value;
		h += '<option value="' + value.code + '" ' + '>' 
			+ value.title + '</option>';
	});
	$('#language').html(h);
	$('#language').val(Vosao.ENGLISH_CODE);
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
    var html = '<table class="form-table"><tr><th></th><th>' + messages('name') 
    	+ '</th><th>' + messages('value') +'</th><th>' + messages('inherited') 
    	+ '</th></tr>';
    $.each(attributes, function (n, value) {
    	attributesMap[value.name] = value;
    	var inherited = value.inherited ? messages('inherited') : '';
    	html += '<tr><td><input type="checkbox" value="' + value.id	
    		+ '"/></td><td><a href="#" onclick="onEditValue(\'' + value.name 
    		+ '\')">' + value.name + '</a></td><td>' 
    		+ getAttributeValue(value.name, Vosao.ENGLISH_CODE)
    		+ '</td><td>' + inherited + '</td></tr>';
    });
    $('#attributes').html(html + '</table>');
    $('#attributes tr:even').addClass('even'); 
}

function onAdd() {
	$('#name').val('');
	$('#value').val('');
	$("#attribute-dialog").dialog('open');
}

function onDelete() {
	var ids = [];
	$('#attributes input:checked').each(function() {
		ids.push(this.value);
	});
	if (ids.length == 0) {
		Vosao.info(messages('nothing_selected'));
		return;
	}
	if (confirm(messages('are_you_sure'))) {
		Vosao.jsonrpc.pageAttributeService.remove(function(r) {
			Vosao.showServiceMessages(r);
			loadData();
		}, Vosao.javaList(ids), page.id);
	}
}

function onSave() {
	var attrDefVO = {
		id : attrDef == null ? '' : String(attrDef.id),
		url : page.friendlyURL,
		name : $('#name').val(),
		title : $('#title').val(),
		inherited : String($('#inherited:checked').size() > 0),
		defaultValue : $('#defaultValue').val()
	};
	Vosao.jsonrpc.pageAttributeService.save(function(r) {
		if (r.result == 'success') {
			Vosao.info(messages('page.success_save'));
			$("#attribute-dialog").dialog('close');
			loadData();
			attrDefVO.inherited = $('#inherited:checked').size() > 0;
			attributesMap[attrDefVO.name] = attrDefVO;
			onEditValue(attrDefVO.name);
		} else {
			Vosao.showServiceMessages(r);
		}
	}, Vosao.javaMap(attrDefVO));
}

function getAttributeValue(name, language) {
	if (attrValues[name] && attrValues[name][language]) {
		return attrValues[name][language];
	}
	return '';
}

function onEditValue(name) {
	$("#attrValue-dialog").dialog('open');
	$('#attrName').html(name);
	$('#attrInherited').each(function() {this.checked = false;});
	if (attributesMap[name].inherited) {
		$('#applyChildren').show();
	}
	else {
		$('#applyChildren').hide();
	}
	var value = getAttributeValue(name, $('#language').val());
	if (!value && attributesMap[name].defaultValue) {
		if (confirm(messages('page.use_default_value'))) {
			value = attributesMap[name].defaultValue;
		}
	}
	$('#value').val(value);
	$('#language').val(Vosao.ENGLISH_CODE);
}

function checkPageAttrValue(name, lang) {
	if (attrValues[name] == undefined) {
		attrValues[name] = {};
	}
	if (attrValues[name][lang] == undefined) {
		attrValues[name][lang] = ''; 
	}
}

function setPageAttrValue(name, lang, value) {
	checkPageAttrValue(name, lang);
	attrValues[name][lang] = value;
}

function getPageAttrValue(name, lang) {
	checkPageAttrValue(name, lang);
	return attrValues[name][lang];
}

function onLanguageChange() {
	var name = $('#attrName').text();
	var lang = $('#language').val();
	var value = getPageAttrValue(name, lang);
	if (!value && attributesMap[name].defaultValue) {
		if (confirm(messages('page.use_default_value'))) {
			value = attributesMap[name].defaultValue;
		}
	}
	$('#value').val(value);
}

function onSaveValue() {
	var name = $('#attrName').text();
	var lang = $('#language').val();
	var value = $('#value').val();
	setPageAttrValue(name, lang, value);
	Vosao.jsonrpc.pageService.saveAttribute(function(r) {
		if (r.result == 'success') {
			$("#attrValue-dialog").dialog('close');
			Vosao.info(messages('success'));
			loadData();
		}
		else {
			Vosao.showServiceMessages(r);
		}
	}, page.id, name, value, lang, $('#attrInherited:checked').size() > 0);
}