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

$(function() {
	Vosao.initJSONRpc(loadData);
});

function loadData() {
	loadTree();
	loadUser();
}

function loadTree() {
	Vosao.jsonrpc.pageService.getTree(function(r) {
		$('#pages-tree').html(renderPage(r));
		$("#pages-tree").treeview({
			animated: "fast",
			collapsed: true,
			unique: true,
			persist: "cookie",
			cookieId: "pageTree"
		});
	});
}

function renderPage(vo) {
	var pageUrl = encodeURIComponent(vo.entity.friendlyURL);
	var p = vo.entity.hasPublishedVersion ? 'published' : 'unpublished';
	var html = '<li> <img src="/static/images/'+ p +'.png" title="' + p 
			+ '" width="16px" />' 
			+ ' <a href="page/content.jsp?id=' + vo.entity.id + '">'
			+ vo.entity.title
			+ '</a> <a title="Add child" href="page/index.jsp?parent=' + pageUrl
			+ '"><img src="/static/images/add.png"/></a> '
			+ '<a title="Remove" href="#" onclick="onPageRemove(\'' 
			+ vo.entity.friendlyURL + '\')">'
			+ '<img src="/static/images/02_x.png" /></a>';
	if (vo.children.list.length > 0) {
		html += '<ul>';
		$.each(vo.children.list, function(n, value) {
			html += renderPage(value);
		});
		html += '</ul>';
	}
	return html + '</li>';
}

function loadUser() {
	Vosao.jsonrpc.userService.getLoggedIn(function(r) {
		if (!r.admin) {
		    $('#structuresTab').hide();
		}
	});
}

function onPageRemove(url) {
	if (confirm('Are you shure?')) {
		Vosao.jsonrpc.pageService.remove(function(r) {
			Vosao.showServiceMessages(r);
			loadData();
		}, url);
	}
}