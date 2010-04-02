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

var parentURL = null;
var tree = null;
var pages = {};
var page = null;

var tree_options = {
	selected : encodeURL('/'),
	plugins : {
		cookie : { prefix : 'pages_' },	
		hotkeys : {
			functions : {
				'insert' : function() {
					$.tree.focused().create();
					return false;
				}
				/*'del' : function() {
					onPageRemove($.tree.focused().get_rollback().selected);
					return false;
				},
				'return' : function() {
					onPageEdit($.tree.focused().get_rollback().selected);
					return false;
				},
				'f2': function() {},						
				'ctrl+c': function() {},						
				'ctrl+x': function() {},					
				'ctrl+v': function() {}*/						
			}
		}
	},
	ui : {
		theme_name: "classic",
		animation: 200
	},
	callback : {
		onselect : function (node, tree_ong) {
			//alert('on select ' + node);
		}
	}
};

$(function() {
    $("#page-dialog").dialog({ width: 400, autoOpen: false });
	Vosao.initJSONRpc(loadData);
	$('#cancelDlgButton').click(onPageCancel);
    $('#pageForm').submit(function() {onSave(); return false;});
    $('#title').change(onTitleChange);
});

function loadData() {
	loadTree();
	loadUser();
}

function loadTree() {
	Vosao.jsonrpc.pageService.getTree(function(r) {
		$('#pages-tree').html('<ul>' + renderPage(r) + '</ul>');
		if (tree == null) {
		  $("#pages-tree").tree(tree_options);
		  tree = $.tree.reference('#pages-tree');
		}
		else {
			tree.init(tree_options);
		}
	});
}

function renderPage(vo) {
	pages[vo.entity.friendlyURL] = vo;
	var pageUrl = encodeURIComponent(vo.entity.friendlyURL);
	var html = '<li id="' + pageUrl.replace(/\%/g,'__') + '"> '
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

function encodeURL(url) {
	return encodeURIComponent(url).replace(/\%/g,'__');
}

function decodeURL(url) {
	return decodeURIComponent(url.replace(/__/g,'%'));
}

function onPageRemove(url) {
	var pageURL = decodeURL(url);
	if (pageURL == '/') {
		alert("You can't delete root page!");
		return;
	}
	if (confirm('Removing page. ' + pageURL + ' Are you shure?')) {
		Vosao.jsonrpc.pageService.remove(function(r) {
			Vosao.showServiceMessages(r);
			if (r.result == 'success') {
				$.tree.focused().remove();
			}
		}, pageURL);
	}
}

function onPageAdd(parentURI) {
	parent = decodeURL(parentURI);
	parentURL = parent == '/' ? '' : parent;
	$('#page-dialog').dialog('open');
	$('#parentURL').html(parentURL + '/');
	$('#title').val('');
	$('#url').val('');
	$('#title').focus();
	page = null;
}

function onPageEdit(uri) {
	var url = decodeURL(uri);
	page = pages[url];
	url = url == '/' ? '' : url;
	$('#page-dialog').dialog('open');
	$('#parentURL').html(page.entity.parentUrl + '/');
	$('#title').val(page.entity.title);
	$('#url').val(page.entity.pageFriendlyURL);
	$('#title').focus();
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
		return 'Title is empty';
	}
	else {
		if (vo.title.indexOf(',') != -1) {
			return 'Symbol , (coma) is not allowed in title.'
		}
	}
	if (vo.url == '') {
		return 'Page URL is empty';
	}
	else {
		if (vo.url.indexOf('/') != -1) {
			return 'Symbol / is not allowed in URL.'
		}
	}
}

function onSave() {
	var friendlyURL = parentURL + '/' + $('#url').val();
	var newURL = '';
	if (page != null) {
		friendlyURL = page.entity.friendlyURL;
		newURL = page.entity.parentUrl + '/' + $('#url').val();
	}
	var vo = {
		newPage : String(page == null),
		url : $('#url').val(),
		friendlyURL : friendlyURL,
		title : $('#title').val(),
		newURL : newURL
	};
	var error = validate(vo);
	if (!error) {
		Vosao.jsonrpc.pageService.updatePage(function(r) {
			if (r.result == 'success') {
				Vosao.info('Success');
				$('#page-dialog').dialog('close');
				loadData();
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