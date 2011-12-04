// file CommentsView.js
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

define(['text!template/page/comments.html',
        'view/page/context', 'view/page/version', 'view/page/breadcrumbs'], 
function(commentsHtml, ctx, version, breadcrumbs) {
	
	console.log('Loading CommentsView.js');
	
	function postRender() {
		ctx.loadData = loadData;
		ctx.editMode = ctx.pageId != '';
	    Vosao.initJSONRpc(loadData);
	    // hover states on the link buttons
	    $('a.button').hover(
	     	function() { $(this).addClass('ui-state-hover'); },
	       	function() { $(this).removeClass('ui-state-hover'); }
	    ); 
	    version.initVersionDialog();
	    $('#enableCommentsButton').click(onEnableComments);
	    $('#disableCommentsButton').click(onDisableComments);
	    $('#deleteCommentsButton').click(onDeleteComments);
	    $('ul.ui-tabs-nav li:nth-child(4)').addClass('ui-state-active')
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
			loadComments();
			version.showAuditInfo();
		} else {
			ctx.pages['1'] = ctx.page;
		}
		loadPagePermission();
	}

	function loadComments() {
		var r = ctx.pageRequest.comments;
	    var html = '<table class="form-table"><tr><th></th><th>' + messages('state') 
	    	+ '</th><th>' + messages('name') +'</th><th>' + messages('content') 
	    	+ '</th></tr>';
	    $.each(r.list, function (n, value) {
	        var status = value.disabled ? messages('disabled') : messages('enabled');
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
			Vosao.info(messages('nothing_selected'));
			return;
		}
		Vosao.jsonrpc.commentService.enableComments(function(r) {
			Vosao.showServiceMessages(r);
			loadData();
		}, Vosao.javaList(ids));
	}

	function onDisableComments() {
		var ids = [];
		$('#comments input:checked').each(function() {
			ids.push(this.value);
		});
		if (ids.length == 0) {
			Vosao.info(messages('nothing_selected'));
			return;
		}
		Vosao.jsonrpc.commentService.disableComments(function(r) {
			Vosao.showServiceMessages(r);
			loadData();
		}, Vosao.javaList(ids));
	}

	function onDeleteComments() {
		var ids = [];
		$('#comments input:checked').each(function() {
			ids.push(this.value);
		});
		if (ids.length == 0) {
			Vosao.info(messages('nothing_selected'));
			return;
		}
		if (confirm(messages('are_you_sure'))) {
			Vosao.jsonrpc.commentService.deleteComments(function(r) {
				Vosao.showServiceMessages(r);
				loadData();
			}, Vosao.javaList(ids));
		}
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

	
	return Backbone.View.extend({
		
		el: $('#tab-1'),

		tmpl: _.template(commentsHtml),
		
		render: function() {
			this.el = $('#tab-1');
			this.el.html(this.tmpl({messages:messages}));
			postRender();
		},
		
		remove: function() {
			this.el.html('');
		}
		
	});
	
});