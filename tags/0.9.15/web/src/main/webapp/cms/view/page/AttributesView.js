// file AttributesView.js
/*
 Vosao CMS. Simple CMS for Google App Engine.
 
 Copyright (C) 2009-2011 Vosao development team.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 email: vosao.dev@gmail.com
*/

define(['text!template/page/attributes.html',
        'view/page/context', 'view/page/version', 'view/page/breadcrumbs'], 
function(attributesHtml, ctx, version, breadcrumbs) {
	
	console.log('Loading AttributesView.js');
	
	var attrDef = null;
	var attributes = null;
	var attributesMap = {};
	var attrValues = {};
	var languages = null;
	    
	function postRender() {
		ctx.loadData = loadData;
		ctx.editMode = ctx.pageId != '';
	    $("#attribute-dialog").dialog({ width: 430, autoOpen: false });
	    $("#attrValue-dialog").dialog({ width: 430, autoOpen: false });
	    Vosao.initJSONRpc(loadData);
	    // hover states on the link buttons
	    $('a.button').hover(
	     	function() { $(this).addClass('ui-state-hover'); },
	       	function() { $(this).removeClass('ui-state-hover'); }
	    ); 
	    version.initVersionDialog();
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
	}

	function loadData() {
		Vosao.jsonrpc.pageService.getPageRequest(function(r) {
			ctx.pageRequest = r;
			ctx.page = ctx.pageRequest.page;
			loadPage();
			breadcrumbs.breadcrumbsShow();
		}, ctx.pageId, ctx.pageParentUrl);
	}

	function callLoadPage() {
		Vosao.jsonrpc.pageService.getPageRequest(function(r) {
			ctx.pageRequest = r;
			ctx.page = ctx.pageRequest.page;
			ctx.editMode = ctx.pageId != null;
			loadPage();
		}, ctx.pageId, ctx.pageParentUrl);
	}

	function loadPage() {
		if (ctx.editMode) {
			ctx.pageId = String(ctx.page.id);
			ctx.pageParentUrl = ctx.page.parentUrl;
			version.loadVersions();
			loadLanguages();
			version.showAuditInfo();
			attrValues = ctx.page.attributes ? eval('(' + ctx.page.attributes + ')') : {};
			loadAttributes();
		} else {
			ctx.pages['1'] = ctx.page;
		}
		loadPagePermission();
	}

	function loadLanguages() {
		var r = ctx.pageRequest.languages;
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
	    var r = ctx.pageRequest.pagePermission;
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
	   	if (r.admin && ctx.editMode) {
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
		}, ctx.page.friendlyURL);
	}

	function showAttributes() {
	    var html = '<table class="form-table"><tr><th></th><th>' + messages('name') 
	    	+ '</th><th>' + messages('value') +'</th><th>' + messages('inherited') 
	    	+ '</th></tr>';
	    $.each(attributes, function (n, value) {
	    	attributesMap[value.name] = value;
	    	var inherited = value.inherited ? messages('inherited') : '';
	    	html += '<tr><td><input type="checkbox" value="' + value.id	
	    		+ '"/></td><td><a data-name="' + value.name	+ '">' 
	    		+ value.name + '</a></td><td>' 
	    		+ getAttributeValue(value.name, Vosao.ENGLISH_CODE)
	    		+ '</td><td>' + inherited + '</td></tr>';
	    });
	    $('#attributes').html(html + '</table>');
	    $('#attributes tr:even').addClass('even');
	    $('#attributes a').click(function() {
	    	var name = $(this).attr('data-name');
	    	if (name) {
	    		onEditValue(name);
	    	}
	    });
	}

	function onAdd() {
		$('#name').val('');
		$('#title').val('');
		$('#defaultValue').val('');
		$('#inherited').each(function() { this.checked = false;} );
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
			}, Vosao.javaList(ids), ctx.page.id);
		}
	}

	function onSave() {
		var attrDefVO = {
			id : attrDef == null ? '' : String(attrDef.id),
			url : ctx.page.friendlyURL,
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
				if (attrDefVO.inherited && attrDefVO.defaultValue) {
					loadData();	
				}
				else {
					onEditValue(attrDefVO.name);
				}
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
		}, ctx.page.id, name, value, lang, $('#attrInherited:checked').size() > 0);
	}
	
	
	return Backbone.View.extend({
		
		el: $('#tab-1'),

		tmpl: _.template(attributesHtml),
		
		render: function() {
			this.el = $('#tab-1');
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#attribute-dialog, #attrValue-dialog").dialog('destroy').remove();
			this.el.html('');
		}
		
	});
	
});