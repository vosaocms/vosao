// file ChildrenView.js
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

define(['text!template/page/children.html',
        'view/page/context', 'view/page/version', 'view/page/breadcrumbs'], 
function(childrenHtml, ctx, version, breadcrumbs) {
	
	console.log('Loading ChildrenView.js');
	
	var parentURL = null;
	var children = null;
	    
	function postRender() {
		ctx.loadData = loadData;
	    $("#page-dialog").dialog({ width: 400, autoOpen: false });
	    Vosao.initJSONRpc(loadData);
	    // hover states on the link buttons
	    $('a.button').hover(
	     	function() { $(this).addClass('ui-state-hover'); },
	       	function() { $(this).removeClass('ui-state-hover'); }
	    ); 
	    version.initVersionDialog();
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
			pageId = String(ctx.page.id);
			pageParentUrl = ctx.page.parentUrl;
			loadChildren();
			version.loadVersions();
			version.showAuditInfo();
		} else {
			pages['1'] = ctx.page;
		}
		loadPagePermission();
	}

	function callLoadChildren() {
		Vosao.jsonrpc.pageService.getChildren(function(r) {
			ctx.pageRequest.children = r;
			loadChildren();
		}, ctx.page.friendlyURL);
	}

	function loadChildren() {
		children = ctx.pageRequest.children.list;
	    var html = '<table class="form-table"><tr><th></th><th>' + messages('title') 
	    	+ '</th><th>' + messages('page.friendly_url') + '</th><th></th></tr>';
	    $.each(children, function (n, value) {
	        html += '<tr><td><input type="checkbox" value="' + value.id 
	        + '"/></td><td><a href="#page/content/' + value.id 
	        +'">' + value.title + '</a></td><td>' + value.friendlyURL + '</td>\
			<td><a class="pageup" data-n="' + n + '"><img src="/static/images/02_up.png"/></a>\
	        <a class="pagedown" data-n="' + n + '"><img src="/static/images/02_down.png"/></a>\
	        </td></tr>';
	    });
	    $('#children').html(html + '</table>');
	    $('#children tr:even').addClass('even');
	    $('a.pageup').click(function() {
	    	onPageUp(Number($(this).attr('data-n')));
	    });
	    $('a.pagedown').click(function() {
	    	onPageDown(Number($(this).attr('data-n')));
	    });
	}

	function onAddChild() {
		$('#ui-dialog-title-page-dialog').text(messages('pages.new_page'));
		parentURL = ctx.page.friendlyURL == '/' ? '' : ctx.page.friendlyURL;
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
	    var r = ctx.pageRequest.pagePermission;
	   	if (r.changeGranted) {
	   		$('#addChildButton').show();
	   		$('#deleteChildButton').show();
	   	}
	   	else {
	   		$('#addChildButton').hide();
	   		$('#deleteChildButton').hide();
	   	}
	   	if (r.admin && ctx.editMode) {
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
			location.href = "#page/" + r.id;
		}, ctx.page.friendlyURL);
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
					location.href = '#page/content/' + r.message;
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

	
	return Backbone.View.extend({
		
		css: '/static/css/children.css',
		
		el: $('#tab-1'),

		tmpl: _.template(childrenHtml),
		
		render: function() {
			Vosao.addCSSFile(this.css);
			this.el = $('#tab-1');
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
		    $("#page-dialog").dialog('destroy').remove();
			this.el.html('');
			Vosao.removeCSSFile(this.css);
		}
		
	});
	
});