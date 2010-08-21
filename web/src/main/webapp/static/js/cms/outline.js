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

var tree_options = {
	selected : '',
	plugins : {
		cookie : { prefix : 'pages_' },	
		hotkeys : {
			functions : {
				'insert' : function() {
					$.tree.focused().create();
					return false;
				}
			}
		}
	},
	ui : {
		theme_name: "classic",
		animation: 200
	},
	callback : {
		onrename : renamePage,
		beforedelete : function(node, tree_obj) {
			return confirm('Delete page. Are you shure?');
		},
		ondelete : deletePage,
		onmove : movePage,
		oncopy : copyPage
	}
};

$(function() {
	Vosao.initJSONRpc(loadData);
});

function loadData() {
	loadTree();
	loadUser();
}

function loadTree() {
	Vosao.jsonrpc.pageService.getTree(function(r) {
		$('#pages-tree').html('<ul>' + renderPage(r) + '</ul>');
		tree_options.selected = String(r.entity.id);
		$("#pages-tree").tree(tree_options);
	});
}

function renderPage(vo) {
	var html = '<li id="' + vo.entity.id + '"> '
		    + ' <a href="#"><ins> </ins>' + vo.entity.title + '</a>';
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

function renamePage(node, tree_obj, rb) {
	var parent = tree_obj.parent(node);
	var parentId = parent ? $(parent).attr('id') : null;
	var title = $(node).children('a').text().trim();
	var nodeId = $(node).attr('id') ? $(node).attr('id') : null;
	Vosao.jsonrpc.pageService.rename(function(r) {
		if (r.result == 'success') {
			if (nodeId == null) {
				$(node).attr('id', r.message);
			}
		}
		else {
			Vosao.showServiceMessages(r);
		}
	}, nodeId, parentId, title);
	
}

function deletePage(node, tree_obj, rb) {
	var nodeId = $(node).attr('id');
	Vosao.jsonrpc.pageService.removePage(function(r) {
		Vosao.showServiceMessages(r);
	}, nodeId);
}

function movePage(node, ref_node, type, tree_obj, rb) {
	var nodeId = $(node).attr('id');
	var refNodeId = $(ref_node).attr('id');
	Vosao.jsonrpc.pageService.movePage(function(r) {
		Vosao.showServiceMessages(r);
	}, nodeId, refNodeId, type);
}

function copyPage(node, ref_node, type, tree_obj, rb) {
	var nodeId = $(node).attr('id').replace('_copy', '');
	var refNodeId = $(ref_node).attr('id');
	Vosao.jsonrpc.pageService.copyPage(function(r) {
		Vosao.showServiceMessages(r);
		location.reload();
	}, nodeId, refNodeId, type);
}
