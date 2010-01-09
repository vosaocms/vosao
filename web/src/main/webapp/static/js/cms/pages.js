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
	initJSONRpc(loadData);
});

function loadData() {
	loadTree();
	loadUser();
}

function loadTree() {
	jsonrpc.pageService.getTree(function(r) {
		$('#pages-tree').html(renderPage(r));
		$("#pages-tree").treeview();
	});
}

function renderPage(vo) {
	var pageUrl = encodeURIComponent(vo.entity.friendlyURL);
	var html = '<li><a href="page.jsp?id=' + vo.entity.id + '">'
			+ vo.entity.title
			+ '</a> <a title="Add child" href="page.jsp?parent=' + pageUrl
			+ '">+</a>';
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
	jsonrpc.userService.getLoggedIn(function(r) {
		if (!r.admin) {
		    $('#structuresTab').hide();
		}
	});
}